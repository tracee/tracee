package de.holisticon.util.tracee.jms.out;

import de.holisticon.util.tracee.SimpleTraceeBackend;
import de.holisticon.util.tracee.TraceeBackend;
import de.holisticon.util.tracee.TraceeConstants;
import org.junit.Test;
import org.mockito.Mockito;

import javax.jms.Message;
import javax.jms.MessageProducer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
public class TraceeMessageProducerTest {

	private final TraceeBackend backend = SimpleTraceeBackend.createNonLoggingAllPermittingBackend();
	private final MessageProducer messageProducer = Mockito.mock(MessageProducer.class);
	private final TraceeMessageProducer unit = new TraceeMessageProducer(messageProducer, backend);
	private final Message message = Mockito.mock(Message.class);


	@Test
	public void testWriteTraceeContextToMessage() throws Exception {
		backend.put("random", "entry");
		unit.writeTraceeContextToMessage(message);
		verify(message).setObjectProperty(eq(TraceeConstants.JMS_HEADER_NAME), eq(Collections.singletonMap("random","entry")));
	}

	@Test
	public void testDontWriteEmptyTraceeContextToMessage() throws Exception {
		backend.clear();
		unit.writeTraceeContextToMessage(message);
		verifyNoMoreInteractions(message);
	}
}
