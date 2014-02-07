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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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

	}

	@After
	public void stopJetty() throws Exception {
		if (server != null)
			server.stop();
	}

	@Test
	public void testRespondWithTraceeContext() throws Exception {
		traceeFilterHolder.setInitParameter(TraceeFilter.RESPOND_WITH_CONTEXT_KEY, "true");
		server.start();

		final Header traceeResponseHeader = get(ENDPOINT_URL).getFirstHeader(TraceeConstants.HTTP_HEADER_NAME);

		assertThat(traceeResponseHeader, notNullValue());
		assertThat(traceeResponseHeader.getValue(), containsString("\"inServlet\":\"yes\""));
	}

	public static final class SillyServlet extends HttpServlet {
		@Override
		protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			Tracee.getBackend().put("inServlet", "yes");
		}
	}

	private HttpResponse get(String url) throws IOException {
		final HttpClient client = new DefaultHttpClient();
		final HttpGet httpGet = new HttpGet(ENDPOINT_URL);
		return client.execute(httpGet);
	}

}
