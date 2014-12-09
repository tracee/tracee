package io.tracee.cxf.client;

import io.tracee.Tracee;
import io.tracee.TraceeConstants;
import io.tracee.cxf.interceptor.TraceeOutInterceptor;
import io.tracee.cxf.test.HelloWorldPortType;
import io.tracee.cxf.test.HelloWorldService;
import org.apache.cxf.Bus;
import org.apache.cxf.bus.CXFBusFactory;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.feature.AbstractFeature;
import org.apache.cxf.feature.LoggingFeature;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.interceptor.InterceptorProvider;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class WebserviceClientTest {

	private HelloWorldPortType helloWorldPort;

	private boolean backendCleared = false;

	@Before
	public void setup() {
		final Bus bus = CXFBusFactory.getDefaultBus();

		JaxWsServerFactoryBean jaxWsServer = createJaxWsServer(new HelloWorldTestService(), bus);
		jaxWsServer.getFeatures().add(new LoggingFeature());
		jaxWsServer.getFeatures().add(new TraceeCxfFeature());
		jaxWsServer.create();

		helloWorldPort = new HelloWorldService().getHelloWorldPort();

		Client client = ClientProxy.getClient(helloWorldPort);
		client.getOutInterceptors().add(new LoggingOutInterceptor());
		client.getOutInterceptors().add(new TraceeOutInterceptor());
		client.getOutInterceptors().add(new ResetBackendInterceptor());
	}

	@After
	public void traceeBackendShouldBeClearedDuringTest() {
		assertThat(backendCleared, is(true));
	}

	@Test
	@Ignore
	public void transportTraceeVariablesFromCxfToJaxWsBackend() {
		Tracee.getBackend().put(TraceeConstants.REQUEST_ID_KEY, "123");
		final String answer = helloWorldPort.sayHelloWorld("Michail");
		assertThat(answer, allOf(containsString("Michail"), endsWith("requestId was 123")));
	}

	public JaxWsServerFactoryBean createJaxWsServer(HelloWorldTestService helloWorldBean, Bus bus) {
		JaxWsServerFactoryBean serverFactoryBean = new JaxWsServerFactoryBean();
		serverFactoryBean.setServiceClass(HelloWorldTestService.class);
		serverFactoryBean.setAddress("local://localPath");
		serverFactoryBean.setServiceBean(helloWorldBean);
		serverFactoryBean.setBus(bus);
		return serverFactoryBean;
	}

	class ResetBackendInterceptor extends AbstractPhaseInterceptor<Message> {

		public ResetBackendInterceptor() {
			super(Phase.POST_LOGICAL);
		}

		@Override
		public void handleMessage(Message message) throws Fault {
			Tracee.getBackend().clear();
			backendCleared = true;
		}
	}
}
