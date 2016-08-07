package io.tracee.binding.cxf;

import io.tracee.TraceeConstants;
import io.tracee.binding.cxf.testSoapService.HelloWorldTestService;
import io.tracee.binding.jaxws.TraceeClientHandler;
import io.tracee.binding.jaxws.TraceeServerHandler;
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
		jaxWsServer.getHandlers().add(new TraceeServerHandler(serverBackend, filterConfiguration, new SoapHeaderTransport()));
		server = jaxWsServer.create();

		JaxWsProxyFactoryBean factoryBean = new JaxWsProxyFactoryBean();

		factoryBean.setServiceClass(HelloWorldTestService.class);
		factoryBean.setAddress(endpointAddress);
		factoryBean.getHandlers().add(new TraceeClientHandler(clientBackend, filterConfiguration));
		factoryBean.setBus(CXFBusFactory.getDefaultBus());

		helloWorldPort = factoryBean.create(HelloWorldTestService.class);
	}

	@Test
	public void transportTraceeVariablesFromClientToBackend() {
		clientBackend.put(TraceeConstants.INVOCATION_ID_KEY, "123");
		final String answer = helloWorldPort.sayHelloWorld("Michail");
		assertThat(answer, allOf(containsString("Michail"), endsWith("invocationId was 123")));
	}

	@Test
	public void transportTraceeVariablesFromBackendToTheClient() {
		helloWorldPort.sayHelloWorld("Michail");
		assertThat(clientBackend.get(HelloWorldTestService.TEST_KEY), is("accepted"));
	}
}
