package io.tracee.jms.out;

import io.tracee.SimpleTraceeBackend;
import io.tracee.TraceeBackend;
import org.junit.Test;

import javax.jms.DeliveryMode;
import javax.jms.MessageProducer;
import javax.jms.QueueSender;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class TraceeQueueSenderTest {

	public static final QueueSender queueSender = mock(QueueSender.class);
	private final TraceeBackend backend = spy(SimpleTraceeBackend.createNonLoggingAllPermittingBackend());
	private final MessageProducer messageProducer = mock(MessageProducer.class);
	private final TraceeQueueSender unit = new TraceeQueueSender(new TraceeMessageProducer(messageProducer, backend), queueSender);

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