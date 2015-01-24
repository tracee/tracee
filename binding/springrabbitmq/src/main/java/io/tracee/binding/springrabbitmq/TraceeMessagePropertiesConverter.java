package io.tracee.binding.springrabbitmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Envelope;
import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.Utilities;
import io.tracee.configuration.TraceeFilterConfiguration;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.support.DefaultMessagePropertiesConverter;

import java.util.HashMap;
import java.util.Map;

import static io.tracee.TraceeConstants.TPIC_HEADER;
import static io.tracee.configuration.TraceeFilterConfiguration.Channel.AsyncDispatch;
import static io.tracee.configuration.TraceeFilterConfiguration.Channel.AsyncProcess;

public class TraceeMessagePropertiesConverter extends DefaultMessagePropertiesConverter {

	private final TraceeBackend backend;
	private final String profile;

	public TraceeMessagePropertiesConverter() {
		this(Tracee.getBackend(), null);
	}

	public TraceeMessagePropertiesConverter(String profile) {
		this(Tracee.getBackend(), profile);
	}

	TraceeMessagePropertiesConverter(TraceeBackend backend, String profile) {
		this.backend = backend;
		this.profile = profile;
	}

	/**
	 * Incoming messages
	 */
	@Override
	public MessageProperties toMessageProperties(AMQP.BasicProperties source, Envelope envelope, String charset) {
		final MessageProperties messageProperties = super.toMessageProperties(source, envelope, charset);

		final TraceeFilterConfiguration filterConfiguration = backend.getConfiguration(profile);
		if (filterConfiguration.shouldProcessContext(AsyncProcess)) {
			// Values are stored as type of LongStringHelper.ByteArrayLongString - but it's private
			final Map<String, String> traceeContextMap = transformToTraceeContextMap(
					(Map<String, ?>) messageProperties.getHeaders().get(TPIC_HEADER));
			if (traceeContextMap != null && !traceeContextMap.isEmpty()) {
				backend.putAll(filterConfiguration.filterDeniedParams(traceeContextMap, AsyncProcess));
			}
		}

		Utilities.generateInvocationIdIfNecessary(backend);
		return messageProperties;
	}

	private Map<String, String> transformToTraceeContextMap(final Map<String, ?> tpicMessageHeader) {
		final Map<String, String> traceeContext = new HashMap<String, String>();
		if (tpicMessageHeader != null) {
			for (Map.Entry<String, ?> stringObjectEntry : tpicMessageHeader.entrySet()) {
				traceeContext.put(stringObjectEntry.getKey(), String.valueOf(stringObjectEntry.getValue()));
			}
		}
		return traceeContext;
	}

	/**
	 * Outgoing messages
	 */
	@Override
	public AMQP.BasicProperties fromMessageProperties(MessageProperties source, String charset) {

		final TraceeFilterConfiguration filterConfiguration = backend.getConfiguration(profile);
		if (!backend.isEmpty() && filterConfiguration.shouldProcessContext(AsyncDispatch)) {
			final Map<String, String> filteredParams = filterConfiguration.filterDeniedParams(backend.copyToMap(), AsyncDispatch);
			source.getHeaders().put(TPIC_HEADER, filteredParams);
		}

		return super.fromMessageProperties(source, charset);
	}
}
