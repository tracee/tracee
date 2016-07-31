package io.tracee.binding.springjms;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import io.tracee.Utilities;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.util.Map;

import static io.tracee.configuration.TraceeFilterConfiguration.Channel.AsyncProcess;

/**
 * A subclass of {@link DefaultMessageListenerContainer},
 * but wraps {@link this#invokeListener(Session, Message)} with TraceeContext writing and cleanup.
 *
 * With spring jms namespace support, you can easily replace your origin {@link DefaultMessageListenerContainer} with container-class element.
 *
 * <pre class="code">
 * &lt;beans&gt;
 *     &lt;jms:listener-container container-class="io.tracee.binding.springjms.TraceeMessageListenerContainer"&gt;
 *         &lt;jms:listener id="fooListener" destination="foo" ref="fooReceiver" method="handleMessage"/&gt;
 *     &lt;/jms:listener-container&gt;
 *
 *     &lt;bean id="fooReceiver" class="foo.Receiver"/&gt;
 * &lt;/beans&gt;
 * </pre>
 *
 * spring-jms supports jms message reply with {@link org.springframework.jms.listener.SessionAwareMessageListener}, if the {@link javax.jms.ConnectionFactory} used by {@link TraceeMessageListenerContainer} is a {@link TraceeConnectionFactory} instance,
 * the TraceeContext will be written to the replied message also.
 *
 * @see org.springframework.jms.listener.SessionAwareMessageListener
 */
public class TraceeMessageListenerContainer extends DefaultMessageListenerContainer {
	private final TraceeBackend backend;

	public TraceeMessageListenerContainer() {
		this(Tracee.getBackend());
	}

	public TraceeMessageListenerContainer(TraceeBackend traceeBackend) {
		this.backend = traceeBackend;
	}

	@Override
	protected void invokeListener(Session session, Message message) throws JMSException {
		beforeProcessing(message);
		try {
			super.invokeListener(session, message);
		} finally {
			cleanUp();
		}
	}

	void beforeProcessing(final Message message) throws JMSException {
		if (message != null && backend.getConfiguration().shouldProcessContext(AsyncProcess)) {
			final Object encodedTraceeContext = message.getObjectProperty(TraceeConstants.TPIC_HEADER);
			if (encodedTraceeContext != null) {
				final Map<String, String> contextFromMessage = (Map<String, String>) encodedTraceeContext;
				backend.putAll(backend.getConfiguration().filterDeniedParams(contextFromMessage, AsyncProcess));
			}
		}
		Utilities.generateInvocationIdIfNecessary(backend);
	}

	void cleanUp() {
		if (backend.getConfiguration().shouldProcessContext(AsyncProcess)) {
			backend.clear();
		}
	}
}
