package io.tracee.binding.springrabbitmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Envelope;
import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.Utilities;
import io.tracee.configuration.PropertiesBasedTraceeFilterConfiguration;
import io.tracee.configuration.TraceeFilterConfiguration;
import io.tracee.configuration.TraceeFilterConfiguration.Profile;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.support.DefaultMessagePropertiesConverter;

import java.util.HashMap;
import java.util.Map;

import static io.tracee.TraceeConstants.TPIC_HEADER;
import static io.tracee.configuration.TraceeFilterConfiguration.Channel.AsyncDispatch;
import static io.tracee.configuration.TraceeFilterConfiguration.Channel.AsyncProcess;

public class TraceeMessagePropertiesConverter extends DefaultMessagePropertiesConverter {

	private final TraceeBackend backend;
	private final TraceeFilterConfiguration filterConfiguration;

	/**
	 * @deprecated use full ctor
	 */
	@Deprecated
	public TraceeMessagePropertiesConverter() {
		this(Tracee.getBackend(), PropertiesBasedTraceeFilterConfiguration.instance().DEFAULT);
	}

	/**
	 * @deprecated use full ctor
	 */
	@Deprecated
	public TraceeMessagePropertiesConverter(String profile) {
		this(Tracee.getBackend(), PropertiesBasedTraceeFilterConfiguration.instance().forProfile(profile));
	}

	public TraceeMessagePropertiesConverter(TraceeBackend backend, TraceeFilterConfiguration filterConfiguration) {
		this.backend = backend;
		this.filterConfiguration = filterConfiguration;
	}

	/**
	 * Incoming messages
	 */
	@Override
	@SuppressWarnings("unchecked")
	public MessageProperties toMessageProperties(AMQP.BasicProperties source, Envelope envelope, String charset) {
		final MessageProperties messageProperties = super.toMessageProperties(source, envelope, charset);

		if (filterConfiguration.shouldProcessContext(AsyncProcess)) {
			// Values are stored as type of LongStringHelper.ByteArrayLongString - but it's private
			final Map<String, String> traceeContextMap = transformToTraceeContextMap(
					(Map<String, ?>) messageProperties.getHeaders().get(TPIC_HEADER));
			if (traceeContextMap != null && !traceeContextMap.isEmpty()) {
				backend.putAll(filterConfiguration.filterDeniedParams(traceeContextMap, AsyncProcess));
			}
		}

		Utilities.generateInvocationIdIfNecessary(backend, filterConfiguration);
		return messageProperties;
	}

	private Map<String, String> transformToTraceeContextMap(final Map<String, ?> tpicMessageHeader) {
		final Map<String, String> traceeContext = new HashMap<>();
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

		if (!backend.isEmpty() && filterConfiguration.shouldProcessContext(AsyncDispatch)) {
			final Map<String, String> filteredParams = filterConfiguration.filterDeniedParams(backend.copyToMap(), AsyncDispatch);
			source.getHeaders().put(TPIC_HEADER, filteredParams);
		}

		return super.fromMessageProperties(source, charset);
	}
}
