package io.tracee.cxf.client;

import io.tracee.Tracee;
import io.tracee.cxf.test.HelloWorldPortType;
import io.tracee.cxf.test.HelloWorldService;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.feature.LoggingFeature;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = WebserviceClientTest.ContextConfig.class)
public class WebserviceClientTest {

	@Autowired
	@Qualifier("helloWorldPort")
	private HelloWorldPortType helloWorldPort;

	@Autowired
	@Qualifier("helloWorldTestBean")
	private HelloWorldPortType helloWorldService;

	@Test
	public void testContext() {
		assertNotNull(helloWorldPort);
		assertNotNull(helloWorldService);
	}

	@Test
	public void testCall() {
		Tracee.getBackend().put("sss", "123");
		final String answer = helloWorldPort.sayHelloWorld("Michail");
		assertNotNull(answer);
		assertTrue(answer.endsWith("Michail"));
	}

	@Configuration
	public static class ContextConfig {
		@Bean(destroyMethod = "shutdown")
		public SpringBus cxf() {
			final SpringBus bus = new SpringBus();
			bus.getFeatures().add(new LoggingFeature());
			bus.getFeatures().add(new TraceeCxfFeature());
			return bus;
		}

		@Bean(initMethod = "create", destroyMethod = "destroy")
		@DependsOn("cxf")
		public JaxWsServerFactoryBean helloWorldServer(HelloWorldTestService helloWorldBean) {
			JaxWsServerFactoryBean serverFactoryBean = new JaxWsServerFactoryBean();
			serverFactoryBean.setServiceClass(HelloWorldTestService.class);
			serverFactoryBean.setAddress("local://localPath");
			serverFactoryBean.setServiceBean(helloWorldBean);

//            serverFactoryBean.getFeatures().add(new TraceeCxfFeature());
			return serverFactoryBean;
		}

		@Bean
		public HelloWorldTestService helloWorldTestBean() {
			return new HelloWorldTestService();
		}

		@Bean
		@DependsOn("cxf")
		public HelloWorldPortType helloWorldPort() {
//            return new HelloWorldService(new TraceeCxfFeature()).getHelloWorldPort();
			return new HelloWorldService().getHelloWorldPort();
		}

		@Bean
		@DependsOn("cxf")
		public HelloWorldPortType helloWorldPort2() {
			JaxWsProxyFactoryBean factoryBean = new JaxWsProxyFactoryBean();

			factoryBean.setServiceClass(HelloWorldPortType.class);
			factoryBean.setAddress("local://localPath");

//            factoryBean.getFeatures().add(new TraceeCxfFeature());

			return (HelloWorldPortType) factoryBean.create();
		}
	}
}
