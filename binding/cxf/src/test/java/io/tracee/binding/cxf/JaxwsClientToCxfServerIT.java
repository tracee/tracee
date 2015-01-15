package io.tracee.binding.cxf;

import io.tracee.TraceeConstants;
import io.tracee.binding.cxf.testSoapService.HelloWorldTestService;
import io.tracee.binding.jaxws.client.TraceeClientHandler;
import org.apache.cxf.Bus;
import org.apache.cxf.bus.CXFBusFactory;
import org.apache.cxf.feature.LoggingFeature;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class JaxwsClientToCxfServerIT extends AbstractConnectionITHelper {

	private HelloWorldTestService helloWorldPort;

	@Before
	public void setup() {
		final Bus bus = CXFBusFactory.getThreadDefaultBus();

		JaxWsServerFactoryBean jaxWsServer = createJaxWsServer();
		jaxWsServer.getFeatures().add(new LoggingFeature());
		jaxWsServer.getFeatures().add(new TraceeCxfFeature(serverBackend));
		server = jaxWsServer.create();

		JaxWsProxyFactoryBean factoryBean = new JaxWsProxyFactoryBean();

		factoryBean.setServiceClass(HelloWorldTestService.class);
		factoryBean.setAddress(endpointAddress);
		factoryBean.getHandlers().add(new TraceeClientHandler(clientBackend));
		factoryBean.setBus(bus);

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
