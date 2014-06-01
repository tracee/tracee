package io.tracee.servlet;

import io.tracee.Tracee;
import io.tracee.TraceeConstants;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.eclipse.jetty.server.DispatcherType;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.EnumSet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;

public class TraceeFilterIT {

	private Server server;
	private String serverUrl;

	@Before
	public void startJetty() throws Exception {
		server = new Server(new InetSocketAddress("127.0.0.1", 0));
		ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.NO_SECURITY);
		contextHandler.setContextPath("/");
		contextHandler.addServlet(SillyServlet.class, "/sillyServlet");
		contextHandler.addServlet(SillyServlet.class, "/sillyFlushingServlet");

		contextHandler.addFilter(TraceeFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
		contextHandler.addEventListener(new TraceeServletRequestListener());

		server.setHandler(contextHandler);
		server.start();
		serverUrl = "http://"+server.getConnectors()[0].getName()+"/";
	}

	@After
	public void stopJetty() throws Exception {
		if (server != null) {
			server.stop();
			server.join();
		}
	}

	@Test
	public void testCompleteRoundtrip() throws Exception {
		final Header traceeResponseHeader = get("sillyServlet","{ \"inClient\":\"yes\" }").getFirstHeader(TraceeConstants.HTTP_HEADER_NAME);

		assertThat(traceeResponseHeader, notNullValue());
		assertThat(traceeResponseHeader.getValue(), containsString("\"inServlet\":\"yes\""));
		assertThat(traceeResponseHeader.getValue(), containsString("\"inClient\":\"yes\""));
		assertThat(traceeResponseHeader.getValue(), containsString("\"" + TraceeConstants.REQUEST_ID_KEY + "\":\""));
	}

	@Test
	public void testRoundtripWithPrematurlyFlushedBuffer() throws Exception {
		final Header traceeResponseHeader = get("sillyFlushingServlet","{ \"inClient\":\"yes\" }").getFirstHeader(TraceeConstants.HTTP_HEADER_NAME);

		assertThat(traceeResponseHeader, notNullValue());
		assertThat(traceeResponseHeader.getValue(), containsString("\"inClient\":\"yes\""));
		assertThat(traceeResponseHeader.getValue(), containsString("\"" + TraceeConstants.REQUEST_ID_KEY + "\":\""));
	}

	public static final class SillyServlet extends HttpServlet {
		@Override
		protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			assertThat(req.getHeader(TraceeConstants.HTTP_HEADER_NAME), Matchers.containsString("\"inClient\":\"yes\""));
			Tracee.getBackend().put("inServlet", "yes");
		}
	}

	public static final class SillyFlushingServlet extends HttpServlet {
		@Override
		protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			assertThat(req.getHeader(TraceeConstants.HTTP_HEADER_NAME), Matchers.containsString("\"inClient\":\"yes\""));
			Tracee.getBackend().put("inServlet", "yes");
			resp.getWriter().append("Hello World");
			resp.flushBuffer();
		}
	}

	private HttpResponse get(String servlet, String traceeHeader) throws IOException {
		final HttpClient client = new DefaultHttpClient();
		final HttpGet httpGet = new HttpGet(serverUrl+servlet);
		if (traceeHeader != null) {
			httpGet.setHeader(TraceeConstants.HTTP_HEADER_NAME, traceeHeader);
		}
		return client.execute(httpGet);
	}

}
