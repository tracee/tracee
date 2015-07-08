package io.tracee.binding.springrabbitmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.LongString;
import io.tracee.testhelper.SimpleTraceeBackend;
import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import org.junit.Test;
import org.springframework.amqp.core.MessageProperties;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static io.tracee.TraceeConstants.INVOCATION_ID_KEY;
import static io.tracee.TraceeConstants.TPIC_HEADER;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsMapContaining.hasEntry;
import static org.mockito.Mockito.mock;

public class TraceeMessagePropertiesConverterTest {
	private static final String USED_PROFILE = "A_PROFILE";
	private static final String CHARSET_UTF8 = "UTF-8";
	private final TraceeBackend backend = SimpleTraceeBackend.createNonLoggingAllPermittingBackend();

	private final TraceeMessagePropertiesConverter unit = new TraceeMessagePropertiesConverter(backend, USED_PROFILE);

	@Test
	public void shouldAddTraceeContexttoMessageHeaders() throws Exception {
		backend.put("inClientBeforeRequest", "true");

		MessageProperties messageProperties = new MessageProperties();
		final AMQP.BasicProperties basicProperties = unit.fromMessageProperties(messageProperties, CHARSET_UTF8);

		assertThat(basicProperties.getHeaders(), hasKey(TPIC_HEADER));
		assertThat((Map<String, ?>)basicProperties.getHeaders().get(TPIC_HEADER), hasKey("inClientBeforeRequest"));
	}

	@Test
	public void messageParserShouldHandleMissingTpicHeaderAndGenerateInvocationId() {
		unit.toMessageProperties(createRabbitHeaderWith("anyKey", "anyValue"), mock(Envelope.class), CHARSET_UTF8);
		assertThat(backend.copyToMap(), hasKey(TraceeConstants.INVOCATION_ID_KEY));
	}

	@Test
	public void parseMessageHeaderIntoBackend() throws Exception {
		final Map<String, LongString> rabbitTraceeHeader = new HashMap<String, LongString>();
		rabbitTraceeHeader.put(INVOCATION_ID_KEY, new TestLongString("Crazy ID"));

		unit.toMessageProperties(createRabbitHeaderWith(TPIC_HEADER, rabbitTraceeHeader), mock(Envelope.class), CHARSET_UTF8);

		assertThat(backend.copyToMap(), hasEntry(INVOCATION_ID_KEY, "Crazy ID"));
	}

	@Test
	public void noTraceeHeaderShouldResultInGeneratedRequestId() throws Exception {
		unit.toMessageProperties(createRabbitHeaderWith("someHeader", "fromResponse=true"), mock(Envelope.class), CHARSET_UTF8);

		assertThat(backend.copyToMap(), hasKey(INVOCATION_ID_KEY));
		assertThat(backend.size(), is(1));
	}

	@Test
	public void roundTripShouldWorkAndGenerateRequestId() throws Exception {
		backend.put("inClientBeforeRequest", "true");

		MessageProperties messageProperties = new MessageProperties();
		final AMQP.BasicProperties basicProperties = unit.fromMessageProperties(messageProperties, CHARSET_UTF8);
		backend.clear();

		unit.toMessageProperties(basicProperties, mock(Envelope.class), CHARSET_UTF8);

		assertThat(backend.copyToMap(), hasEntry("inClientBeforeRequest", "true"));
		assertThat(backend.copyToMap(), hasKey(INVOCATION_ID_KEY));
		assertThat(backend.size(), is(2));
	}

	private AMQP.BasicProperties createRabbitHeaderWith(final String key, final Object value) {
		return new AMQP.BasicProperties().builder().headers(new HashMap<String, Object>() {{
			put(key, value);
		}}).build();
	}

	// Because LongStringHelper.ByteArrayLongString is private ..
	private class TestLongString implements LongString {

		private final String value;

		public TestLongString(String value) {
			this.value = value;
		}

		@Override
		public long length() {
			return value.length();
		}

		@Override
		public DataInputStream getStream() throws IOException {
			return new DataInputStream(new ByteArrayInputStream(getBytes()));
		}

		@Override
		public byte[] getBytes() {
			try {
				return value.getBytes(CHARSET_UTF8);
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public String toString() {
			return value;
		}
	}
}
