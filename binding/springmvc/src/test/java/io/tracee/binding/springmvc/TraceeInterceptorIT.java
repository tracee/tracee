package io.tracee.binding.springmvc;

import io.tracee.Tracee;
import io.tracee.TraceeConstants;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.InetSocketAddress;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class TraceeInterceptorIT {

	private Server server;
	private String ENDPOINT_URL;

	@Before
	public void startJetty() throws Exception {
		Tracee.getBackend().clear();

		server = new Server(new InetSocketAddress("127.0.0.1", 0));
		ServletContextHandler context = new ServletContextHandler(null, "/", ServletContextHandler.NO_SECURITY);
		final DispatcherServlet dispatcherServlet = new DispatcherServlet();
		dispatcherServlet.setContextConfigLocation("classpath:/spring-itest-configuration.xml");
		context.addServlet(new ServletHolder(dispatcherServlet), "/");
		server.setHandler(context);
		server.start();
		ENDPOINT_URL = "http://" + server.getConnectors()[0].getName() + "/";
	}

	@After
	public void stopJetty() throws Exception {
		if (server != null) {
			server.stop();
		}
		Tracee.getBackend().clear();
	}

	@Test
	public void shouldDelegateContextToServerAndBack() throws IOException {
		final Header traceeResponseHeader = get("addToTpic", "in+Client=yes").getFirstHeader(TraceeConstants.TPIC_HEADER);

		assertThat(traceeResponseHeader, notNullValue());
		assertThat(traceeResponseHeader.getValue(), containsString("inInterceptor=y+e+s"));
		assertThat(traceeResponseHeader.getValue(), containsString("in+Client=yes"));
	}

	@Test
	public void shouldDelegateContextToServerAndReceiveItInCaseOfException() throws IOException {
		final Header traceeResponseHeader = get("throwException", "in+Client=yes").getFirstHeader(TraceeConstants.TPIC_HEADER);

		assertThat(traceeResponseHeader, notNullValue());
		assertThat(traceeResponseHeader.getValue(), containsString("inInterceptor=y+e+s"));
		assertThat(traceeResponseHeader.getValue(), containsString("in+Client=yes"));
	}

	private HttpResponse get(String remoteRessource, String traceeHeaderValue) throws IOException {
		final HttpClient client = new DefaultHttpClient();
		final HttpGet httpGet = new HttpGet(ENDPOINT_URL + remoteRessource);
		if (traceeHeaderValue != null) {
			httpGet.setHeader(TraceeConstants.TPIC_HEADER, traceeHeaderValue);
			httpGet.setHeader("Accept", MediaType.APPLICATION_JSON_VALUE);
		}
		return client.execute(httpGet);
	}

	@Controller
	public static class SillyController {

		@RequestMapping("/addToTpic")
		@ResponseStatus(HttpStatus.NO_CONTENT)
		public void handleGet(HttpServletRequest request) {
			if (request.getHeader(TraceeConstants.TPIC_HEADER) == null) {
				throw new AssertionError("No expected Header " + TraceeConstants.TPIC_HEADER + " in request set");
			}
			Tracee.getBackend().put("inInterceptor", "y e s");
		}

		@RequestMapping("/throwException")
		@ResponseStatus(HttpStatus.BAD_REQUEST)
		public void throwException(HttpServletRequest request) {
			handleGet(request);
			throw new RuntimeException("Mööp!");
		}
	}
}
