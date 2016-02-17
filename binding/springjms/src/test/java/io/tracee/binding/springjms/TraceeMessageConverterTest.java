package io.tracee.binding.springjms;

import io.tracee.TraceeBackend;
import io.tracee.testhelper.SimpleTraceeBackend;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Matchers;
import org.springframework.jms.support.converter.MessageConverter;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.util.HashMap;
import java.util.Map;

import static io.tracee.TraceeConstants.TPIC_HEADER;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMapOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TraceeMessageConverterTest {

	private final MessageConverter messageConverter = mock(MessageConverter.class);

	private final TraceeBackend backend = spy(SimpleTraceeBackend.createNonLoggingAllPermittingBackend());

	private final TraceeMessageConverter UNIT = new TraceeMessageConverter(messageConverter, backend);

	private final Message message = mock(Message.class);

	@Before
	public void setup() throws JMSException {
		when(messageConverter.toMessage(any(), any(Session.class))).thenReturn(message);
	}

	@Test
	public void cleanBackendBeforeAddingNewValues() throws JMSException {
		final Map<String, String> tpicOfMessage = new HashMap<String, String>();
		tpicOfMessage.put("keyOfTpic", "Val654");
		when(message.getObjectProperty(TPIC_HEADER)).thenReturn(tpicOfMessage);

		UNIT.fromMessage(message);
		final InOrder inOrder = inOrder(backend, message);

		inOrder.verify(backend).clear();
		inOrder.verify(message).getObjectProperty(eq(TPIC_HEADER));
		inOrder.verify(backend).putAll(anyMapOf(String.class, String.class));
	}

	@Test
	public void writeTpicOfBackendIntoMessageOfDelegate() throws JMSException {
		backend.put("myTestK", "superVal");
		final Message message = UNIT.toMessage(null, null);

		verify(message).setObjectProperty(eq(TPIC_HEADER), Matchers.anyMapOf(String.class, String.class));
	}

	@Test
	public void readTpicFromMessage() throws JMSException {
		final Map<String, String> tpicOfMessage = new HashMap<String, String>();
		tpicOfMessage.put("keyOfTpic", "Val654");
		when(message.getObjectProperty(TPIC_HEADER)).thenReturn(tpicOfMessage);

		UNIT.fromMessage(message);

		assertThat(backend.copyToMap(), org.hamcrest.Matchers.hasEntry("keyOfTpic", "Val654"));
	}
}
