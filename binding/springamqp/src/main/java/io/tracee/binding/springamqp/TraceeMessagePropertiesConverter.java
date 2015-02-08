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

	TraceeMessagePropertiesConverter(TraceeBackend backend, HttpHeaderTransport transportSerialization, String profile) {
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

		final Object headerObject = messageProperties.getHeaders().get(TraceeConstants.JMS_HEADER_NAME);
		if (headerObject != null) {
			final String headerString = String.valueOf(headerObject);
			if (!headerString.isEmpty()) {
				final TraceeFilterConfiguration filterConfiguration = backend.getConfiguration(profile);

				if (filterConfiguration.shouldProcessContext(TraceeFilterConfiguration.Channel.AsyncProcess)) {
					final Map<String, String> parsedContext = transportSerialization.parse(Arrays.asList(headerString));
					backend.putAll(filterConfiguration.filterDeniedParams(parsedContext, TraceeFilterConfiguration.Channel.AsyncProcess));
				}
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
		if (!backend.isEmpty() && filterConfiguration.shouldProcessContext(TraceeFilterConfiguration.Channel.AsyncDispatch)) {
			final Map<String, String> filteredParams = filterConfiguration.filterDeniedParams(backend.copyToMap(), TraceeFilterConfiguration.Channel.AsyncDispatch);
			final String contextAsHeader = transportSerialization.render(filteredParams);
			source.getHeaders().put(TraceeConstants.JMS_HEADER_NAME, contextAsHeader);
		}

		return super.fromMessageProperties(source, charset);
	}
}
