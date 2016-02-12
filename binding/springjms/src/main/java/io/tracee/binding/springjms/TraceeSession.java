package io.tracee.binding.springjms;

import io.tracee.binding.jms.TraceeMessageWriter;

import javax.jms.*;
import java.io.Serializable;

/**
 * A delegating {@link Session},
 * wraps the MessageProducer created by {@link this#delegate}.
 *
 * @see io.tracee.binding.jms.TraceeMessageProducer
 * @see TraceeMessageWriter
 */
public final class TraceeSession implements Session {
	private Session delegate;

	public TraceeSession(Session session) {
		this.delegate = session;
	}

	@Override
	public void close() throws JMSException {
		delegate.close();
	}

	@Override
	public MessageConsumer createConsumer(Destination destination, String messageSelector) throws JMSException {
		return delegate.createConsumer(destination, messageSelector);
	}

	@Override
	public Queue createQueue(String queueName) throws JMSException {
		return delegate.createQueue(queueName);
	}

	@Override
	public TemporaryTopic createTemporaryTopic() throws JMSException {
		return delegate.createTemporaryTopic();
	}

	@Override
	public TemporaryQueue createTemporaryQueue() throws JMSException {
		return delegate.createTemporaryQueue();
	}

	@Override
	public void unsubscribe(String name) throws JMSException {
		delegate.unsubscribe(name);
	}

	@Override
	public TopicSubscriber createDurableSubscriber(Topic topic, String name) throws JMSException {
		return delegate.createDurableSubscriber(topic, name);
	}

	@Override
	public MessageProducer createProducer(Destination destination) throws JMSException {
		return TraceeMessageWriter.wrap(delegate.createProducer(destination));
	}

	@Override
	public MessageListener getMessageListener() throws JMSException {
		return delegate.getMessageListener();
	}

	@Override
	public void run() {
		delegate.run();
	}

	@Override
	public MessageConsumer createConsumer(Destination destination) throws JMSException {
		return delegate.createConsumer(destination);
	}

	@Override
	public Topic createTopic(String topicName) throws JMSException {
		return delegate.createTopic(topicName);
	}

	@Override
	public TopicSubscriber createDurableSubscriber(Topic topic, String name, String messageSelector, boolean noLocal) throws JMSException {
		return delegate.createDurableSubscriber(topic, name, messageSelector, noLocal);
	}

	@Override
	public TextMessage createTextMessage() throws JMSException {
		return delegate.createTextMessage();
	}

	@Override
	public ObjectMessage createObjectMessage() throws JMSException {
		return delegate.createObjectMessage();
	}

	@Override
	public void commit() throws JMSException {
		delegate.commit();
	}

	@Override
	public ObjectMessage createObjectMessage(Serializable object) throws JMSException {
		return delegate.createObjectMessage(object);
	}

	@Override
	public MapMessage createMapMessage() throws JMSException {
		return delegate.createMapMessage();
	}

	@Override
	public boolean getTransacted() throws JMSException {
		return delegate.getTransacted();
	}

	@Override
	public BytesMessage createBytesMessage() throws JMSException {
		return delegate.createBytesMessage();
	}

	@Override
	public MessageConsumer createConsumer(Destination destination, String messageSelector, boolean NoLocal) throws JMSException {
		return delegate.createConsumer(destination, messageSelector, NoLocal);
	}

	@Override
	public void setMessageListener(MessageListener listener) throws JMSException {
		delegate.setMessageListener(listener);
	}

	@Override
	public Message createMessage() throws JMSException {
		return delegate.createMessage();
	}

	@Override
	public void recover() throws JMSException {
		delegate.recover();
	}

	@Override
	public int getAcknowledgeMode() throws JMSException {
		return delegate.getAcknowledgeMode();
	}

	@Override
	public void rollback() throws JMSException {
		delegate.rollback();
	}

	@Override
	public TextMessage createTextMessage(String text) throws JMSException {
		return delegate.createTextMessage(text);
	}

	@Override
	public QueueBrowser createBrowser(Queue queue, String messageSelector) throws JMSException {
		return delegate.createBrowser(queue, messageSelector);
	}

	@Override
	public QueueBrowser createBrowser(Queue queue) throws JMSException {
		return delegate.createBrowser(queue);
	}

	@Override
	public StreamMessage createStreamMessage() throws JMSException {
		return delegate.createStreamMessage();
	}
}
