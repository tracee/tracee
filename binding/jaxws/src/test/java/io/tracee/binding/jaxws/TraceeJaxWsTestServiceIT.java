package io.tracee.binding.jaxws;


import io.tracee.Tracee;
import io.tracee.TraceeConstants;
import io.tracee.configuration.TraceeFilterConfiguration;
import io.tracee.testhelper.PermitAllTraceeFilterConfiguration;
import io.tracee.testhelper.SimpleTraceeBackend;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.naming.NamingException;
import javax.xml.namespace.QName;
import javax.xml.ws.Endpoint;
import javax.xml.ws.Service;
import java.net.URL;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasKey;

public class TraceeJaxWsTestServiceIT {

	private static final QName ENDPOINT_QNAME = new QName(TraceeJaxWsEndpoint.Descriptor.TNS, TraceeJaxWsEndpointImpl.Descriptor.SERVICE_NAME);
	private static String ENDPOINT_URL = "http://127.0.0.1:4204/jaxws/TraceeJaxWsEndpointImpl";
	private static Endpoint endpoint;

	private SimpleTraceeBackend clientBackend = new SimpleTraceeBackend();
	private TraceeFilterConfiguration clientFilterConfiguration = PermitAllTraceeFilterConfiguration.INSTANCE;

	@BeforeClass
	public static void setUp() throws Exception {
		endpoint = Endpoint.publish(ENDPOINT_URL, new TraceeJaxWsEndpointImpl());
	}

	@AfterClass
	public static void tearDown() throws NamingException {
		endpoint.stop();
		Tracee.getBackend().clear();
	}

	@Test
	public void transferClientTpicToServerBackend() throws Exception {
		clientBackend.put("inRequest", "yes");

		Service calculatorService = Service.create(new URL(ENDPOINT_URL + "?wsdl"), ENDPOINT_QNAME);
		calculatorService.setHandlerResolver(new TraceeClientHandlerResolver(clientBackend, clientFilterConfiguration));

		final TraceeJaxWsEndpoint remote = calculatorService.getPort(TraceeJaxWsEndpoint.class);
		final List<String> result = remote.getCurrentTraceeContext();

		// checking server context as response.
		assertThat(result, hasItem(TraceeConstants.INVOCATION_ID_KEY));
		assertThat(result, hasItem("inRequest"));
		assertThat(result, hasItem("yes"));

		// checking client context. We should receive the pair "called"->"yes" and the invocationId from the server.
		assertThat(clientBackend.copyToMap(), hasKey(TraceeConstants.INVOCATION_ID_KEY));
		assertThat(clientBackend.copyToMap(), hasEntry("called", "yes"));
	}
}
