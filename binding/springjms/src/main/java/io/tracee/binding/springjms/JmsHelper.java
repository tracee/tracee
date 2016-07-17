package io.tracee.binding.springjms;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import io.tracee.Utilities;

import javax.jms.JMSException;
import javax.jms.Message;
import java.util.Map;

import static io.tracee.configuration.TraceeFilterConfiguration.Channel.AsyncDispatch;
import static io.tracee.configuration.TraceeFilterConfiguration.Channel.AsyncProcess;

/**
 * For manual reading / writing from/to messages
 */
public class JmsHelper {

	@SuppressWarnings("unused")
	public static void readTpicFromMessage(final Message message) throws JMSException {
		readTpicFromMessage(message, Tracee.getBackend());
	}

	public static void readTpicFromMessage(final Message message, final TraceeBackend backend) throws JMSException {
		if (message != null && backend.getConfiguration().shouldProcessContext(AsyncProcess)) {
			final Object encodedTraceeContext = message.getObjectProperty(TraceeConstants.TPIC_HEADER);
			if (encodedTraceeContext != null) {
				@SuppressWarnings("unchecked")
				final Map<String, String> contextFromMessage = (Map<String, String>) encodedTraceeContext;
				backend.putAll(backend.getConfiguration().filterDeniedParams(contextFromMessage, AsyncProcess));
			}
		}

		Utilities.generateInvocationIdIfNecessary(backend);
	}

	@SuppressWarnings("unused")
	public static void writeTpicToMessage(final Message message) throws JMSException {
		writeTpicToMessage(message, Tracee.getBackend());
	}

	public static void writeTpicToMessage(final Message message, final TraceeBackend backend) throws JMSException {
		if (!backend.isEmpty() && backend.getConfiguration().shouldProcessContext(AsyncDispatch) && message != null) {
			final Map<String, String> filteredContext = backend.getConfiguration().filterDeniedParams(backend.copyToMap(), AsyncDispatch);
			message.setObjectProperty(TraceeConstants.TPIC_HEADER, filteredContext);
		}
	}
}
