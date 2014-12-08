package io.tracee.jms.out;

import io.tracee.SimpleTraceeBackend;
import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import org.junit.Before;
import org.junit.Test;

import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Topic;
import javax.jms.TopicPublisher;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TraceeTopicPublisherTest {

	private final MessageProducer messageProducer = mock(MessageProducer.class);
	private final TopicPublisher topicPublisher = mock(TopicPublisher.class);
	private final TraceeBackend backend = SimpleTraceeBackend.createNonLoggingAllPermittingBackend();
	private final TraceeTopicPublisher unit = new TraceeTopicPublisher(new TraceeMessageProducer(messageProducer, backend), topicPublisher);
	private final Message message = mock(Message.class);

	@Before
	public void setup() {
		backend.put("random", "entry");
	}

	@Test
	public void publishMessageShouldAddContextAndDelegate() throws Exception {
		unit.publish(message);
		verify(message).setObjectProperty(eq(TraceeConstants.JMS_HEADER_NAME), anyString());
		verify(topicPublisher).publish(message);
	}

	@Test
	public void publishMessageWithTopicShouldAddContextAndDelegate() throws Exception {
		final Topic topic = mock(Topic.class);
		unit.publish(topic, message);
		verify(message).setObjectProperty(eq(TraceeConstants.JMS_HEADER_NAME), anyString());
		verify(topicPublisher).publish(topic, message);
	}

	@Test
	public void publishMessageWithDeliveryModeAndPriorityAndTTLShouldAddContextAndDelegate() throws Exception {
		unit.publish(message, DeliveryMode.PERSISTENT, 5, 100);
		verify(message).setObjectProperty(eq(TraceeConstants.JMS_HEADER_NAME), anyString());
		verify(topicPublisher).publish(message, DeliveryMode.PERSISTENT, 5, 100);
	}

	@Test
	public void publishMessageWithTopicAndDeliveryModeAndPriorityAndTTLShouldAddContextAndDelegate() throws Exception {
		final Topic topic = mock(Topic.class);
		unit.publish(topic, message, DeliveryMode.PERSISTENT, 5, 100);
		verify(message).setObjectProperty(eq(TraceeConstants.JMS_HEADER_NAME), anyString());
		verify(topicPublisher).publish(topic, message, DeliveryMode.PERSISTENT, 5, 100);
	}

	@Test
	public void sendMessageShouldAddContextAndDelegate() throws Exception {
		unit.send(message);
		verify(message).setObjectProperty(eq(TraceeConstants.JMS_HEADER_NAME), anyString());
		verify(messageProducer).send(message);
	}

	@Test
	public void sendMessageWithDestinationShouldAddContextAndDelegate() throws Exception {
		final Destination destination = mock(Destination.class);
		unit.send(destination, message);
		verify(message).setObjectProperty(eq(TraceeConstants.JMS_HEADER_NAME), anyString());
		verify(messageProducer).send(destination, message);
	}

	@Test
	public void sendMessageWithDeliveryModeAndPriorityAndTTLShouldAddContextAndDelegate() throws Exception {
		unit.send(message, DeliveryMode.PERSISTENT, 5, 100);
		verify(message).setObjectProperty(eq(TraceeConstants.JMS_HEADER_NAME), anyString());
		verify(messageProducer).send(message, DeliveryMode.PERSISTENT, 5, 100);
	}

	@Test
	public void sendMessageWithDestinationAndDeliveryModeAndPriorityAndTTLShouldAddContextAndDelegate() throws Exception {
		final Destination destination = mock(Destination.class);
		unit.send(destination, message, DeliveryMode.PERSISTENT, 5, 100);
		verify(message).setObjectProperty(eq(TraceeConstants.JMS_HEADER_NAME), anyString());
		verify(messageProducer).send(destination, message, DeliveryMode.PERSISTENT, 5, 100);
	}

	@Test
	public void shouldSimpleDelegate() throws Exception {
		unit.setDisableMessageID(false);
		verify(messageProducer).setDisableMessageID(false);
		unit.getDisableMessageID();
		verify(messageProducer).getDisableMessageID();
		unit.setDisableMessageTimestamp(false);
		verify(messageProducer).setDisableMessageTimestamp(false);
		unit.getDisableMessageTimestamp();
		verify(messageProducer).getDisableMessageTimestamp();
		unit.setDeliveryMode(DeliveryMode.PERSISTENT);
		verify(messageProducer).setDeliveryMode(DeliveryMode.PERSISTENT);
		unit.getDeliveryMode();
		verify(messageProducer).getDeliveryMode();
		unit.setPriority(5);
		verify(messageProducer).setPriority(5);
		unit.getPriority();
		verify(messageProducer).getPriority();
		unit.setTimeToLive(5);
		verify(messageProducer).setTimeToLive(5);
		unit.getTimeToLive();
		verify(messageProducer).getTimeToLive();
		unit.getDestination();
		verify(messageProducer).getDestination();
		unit.getTopic();
		verify(topicPublisher).getTopic();
		unit.close();
		verify(messageProducer).close();
	}
}