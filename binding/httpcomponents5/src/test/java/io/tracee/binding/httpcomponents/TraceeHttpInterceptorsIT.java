package io.tracee.binding.httpcomponents;

import io.tracee.Tracee;
import io.tracee.TraceeConstants;
import org.apache.hc.client5.http.impl.sync.CloseableHttpClient;
import org.apache.hc.client5.http.impl.sync.HttpClientBuilder;
import org.apache.hc.client5.http.methods.HttpGet;
import org.apache.hc.core5.http.HttpResponse;
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

public class TraceeHttpInterceptorsIT {

	private final Handler requestHandler = new AbstractHandler() {
		@Override
		public void handle(String s, Request request, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException, ServletException {
			final String incomingTraceeHeader = request.getHeader(TraceeConstants.TPIC_HEADER);

			assertThat(incomingTraceeHeader, equalTo("before+Request=yip"));

			httpServletResponse.setHeader(TraceeConstants.TPIC_HEADER, "responseFromServer=yes+Sir");
			httpServletResponse.setStatus(HttpServletResponse.SC_NO_CONTENT);
			request.setHandled(true);
		}
	};
	private Server server;
	private String serverEndpoint;

	@Test
	public void testWritesToServerAndParsesResponse() throws IOException {

		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		httpClientBuilder.addInterceptorLast(new TraceeHttpRequestInterceptor());
		httpClientBuilder.addInterceptorFirst(new TraceeHttpResponseInterceptor());
		CloseableHttpClient httpClient = httpClientBuilder.build();

		HttpGet getMethod = new HttpGet(serverEndpoint);
		Tracee.getBackend().put("before Request", "yip");
		final HttpResponse response = httpClient.execute(getMethod);

		assertThat(response.getStatusLine().getStatusCode(), equalTo(HttpServletResponse.SC_NO_CONTENT));
		assertThat(Tracee.getBackend().get("responseFromServer"), equalTo("yes Sir"));
	}

	@Before
	public void startJetty() throws Exception {
		server = new Server(new InetSocketAddress("127.0.0.1", 0));
		server.setHandler(requestHandler);
		server.start();
		serverEndpoint = "http://" + server.getConnectors()[0].getName();
	}

	@After
	public void stopJetty() throws Exception {
		if (server != null) {
			server.stop();
			server.join();
		}
	}
}
