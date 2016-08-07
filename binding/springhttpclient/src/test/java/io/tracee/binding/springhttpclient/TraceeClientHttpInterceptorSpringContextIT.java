package io.tracee.binding.springhttpclient;

import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import io.tracee.binding.springhttpclient.config.TraceeSpringWebConfiguration;
import io.tracee.configuration.TraceeFilterConfiguration;
import io.tracee.testhelper.PermitAllTraceeFilterConfiguration;
import io.tracee.testhelper.SimpleTraceeBackend;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.InetSocketAddress;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * Spring configuration test.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TraceeClientHttpInterceptorSpringContextIT.Config.class)
public class TraceeClientHttpInterceptorSpringContextIT {


	@Configuration
	@Import(TraceeSpringWebConfiguration.class)
	static class Config {
		@Bean
		public TraceeBackend traceeBackend() {
			return new SimpleTraceeBackend();
		}
		@Bean
		public TraceeFilterConfiguration filterConfiguration() {
			return PermitAllTraceeFilterConfiguration.INSTANCE;
		}
		@Bean
		public RestTemplate restTemplate() {
			return new RestTemplate();
		}
	}

	@Rule
	public ErrorCollector collector = new ErrorCollector();
	private final Handler requestHandler = new AbstractHandler() {
		@Override
		public void handle(String s, Request request, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException,
				ServletException {
			final String incomingTraceeHeader = request.getHeader(TraceeConstants.TPIC_HEADER);
			collector.checkThat(incomingTraceeHeader, equalTo("before+Request=yip"));
			httpServletResponse.setHeader(TraceeConstants.TPIC_HEADER, "response+From+Server=yesSir");
			httpServletResponse.setStatus(HttpServletResponse.SC_NO_CONTENT);
			request.setHandled(true);
		}
	};
	private Server server;
	private String serverEndpoint;
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private TraceeBackend backend;

	@Test
	public void testWritesToServerAndParsesResponse() throws IOException {
		backend.put("before Request", "yip");
		restTemplate.getForObject(serverEndpoint, EmptyEntity.class);
		assertThat(backend.get("response From Server"), equalTo("yesSir"));
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
		backend.clear();
	}

	private static final class EmptyEntity {
	}


}
