package io.tracee.binding.cxf;

import io.tracee.testhelper.PortUtil;
import io.tracee.testhelper.SimpleTraceeBackend;
import io.tracee.binding.cxf.testSoapService.HelloWorldTestServiceImpl;
import org.apache.cxf.bus.CXFBusFactory;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.junit.After;

public abstract class AbstractConnectionITHelper {

	protected final SimpleTraceeBackend serverBackend = SimpleTraceeBackend.createNonLoggingAllPermittingBackend();
	protected final SimpleTraceeBackend clientBackend = SimpleTraceeBackend.createNonLoggingAllPermittingBackend();
	protected Server server;
	protected String endpointAddress;

	public AbstractConnectionITHelper() {
		endpointAddress = "http://localhost:" + PortUtil.randomTestPort() + "/cxfitest/";
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
}
