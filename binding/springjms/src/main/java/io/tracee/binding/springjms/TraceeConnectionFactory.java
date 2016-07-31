package io.tracee.binding.springjms;

import org.springframework.jms.connection.DelegatingConnectionFactory;

import javax.jms.Connection;
import javax.jms.JMSException;

/**
 * A subclass of {@link DelegatingConnectionFactory} that creates {@link TraceeConnection}.
 *
 * You can setup your {@link TraceeConnectionFactory} by simply injecting your origin {@link javax.jms.ConnectionFactory} instance.
 *
 * <pre class="code">
 * &#064;Bean
 * protected ConnectionFactory traceeConnectionFactory() {
 *     ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory("vm://localhost?broker.persistent=false");
 *     TraceeConnectionFactory connectionFactory = new TraceeConnectionFactory();
 *     connectionFactory.setTargetConnectionFactory(activeMQConnectionFactory);
 *     return connectionFactory;
 * }
 * </pre>
 *
 * You can inject your {@link TraceeConnectionFactory} to {@link org.springframework.jms.core.JmsTemplate}
 * so that it has the ability to write the current TraceeContext to the given {@link javax.jms.Message}.
 *
 * <pre class="code">
 * &#064;Bean
 * protected JmsTemplate jmsTemplate() {
 *     return new JmsTemplate(traceeConnectionFactory());
 * }
 * </pre>
 *
 * @see TraceeConnection
 * @see TraceeSession
 * @see org.springframework.jms.core.JmsTemplate
 * @see io.tracee.binding.jms.TraceeMessageProducer
 */
public class TraceeConnectionFactory extends DelegatingConnectionFactory {

	@Override
	public Connection createConnection() throws JMSException {
		return new TraceeConnection(super.createConnection());
	}

	@Override
	public Connection createConnection(String username, String password) throws JMSException {
		return new TraceeConnection(super.createConnection(username, password));
	}
}
