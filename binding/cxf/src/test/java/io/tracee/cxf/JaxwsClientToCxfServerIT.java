package io.tracee.cxf;

import io.tracee.Tracee;
import io.tracee.TraceeConstants;
import io.tracee.cxf.client.TraceeCxfFeature;
import io.tracee.cxf.testSoapService.HelloWorldTestService;
import io.tracee.cxf.testSoapService.HelloWorldTestServiceImpl;
import io.tracee.jaxws.client.TraceeClientHandler;
import org.apache.cxf.Bus;
import org.apache.cxf.bus.CXFBusFactory;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.feature.LoggingFeature;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class JaxwsClientToCxfServerIT extends AbstractConnectionITHelper {

	private HelloWorldTestService helloWorldPort;

	@Before
	public void setup() {
		JaxWsServerFactoryBean jaxWsServer = createJaxWsServer(bus);
		jaxWsServer.getFeatures().add(new LoggingFeature());
		jaxWsServer.getFeatures().add(new TraceeCxfFeature());
		server = jaxWsServer.create();

		JaxWsProxyFactoryBean factoryBean = new JaxWsProxyFactoryBean();

		factoryBean.setServiceClass(HelloWorldTestService.class);
		factoryBean.setAddress("local://localPath");
		factoryBean.getHandlers().add(new TraceeClientHandler());
		factoryBean.setBus(bus);

		helloWorldPort = factoryBean.create(HelloWorldTestService.class);
	}

	@Test
	public void transportTraceeVariablesFromCxfToJaxWsBackend() {
		Tracee.getBackend().put(TraceeConstants.REQUEST_ID_KEY, "123");
		final String answer = helloWorldPort.sayHelloWorld("Michail");
		assertThat(answer, allOf(containsString("Michail"), endsWith("requestId was 123")));
	}
}
