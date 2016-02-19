package io.tracee.binding.servlet;

import io.tracee.Tracee;
import io.tracee.TraceeConstants;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.DispatcherType;
import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;

public class TraceeFilterIT {

	private Server server;
	private String serverUrl;

	@Before
	public void startJetty() throws Exception {
		server = new Server(new InetSocketAddress("127.0.0.1", 0));

		final WebAppContext sillyWebApp = new WebAppContext("sillyWebApp", "/");
		final AnnotationConfiguration annotationConfiguration = new AnnotationConfiguration();
		annotationConfiguration.createServletContainerInitializerAnnotationHandlers(sillyWebApp,
				Collections.<ServletContainerInitializer>singletonList(new TestConfig()));
		sillyWebApp.setConfigurations(new Configuration[] {annotationConfiguration});
		server.setHandler(sillyWebApp);
		server.start();
		serverUrl = "http://" + server.getConnectors()[0].getName() + "/";
	}

	public static class TestConfig implements ServletContainerInitializer {

		@Override
		public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
			ctx.addFilter("traceeFilter", TraceeFilter.class).addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, "/*");
			ctx.addListener(TraceeServletRequestListener.class);
			ctx.addListener(TraceeSessionListener.class);

			final ServletRegistration.Dynamic sillyServlet = ctx.addServlet("sillyServlet", SillyServlet.class);
			sillyServlet.addMapping("/sillyServlet", "/sillyFlushingServlet");
		}
	}

	public static final class SillyServlet extends HttpServlet {
		@Override
		protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			assertThat(req.getHeader(TraceeConstants.TPIC_HEADER), Matchers.containsString("inClient=yes"));
			Tracee.getBackend().put("inServlet", "yes");
		}
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
		final Header traceeResponseHeader = get("sillyServlet", "inClient=yes").getFirstHeader(TraceeConstants.TPIC_HEADER);

		assertThat(traceeResponseHeader, notNullValue());
		assertThat(traceeResponseHeader.getValue(), containsString("inServlet=yes"));
		assertThat(traceeResponseHeader.getValue(), containsString("inClient=yes"));
		assertThat(traceeResponseHeader.getValue(), containsString(TraceeConstants.INVOCATION_ID_KEY + "="));
	}

	@Test
	public void testRoundtripWithPrematurlyFlushedBuffer() throws Exception {
		final Header traceeResponseHeader = get("sillyFlushingServlet", "inClient=yes").getFirstHeader(TraceeConstants.TPIC_HEADER);

		assertThat(traceeResponseHeader, notNullValue());
		assertThat(traceeResponseHeader.getValue(), containsString("inClient=yes"));
		assertThat(traceeResponseHeader.getValue(), containsString(TraceeConstants.INVOCATION_ID_KEY + "="));
	}

	public static final class SillyFlushingServlet extends HttpServlet {
		@Override
		protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			assertThat(req.getHeader(TraceeConstants.TPIC_HEADER), Matchers.containsString("inClient=yes"));
			Tracee.getBackend().put("inServlet", "yes");
			resp.getWriter().append("Hello World");
			resp.flushBuffer();
		}
	}

	private HttpResponse get(String servlet, String traceeHeader) throws IOException {
		final HttpClient client = new DefaultHttpClient();
		final HttpGet httpGet = new HttpGet(serverUrl + servlet);
		if (traceeHeader != null) {
			httpGet.setHeader(TraceeConstants.TPIC_HEADER, traceeHeader);
		}
		return client.execute(httpGet);
	}

}
