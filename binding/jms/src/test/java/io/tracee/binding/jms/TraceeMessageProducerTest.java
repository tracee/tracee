package io.tracee.binding.jms;

import io.tracee.testhelper.SimpleTraceeBackend;
import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import io.tracee.testhelper.SimpleTraceeBackend;
import io.tracee.transport.HttpHeaderTransport;
import org.junit.Test;

import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageProducer;
import java.util.Collections;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class TraceeMessageProducerTest {

	private final HttpHeaderTransport httpHeaderTransport = new HttpHeaderTransport();

	private final TraceeBackend backend = spy(SimpleTraceeBackend.createNonLoggingAllPermittingBackend());
	private final MessageProducer messageProducer = mock(MessageProducer.class);
	private final TraceeMessageProducer unit = new TraceeMessageProducer(messageProducer, backend);
	private final Message message = mock(Message.class);

	@Test
	public void testWriteTraceeContextToMessage() throws Exception {
		backend.put("random", "entry");
		unit.writeTraceeContextToMessage(message);
		final String renderedTpic = httpHeaderTransport.render(Collections.singletonMap("random", "entry"));
		verify(message).setStringProperty(eq(TraceeConstants.TPIC_HEADER), eq(renderedTpic));
	}

	@Test
	public void testDontWriteEmptyTraceeContextToMessage() throws Exception {
		backend.clear();
		unit.writeTraceeContextToMessage(message);
		verifyNoMoreInteractions(message);
	}

	@Test
	public void sendMessageShouldAddContextAndDelegate() throws Exception {
		backend.put("random", "entry");
		unit.send(message);
		verify(message).setStringProperty(eq(TraceeConstants.TPIC_HEADER), anyString());
		verify(messageProducer).send(message);
	}

	@Test
	public void sendMessageWithDestinationShouldAddContextAndDelegate() throws Exception {
		backend.put("random", "entry");
		final Destination destination = mock(Destination.class);
		unit.send(destination, message);
		verify(message).setStringProperty(eq(TraceeConstants.TPIC_HEADER), anyString());
		verify(messageProducer).send(destination, message);
	}

	@Test
	public void sendMessageWithDeliveryModeAndPriorityAndTTLShouldAddContextAndDelegate() throws Exception {
		backend.put("random", "entry");
		unit.send(message, DeliveryMode.PERSISTENT, 5, 100);
		verify(message).setStringProperty(eq(TraceeConstants.TPIC_HEADER), anyString());
		verify(messageProducer).send(message, DeliveryMode.PERSISTENT, 5, 100);
	}

	@Test
	public void sendMessageWithDestinationAndDeliveryModeAndPriorityAndTTLShouldAddContextAndDelegate() throws Exception {
		backend.put("random", "entry");
		final Destination destination = mock(Destination.class);
		unit.send(destination, message, DeliveryMode.PERSISTENT, 5, 100);
		verify(message).setStringProperty(eq(TraceeConstants.TPIC_HEADER), anyString());
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
	}

	/**
	 *
	 * @see <a href="https://github.com/tracee/tracee/issues/168">TracEE Issue 168</a>
	 */
	@Test
	public void writeContextWithoutUsingObjectMethods() throws Exception {
		backend.put("random", "entry");
		unit.send(message);
		verify(message, never()).setObjectProperty(anyString(), any());
	}
}
