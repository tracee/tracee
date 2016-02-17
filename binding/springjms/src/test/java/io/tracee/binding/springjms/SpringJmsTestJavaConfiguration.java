package io.tracee.binding.springjms;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.TransportConnector;
import org.apache.activemq.xbean.XBeanBrokerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConverter;

import javax.jms.ConnectionFactory;
import java.net.URI;

import static java.util.Collections.singletonList;

@Configuration
public class SpringJmsTestJavaConfiguration {

	@Bean
	public MessageConverter traceeMessageConverter() {
		return new TraceeMessageConverter();
	}

	@Bean
	public JmsTemplate jmsTemplate() {
		final JmsTemplate jmsTemplate = new JmsTemplate();
		jmsTemplate.setMessageConverter(traceeMessageConverter());
		jmsTemplate.setConnectionFactory(connectionFactory());
		return jmsTemplate;
	}

	@Bean
	public ConnectionFactory connectionFactory() {
		ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
		activeMQConnectionFactory.setBrokerURL("vm://localhost");
		return activeMQConnectionFactory;
	}

	@Bean
	public BrokerService jmsBroker() throws Exception {
		final XBeanBrokerService brokerService = new XBeanBrokerService();
		brokerService.setUseJmx(false);
		brokerService.setPersistent(false);
		final TransportConnector transportConnector = new TransportConnector();
		transportConnector.setUri(new URI("tcp://localhost:0"));
		brokerService.setTransportConnectors(singletonList(transportConnector));
		return brokerService;
	}
}
