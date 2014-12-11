package io.tracee.cxf;

import io.tracee.Tracee;
import io.tracee.TraceeConstants;
import io.tracee.cxf.interceptor.TraceeOutInterceptor;
import io.tracee.cxf.testSoapService.HelloWorldTestService;
import io.tracee.jaxws.client.TraceeClientHandler;
import org.apache.cxf.frontend.ClientProxyFactoryBean;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class CxfClientToJaxwsServerIT extends AbstractConnectionITHelper {

	private HelloWorldTestService helloWorldPort;

	@Before
	public void setup() {
		JaxWsServerFactoryBean jaxWsServer = createJaxWsServer(bus);
		jaxWsServer.getHandlers().add(new TraceeClientHandler());
		server = jaxWsServer.create();

		final ClientProxyFactoryBean factoryBean = new ClientProxyFactoryBean();
		factoryBean.getOutInterceptors().add(new LoggingOutInterceptor());
		factoryBean.getOutInterceptors().add(new TraceeOutInterceptor());
		factoryBean.setServiceClass(HelloWorldTestService.class);
		factoryBean.setAddress("local://localPath");
		helloWorldPort = (HelloWorldTestService) factoryBean.create();
	}

	@Test
	public void transportTraceeVariablesFromCxfToJaxWsBackend() {
		Tracee.getBackend().put(TraceeConstants.REQUEST_ID_KEY, "123");
		final String answer = helloWorldPort.sayHelloWorld("Michail");
		assertThat(answer, allOf(containsString("Michail"), endsWith("requestId was 123")));
	}
}
