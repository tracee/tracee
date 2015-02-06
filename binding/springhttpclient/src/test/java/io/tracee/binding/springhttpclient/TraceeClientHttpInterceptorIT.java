package io.tracee.binding.springhttpclient;

import io.tracee.Tracee;
import io.tracee.TraceeConstants;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class TraceeClientHttpInterceptorIT {

	private Server server;
	private String serverEndpoint;

	private static final class EmptyEntity {

	}

	@Test
	public void testWritesToServerAndParsesResponse() throws IOException {
		final RestTemplate restTemplate = new RestTemplate();
		restTemplate.setInterceptors(Arrays.<ClientHttpRequestInterceptor>asList(new TraceeClientHttpRequestInterceptor("default")));
		Tracee.getBackend().put("before Request", "yip");
		restTemplate.getForObject(serverEndpoint, EmptyEntity.class);
		assertThat(Tracee.getBackend().get("response From Server"), equalTo("yesSir"));
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
			assertThat(incomingTraceeHeader, equalTo("before+Request=yip"));
			httpServletResponse.setHeader(TraceeConstants.TPIC_HEADER, "response+From+Server=yesSir");
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
