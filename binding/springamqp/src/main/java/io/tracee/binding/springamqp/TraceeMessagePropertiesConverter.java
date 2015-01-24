package io.tracee.binding.springamqp;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Envelope;
import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import io.tracee.configuration.TraceeFilterConfiguration;
import io.tracee.transport.HttpHeaderTransport;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.support.DefaultMessagePropertiesConverter;

import java.util.Arrays;
import java.util.Map;

import static io.tracee.configuration.TraceeFilterConfiguration.Channel.IncomingResponse;
import static io.tracee.configuration.TraceeFilterConfiguration.Channel.OutgoingRequest;

public class TraceeMessagePropertiesConverter extends DefaultMessagePropertiesConverter {

	private final TraceeBackend backend;
	private final HttpHeaderTransport transportSerialization;
	private final String profile;

	public TraceeMessagePropertiesConverter() {
		this(Tracee.getBackend(), new HttpHeaderTransport(Tracee.getBackend().getLoggerFactory()), null);
	}

	public TraceeMessagePropertiesConverter(String profile) {
		this(Tracee.getBackend(), new HttpHeaderTransport(Tracee.getBackend().getLoggerFactory()), profile);
	}

	protected TraceeMessagePropertiesConverter(TraceeBackend backend, HttpHeaderTransport transportSerialization, String profile) {
		this.backend = backend;
		this.transportSerialization = transportSerialization;
		this.profile = profile;
	}

	/**
	 * Incoming messages
	 */
	@Override
	public MessageProperties toMessageProperties(AMQP.BasicProperties source, Envelope envelope, String charset) {
		final MessageProperties messageProperties = super.toMessageProperties(source, envelope, charset);

		final String header = String.valueOf(messageProperties.getHeaders().get(TraceeConstants.HTTP_HEADER_NAME));
		if (header != null && !header.isEmpty()) {
			final TraceeFilterConfiguration filterConfiguration = backend.getConfiguration(profile);

			if (filterConfiguration.shouldProcessContext(IncomingResponse)) {
				final Map<String, String> parsedContext = transportSerialization.parse(Arrays.asList(header));
				backend.putAll(filterConfiguration.filterDeniedParams(parsedContext, IncomingResponse));
			}
		}

		return messageProperties;
	}

	/**
	 * Outgoing messages
	 */
	@Override
	public AMQP.BasicProperties fromMessageProperties(MessageProperties source, String charset) {

		final TraceeFilterConfiguration filterConfiguration = backend.getConfiguration(profile);
		if (!backend.isEmpty() && filterConfiguration.shouldProcessContext(OutgoingRequest)) {
			final Map<String, String> filteredParams = filterConfiguration.filterDeniedParams(backend.copyToMap(), OutgoingRequest);
			final String contextAsHeader = transportSerialization.render(filteredParams);
			source.getHeaders().put(TraceeConstants.HTTP_HEADER_NAME, contextAsHeader);
		}

		return super.fromMessageProperties(source, charset);
	}
}
