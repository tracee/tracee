package io.tracee.cxf;

import io.tracee.Tracee;
import io.tracee.TraceeConstants;
import io.tracee.cxf.client.TraceeCxfFeature;
import io.tracee.cxf.testSoapService.HelloWorldTestService;
import org.apache.cxf.bus.CXFBusFactory;
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

public class CxfClientToCxfServerIT extends AbstractConnectionITHelper {

	private HelloWorldTestService helloWorldPort;

	@Before
	public void setup() {
		JaxWsServerFactoryBean jaxWsServer = createJaxWsServer();
		jaxWsServer.getFeatures().add(new LoggingFeature());
		jaxWsServer.getFeatures().add(new TraceeCxfFeature());
		jaxWsServer.getFeatures().add(new ResetBackendFeature());
		server = jaxWsServer.create();

		final ClientProxyFactoryBean factoryBean = new ClientProxyFactoryBean();
		factoryBean.getFeatures().add(new LoggingFeature());
		factoryBean.getFeatures().add(new TraceeCxfFeature());
		factoryBean.getFeatures().add(new ResetBackendFeature());
		factoryBean.setServiceClass(HelloWorldTestService.class);
		factoryBean.setBus(CXFBusFactory.getDefaultBus());
		factoryBean.setAddress(endpointAddress);
		helloWorldPort = (HelloWorldTestService) factoryBean.create();
	}

	@Test
	public void transportTraceeVariablesFromClientToBackend() {
		Tracee.getBackend().put(TraceeConstants.REQUEST_ID_KEY, "123");
		final String answer = helloWorldPort.sayHelloWorld("Michail");
		assertThat(answer, allOf(containsString("Michail"), endsWith("requestId was 123")));
	}

	@Test
	public void transportTraceeVariablesFromBackendToTheClient() {
		helloWorldPort.sayHelloWorld("Michail");
		assertThat(Tracee.getBackend().get(HelloWorldTestService.TEST_KEY), is("accepted"));
	}
}
