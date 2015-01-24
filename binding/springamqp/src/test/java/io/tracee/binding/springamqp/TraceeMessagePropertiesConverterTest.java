package io.tracee.binding.springamqp;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Envelope;
import io.tracee.NoopTraceeLoggerFactory;
import io.tracee.PermitAllTraceeFilterConfiguration;
import io.tracee.SimpleTraceeBackend;
import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import io.tracee.transport.HttpHeaderTransport;
import org.junit.Test;
import org.springframework.amqp.core.MessageProperties;

import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsMapContaining.hasEntry;
import static org.mockito.Mockito.mock;

public class TraceeMessagePropertiesConverterTest {
	private static final String USED_PROFILE = "A_PROFILE";
	private static final String CHARSET = "UTF-8";
	private final HttpHeaderTransport transportSerializationMock = new HttpHeaderTransport(NoopTraceeLoggerFactory.INSTANCE);
	private final TraceeBackend backend = new SimpleTraceeBackend(new PermitAllTraceeFilterConfiguration(), NoopTraceeLoggerFactory.INSTANCE);

	private final TraceeMessagePropertiesConverter unit =
			new TraceeMessagePropertiesConverter(backend, transportSerializationMock, USED_PROFILE);


	@Test
	public void testFromMessageProperties() throws Exception {
		backend.put("inClientBeforeRequest", "true");

		MessageProperties messageProperties = new MessageProperties();
		final AMQP.BasicProperties basicProperties = unit.fromMessageProperties(messageProperties, CHARSET);

		assertThat(basicProperties.getHeaders(), hasEntry(TraceeConstants.HTTP_HEADER_NAME, (Object) "inClientBeforeRequest=true"));
	}

	@Test
	public void testToMessageProperties() throws Exception {
		AMQP.BasicProperties basicProperties = new AMQP.BasicProperties().builder().headers(new HashMap<String, Object>() {{
			put(TraceeConstants.HTTP_HEADER_NAME, "fromResponse=true");
		}}).build();
		unit.toMessageProperties(basicProperties, mock(Envelope.class), CHARSET);

		assertThat(backend.get("fromResponse"), is("true"));
	}
}
