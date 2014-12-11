package io.tracee.cxf;

import io.tracee.Tracee;
import io.tracee.TraceeConstants;
import io.tracee.cxf.client.TraceeCxfFeature;
import io.tracee.cxf.interceptor.TraceeOutInterceptor;
import io.tracee.cxf.testSoapService.HelloWorldTestService;
import io.tracee.cxf.testSoapService.HelloWorldTestServiceImpl;
import org.apache.cxf.Bus;
import org.apache.cxf.bus.CXFBusFactory;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.feature.LoggingFeature;
import org.apache.cxf.frontend.ClientProxyFactoryBean;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.xml.namespace.QName;
import javax.xml.ws.soap.SOAPBinding;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class CxfClientToCxfServerIT extends AbstractConnectionITHelper {

	private HelloWorldTestService helloWorldPort;

	@Before
	public void setup() {
		JaxWsServerFactoryBean jaxWsServer = createJaxWsServer(bus);
		jaxWsServer.getFeatures().add(new LoggingFeature());
		jaxWsServer.getFeatures().add(new TraceeCxfFeature());
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

	public JaxWsServerFactoryBean createJaxWsServer(Bus bus) {
		JaxWsServerFactoryBean serverFactoryBean = new JaxWsServerFactoryBean();
		serverFactoryBean.setServiceClass(HelloWorldTestService.class);
		serverFactoryBean.setBindingId(SOAPBinding.SOAP11HTTP_BINDING);
		serverFactoryBean.setServiceBean(HelloWorldTestServiceImpl.class);
		serverFactoryBean.setAddress("local://localPath");
		serverFactoryBean.setServiceBean(new HelloWorldTestServiceImpl());
		serverFactoryBean.setBus(bus);
		return serverFactoryBean;
	}
}
