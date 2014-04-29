package de.holisticon.util.tracee.outbound.httpcomponents;

import de.holisticon.util.tracee.Tracee;
import de.holisticon.util.tracee.TraceeConstants;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
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

/**
 * @author Daniel Wegener (Holisticon AG)
 */
public class TraceeHttpInterceptorsIT {

	private Server server;
	private String serverEndpoint = "http://localhost:4204/";


	@Test
	public void testWritesToServerAndParsesResponse() throws IOException {

		DefaultHttpClient httpClient = new DefaultHttpClient();
		httpClient.addRequestInterceptor(new TraceeHttpRequestInterceptor());
		httpClient.addResponseInterceptor(new TraceeHttpResponseInterceptor());

		HttpGet getMethod = new HttpGet(serverEndpoint);
		Tracee.getBackend().put("beforeRequest", "yip");
		final HttpResponse response = httpClient.execute(getMethod);

		assertThat(response.getStatusLine().getStatusCode(), equalTo(HttpServletResponse.SC_NO_CONTENT));
		assertThat(Tracee.getBackend().get("responseFromServer"), equalTo("yesSir"));
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
			final String incomingTraceeHeader = request.getHeader(TraceeConstants.HTTP_HEADER_NAME);

			assertThat(incomingTraceeHeader, equalTo("{\"beforeRequest\":\"yip\"}"));

			httpServletResponse.setHeader(TraceeConstants.HTTP_HEADER_NAME, "{ \"responseFromServer\":\"yesSir\" }");
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
