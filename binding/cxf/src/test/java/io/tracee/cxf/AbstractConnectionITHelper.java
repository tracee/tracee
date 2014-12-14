package io.tracee.cxf;

import io.tracee.Tracee;
import io.tracee.cxf.testSoapService.HelloWorldTestServiceImpl;
import org.apache.cxf.Bus;
import org.apache.cxf.bus.CXFBusFactory;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.feature.AbstractFeature;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.interceptor.InterceptorProvider;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.junit.After;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public abstract class AbstractConnectionITHelper {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractConnectionITHelper.class);

	protected Server server;

	protected String endpointAddress;

	public AbstractConnectionITHelper() {
		endpointAddress = "http://localhost:" + randomJettyPort() + "/cxfitest/";
	}

	@After
	public void after() {
		server.destroy();
	}


	protected JaxWsServerFactoryBean createJaxWsServer() {
		JaxWsServerFactoryBean serverFactoryBean = new JaxWsServerFactoryBean();
		serverFactoryBean.setServiceClass(HelloWorldTestServiceImpl.class);
		serverFactoryBean.setAddress(endpointAddress);
		serverFactoryBean.setServiceBean(new HelloWorldTestServiceImpl());
		serverFactoryBean.setBus(CXFBusFactory.getDefaultBus());
		return serverFactoryBean;
	}

	/**
	 * Simple Feature to reset the TraceeBackend to keep sure, that the context is really transferred between HTTP.
	 * Otherwise it could be still in place (same thread) or copied (new generated thread).
	 */
	class ResetBackendFeature extends AbstractFeature {
		@Override
		protected void initializeProvider(InterceptorProvider provider, Bus bus)  {
			final ResetBackendInterceptor interceptor = new ResetBackendInterceptor();
			provider.getOutInterceptors().add(interceptor);
			provider.getInInterceptors().add(interceptor);
		}
	}

	class ResetBackendInterceptor extends AbstractPhaseInterceptor<Message> {
		public ResetBackendInterceptor() {
			super(Phase.SEND_ENDING);
		}

		@Override
		public void handleMessage(Message message) throws Fault {
			LOGGER.info("CLEARING BACKEND!!");
			Tracee.getBackend().clear();
		}
	}

	/*
	 * Random port between 10001 and 65535
	 */
	private int randomJettyPort() {
		int port;
		//noinspection StatementWithEmptyBody
		while ((port = new Random().nextInt(65536)) < 10000) {
		}
		return port;
	}
}
