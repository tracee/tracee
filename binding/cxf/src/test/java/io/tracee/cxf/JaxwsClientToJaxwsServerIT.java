package io.tracee.cxf;

import io.tracee.TraceeConstants;
import io.tracee.cxf.testSoapService.HelloWorldTestService;
import io.tracee.jaxws.client.TraceeClientHandler;
import io.tracee.jaxws.container.TraceeServerHandler;
import io.tracee.transport.SoapHeaderTransport;
import org.apache.cxf.bus.CXFBusFactory;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class JaxwsClientToJaxwsServerIT extends AbstractConnectionITHelper {

	private HelloWorldTestService helloWorldPort;

	@Before
	public void setup() {

		JaxWsServerFactoryBean jaxWsServer = createJaxWsServer();
		jaxWsServer.getHandlers().add(new TraceeServerHandler(serverBackend, new SoapHeaderTransport()));
		server = jaxWsServer.create();

		JaxWsProxyFactoryBean factoryBean = new JaxWsProxyFactoryBean();

		factoryBean.setServiceClass(HelloWorldTestService.class);
		factoryBean.setAddress(endpointAddress);
		factoryBean.getHandlers().add(new TraceeClientHandler(clientBackend));
		factoryBean.setBus(CXFBusFactory.getDefaultBus());

		helloWorldPort = factoryBean.create(HelloWorldTestService.class);
	}

	@Test
	public void transportTraceeVariablesFromClientToBackend() {
		clientBackend.put(TraceeConstants.REQUEST_ID_KEY, "123");
		final String answer = helloWorldPort.sayHelloWorld("Michail");
		assertThat(answer, allOf(containsString("Michail"), endsWith("requestId was 123")));
	}

	@Test
	public void transportTraceeVariablesFromBackendToTheClient() {
		helloWorldPort.sayHelloWorld("Michail");
		assertThat(clientBackend.get(HelloWorldTestService.TEST_KEY), is("accepted"));
	}
}
