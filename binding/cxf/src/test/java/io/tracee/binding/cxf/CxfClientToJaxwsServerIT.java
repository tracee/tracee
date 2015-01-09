package io.tracee.binding.cxf;

import io.tracee.*;
import io.tracee.binding.cxf.client.TraceeCxfFeature;
import io.tracee.binding.cxf.testSoapService.HelloWorldTestService;
import io.tracee.binding.jaxws.client.TraceeClientHandler;
import org.apache.cxf.feature.LoggingFeature;
import org.apache.cxf.frontend.ClientProxyFactoryBean;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class CxfClientToJaxwsServerIT extends AbstractConnectionITHelper {

	private HelloWorldTestService helloWorldPort;


	@Before
	public void setup() {
		JaxWsServerFactoryBean jaxWsServer = createJaxWsServer();
		jaxWsServer.getHandlers().add(new TraceeClientHandler(serverBackend));
		server = jaxWsServer.create();

		final ClientProxyFactoryBean factoryBean = new ClientProxyFactoryBean();
		factoryBean.getFeatures().add(new LoggingFeature());
		factoryBean.getFeatures().add(new TraceeCxfFeature(clientBackend));
		factoryBean.setServiceClass(HelloWorldTestService.class);
		factoryBean.setAddress(endpointAddress);
		helloWorldPort = (HelloWorldTestService) factoryBean.create();
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
