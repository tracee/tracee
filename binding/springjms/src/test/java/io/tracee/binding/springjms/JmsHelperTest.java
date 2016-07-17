package io.tracee.binding.springjms;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import org.junit.Before;
import org.junit.Test;

import javax.jms.JMSException;
import javax.jms.Message;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class JmsHelperTest {

	private final Message message = mock(Message.class);

	private final TraceeBackend backend = Tracee.getBackend();

	@Before
	public void setup() {
		backend.clear();
		backend.put("ourTestKey", "Foo321");
	}

	@Test
	public void shouldHandleNullMessagesOnReadingWithoutException() throws JMSException {
		JmsHelper.readTpicFromMessage(null);
	}

	@Test
	public void shouldHandleNullMessagesOnWritingWithoutException() throws JMSException {
		JmsHelper.writeTpicToMessage(null);
	}

	@Test
	public void writeTpicToMessage() throws JMSException {
		JmsHelper.writeTpicToMessage(message, backend);
		verify(message).setObjectProperty(eq(TraceeConstants.TPIC_HEADER), eq(backend.copyToMap()));
	}

	@Test
	public void readTpicFromMessage() throws JMSException {
		final Map<String, String> testTpic = new HashMap<String, String>();
		testTpic.put("myTestKey", "myTestval32");

		when(message.getObjectProperty(eq(TraceeConstants.TPIC_HEADER))).thenReturn(testTpic);
		JmsHelper.readTpicFromMessage(message, backend);
		assertThat(backend.copyToMap(), hasEntry("myTestKey", "myTestval32"));
	}

	@Test
	public void createInvocationIdIfNoInvocationIdCouldBeReadFromMessage() throws JMSException {
		final Map<String, String> testTpic = new HashMap<String, String>();
		testTpic.put("myTestKey", "myTestval32");

		when(message.getObjectProperty(eq(TraceeConstants.TPIC_HEADER))).thenReturn(testTpic);
		JmsHelper.readTpicFromMessage(message, backend);
		assertThat(backend.getInvocationId().length(), is(32));
	}
}
