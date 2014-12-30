package io.tracee.binding.jms.out;

import io.tracee.SimpleTraceeBackend;
import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import org.junit.Before;
import org.junit.Test;

import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueSender;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TraceeQueueSenderTest {

	public static final QueueSender queueSender = mock(QueueSender.class);
	private final TraceeBackend backend = SimpleTraceeBackend.createNonLoggingAllPermittingBackend();
	private final MessageProducer messageProducer = mock(MessageProducer.class);
	private final TraceeQueueSender unit = new TraceeQueueSender(new TraceeMessageProducer(messageProducer, backend), queueSender);
	private final Message message = mock(Message.class);
	final Queue queue = mock(Queue.class);

	@Before
	public void setup() {
		backend.put("random", "entry");
	}

	@Test
	public void sendQueueShouldAddContextAndDelegate() throws Exception {
		unit.send(queue, message);
		verify(message).setObjectProperty(eq(TraceeConstants.JMS_HEADER_NAME), anyString());
		verify(queueSender).send(queue, message);
	}

	@Test
	public void sendQueueWithExtendParametersShouldAddContextAndDelegate() throws Exception {
		unit.send(queue, message, DeliveryMode.PERSISTENT, 5, 100);
		verify(message).setObjectProperty(eq(TraceeConstants.JMS_HEADER_NAME), anyString());
		verify(queueSender).send(queue, message, DeliveryMode.PERSISTENT, 5, 100);
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
		unit.close();
		verify(messageProducer).close();
		unit.getQueue();
		verify(queueSender).getQueue();
	}
}
