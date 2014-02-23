package de.holisticon.util.tracee.servlet;

import de.holisticon.util.tracee.Tracee;
import de.holisticon.util.tracee.TraceeConstants;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.eclipse.jetty.server.DispatcherType;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.EnumSet;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;


/**
 * @author Daniel Wegener (Holisticon AG)
 */
public class TraceeFilterIT {

	private static final int JETTY_PORT = 4204;

	private Server server;
	private FilterHolder traceeFilterHolder;
	private static final String ENDPOINT_URL = "http://localhost:4204/";

	@Before
	public void startJetty() throws Exception {
		server = new Server(JETTY_PORT);
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SECURITY);
		context.setContextPath("/");
		context.addServlet(SillyServlet.class, "/*");
		traceeFilterHolder = context.addFilter(TraceeFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));

		server.setHandler(context);
		server.start();

	}

	@After
	public void stopJetty() throws Exception {
		if (server != null)
			server.stop();
	}

	@Test
	public void testCompleteRoundtrip() throws Exception {
		final Header traceeResponseHeader = get("{ \"inClient\":\"yes\" }").getFirstHeader(TraceeConstants.HTTP_HEADER_NAME);

		assertThat(traceeResponseHeader, notNullValue());
		assertThat(traceeResponseHeader.getValue(), containsString("\"inServlet\":\"yes\""));
		assertThat(traceeResponseHeader.getValue(), containsString("\"inClient\":\"yes\""));
		assertThat(traceeResponseHeader.getValue(), containsString("\""+TraceeConstants.REQUEST_ID_KEY+"\":\""));
	}

	public static final class SillyServlet extends HttpServlet {
		@Override
		protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			Assert.assertThat(req.getHeader(TraceeConstants.HTTP_HEADER_NAME), Matchers.containsString("\"inClient\":\"yes\""));
			Tracee.getBackend().put("inServlet", "yes");
		}
	}

	private HttpResponse get(String traceeHeader) throws IOException {
		final HttpClient client = new DefaultHttpClient();
		final HttpGet httpGet = new HttpGet(ENDPOINT_URL);
		if (traceeHeader != null) {
			httpGet.setHeader(TraceeConstants.HTTP_HEADER_NAME, traceeHeader);
		}
		return client.execute(httpGet);
	}

}
