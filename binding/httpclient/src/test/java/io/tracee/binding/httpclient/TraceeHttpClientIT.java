package io.tracee.binding.httpclient;

import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import io.tracee.configuration.TraceeFilterConfiguration;
import io.tracee.testhelper.PermitAllTraceeFilterConfiguration;
import io.tracee.testhelper.SimpleTraceeBackend;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.InetSocketAddress;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class TraceeHttpClientIT {

	private Server server;
	private String serverEndpoint;

	private final TraceeBackend backend = new SimpleTraceeBackend();
	private final TraceeFilterConfiguration filterConfiguration = PermitAllTraceeFilterConfiguration.INSTANCE;

	@Test
	public void testWritesToServerAndParsesResponse() throws IOException {
		final HttpClient unit = TraceeHttpClientDecorator.wrap(new HttpClient(), backend, filterConfiguration);

		GetMethod getMethod = new GetMethod(serverEndpoint);
		backend.put("beforeRequest", "yip");
		unit.executeMethod(getMethod);

		assertThat(getMethod.getStatusCode(), equalTo(HttpServletResponse.SC_NO_CONTENT));
		assertThat(backend.get("responseFromServer"), equalTo("yesSir"));
	}

	@Before
	public void startJetty() throws Exception {
		server = new Server(new InetSocketAddress("127.0.0.1", 0));
		server.setHandler(requestHandler);
		server.start();
		serverEndpoint = "http://"+server.getConnectors()[0].getName();
	}

	private final Handler requestHandler = new AbstractHandler() {
		@Override
		public void handle(String s, Request request, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException, ServletException {
			final String incomingTraceeHeader = request.getHeader(TraceeConstants.TPIC_HEADER);

			assertThat(incomingTraceeHeader, equalTo("beforeRequest=yip"));

			httpServletResponse.setHeader(TraceeConstants.TPIC_HEADER, "responseFromServer=yesSir");
			httpServletResponse.setStatus(HttpServletResponse.SC_NO_CONTENT);
			request.setHandled(true);
		}
	};

	@After
	public void stopJetty() throws Exception {
		if (server != null) {
			server.stop();
			server.join();
		}
	}
}
