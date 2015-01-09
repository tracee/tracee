package io.tracee.binding.cxf;

import io.tracee.SimpleTraceeBackend;
import io.tracee.binding.cxf.testSoapService.HelloWorldTestServiceImpl;
import org.apache.cxf.bus.CXFBusFactory;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.junit.After;

import java.util.Random;

public abstract class AbstractConnectionITHelper {


	protected Server server;

	protected final SimpleTraceeBackend serverBackend = SimpleTraceeBackend.createNonLoggingAllPermittingBackend();
	protected final SimpleTraceeBackend clientBackend = SimpleTraceeBackend.createNonLoggingAllPermittingBackend();

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
		serverFactoryBean.setServiceBean(new HelloWorldTestServiceImpl(serverBackend));
		serverFactoryBean.setBus(CXFBusFactory.getDefaultBus());
		return serverFactoryBean;
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
