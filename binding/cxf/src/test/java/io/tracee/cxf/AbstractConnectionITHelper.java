package io.tracee.cxf;

import io.tracee.cxf.testSoapService.HelloWorldTestServiceImpl;
import org.apache.cxf.Bus;
import org.apache.cxf.bus.CXFBusFactory;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.junit.After;

public abstract class AbstractConnectionITHelper {

	protected final Bus bus = CXFBusFactory.getDefaultBus();

	protected Server server;

	@After
	public void after() {
		server.destroy();
	}


	protected JaxWsServerFactoryBean createJaxWsServer(Bus bus) {
		JaxWsServerFactoryBean serverFactoryBean = new JaxWsServerFactoryBean();
		serverFactoryBean.setServiceClass(HelloWorldTestServiceImpl.class);
		serverFactoryBean.setAddress("local://localPath");
		serverFactoryBean.setServiceBean(new HelloWorldTestServiceImpl());
		serverFactoryBean.setBus(bus);
		return serverFactoryBean;
	}
}
