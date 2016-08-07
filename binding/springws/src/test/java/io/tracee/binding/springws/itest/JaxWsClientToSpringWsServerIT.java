package io.tracee.binding.springws.itest;

import io.tracee.Tracee;
import io.tracee.TraceeConstants;
import io.tracee.binding.jaxws.TraceeClientHandlerResolver;
import io.tracee.binding.springws.itest.testservicegen.CurrentTraceeContext;
import io.tracee.binding.springws.itest.testservicegen.JaxwsTestserviceEndpointService;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hamcrest.MatcherAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ws.transport.http.MessageDispatcherServlet;

import javax.xml.ws.BindingProvider;
import java.net.InetSocketAddress;

import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;

public class JaxWsClientToSpringWsServerIT {

	private Server server;
	private CurrentTraceeContext testservice;

	@Before
	public void before() throws Exception {
		// Start jetty to provide spring webservice
		server = new Server(new InetSocketAddress("127.0.0.1", 0));
		ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.NO_SECURITY);
		contextHandler.setContextPath("/");
		final MessageDispatcherServlet messageDispatcherServlet = new MessageDispatcherServlet();
		messageDispatcherServlet.setContextConfigLocation("classpath:springWsServer.xml");
		messageDispatcherServlet.setTransformWsdlLocations(true);
		contextHandler.addServlet(new ServletHolder(messageDispatcherServlet), "/*");
		server.setHandler(contextHandler);
		server.start();
		final String serverUrl = "http://" + server.getConnectors()[0].getName() + "/springws/TestserviceEndpoint";

		// Init jaxws client with TraceeClientHandler
		final JaxwsTestserviceEndpointService endpointService = new JaxwsTestserviceEndpointService();
		endpointService.setHandlerResolver(new TraceeClientHandlerResolver());
		testservice = endpointService.getCurrentTraceeContextPort();
		((BindingProvider) testservice).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, serverUrl);
	}

	@After
	public void after() throws Exception {
		server.stop();
	}

	@Test
	public void callRemoteMethodWithoutInvocationId() throws Exception {
		Tracee.getBackend().remove(TraceeConstants.INVOCATION_ID_KEY);
		final String remoteInvocationId = testservice.currentTraceeContext();
		MatcherAssert.assertThat(remoteInvocationId, not(isEmptyOrNullString()));
		MatcherAssert.assertThat(Tracee.getBackend().get(TraceeConstants.INVOCATION_ID_KEY), is(remoteInvocationId));
	}

	@Test
	public void callRemoteMethodWithInvocationId() throws Exception {
		final String testInvocationId = "tolleId";
		Tracee.getBackend().put(TraceeConstants.INVOCATION_ID_KEY, testInvocationId);
		final String remoteInvocationId = testservice.currentTraceeContext();
		MatcherAssert.assertThat(remoteInvocationId, is(testInvocationId));
		MatcherAssert.assertThat(Tracee.getBackend().get(TraceeConstants.INVOCATION_ID_KEY), is(testInvocationId));
	}

	@Test
	public void callShouldReturnValuesSetInRemoteEndpoint() throws Exception {
		Tracee.getBackend().remove("testId");
		testservice.currentTraceeContext();
		MatcherAssert.assertThat(Tracee.getBackend().copyToMap(), hasEntry("testId", "TestValueFromRemoteEndpoint"));
	}
}
