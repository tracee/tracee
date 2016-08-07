package io.tracee.binding.jms;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import io.tracee.Utilities;
import io.tracee.configuration.PropertiesBasedTraceeFilterConfiguration;
import io.tracee.configuration.TraceeFilterConfiguration;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.jms.JMSException;
import javax.jms.Message;
import java.lang.reflect.Method;
import java.util.Map;

import static io.tracee.configuration.TraceeFilterConfiguration.Channel.AsyncProcess;

/**
 * EJB interceptor that parses a TracEE context from message properties and cleans it after message processing.
 */
public final class TraceeMessageListener {

	private final TraceeBackend backend;
	private final TraceeFilterConfiguration filterConfiguration;

	protected TraceeMessageListener(TraceeBackend backend, TraceeFilterConfiguration filterConfiguration) {
		this.backend = backend;
		this.filterConfiguration = filterConfiguration;
	}

	/**
	 * @deprecated Use full constructor
	 */
	@Deprecated
	public TraceeMessageListener() {
		this(Tracee.getBackend(), PropertiesBasedTraceeFilterConfiguration.instance().DEFAULT);
	}

	@AroundInvoke
    public Object intercept(final InvocationContext ctx) throws Exception {
        final boolean isMdbInvocation = isMessageListenerOnMessageMethod(ctx.getMethod());
		try {
            if (isMdbInvocation) {
                beforeProcessing(extractMessageParameter(ctx.getParameters()));
            }
            return ctx.proceed();
        } finally {
            if (isMdbInvocation) {
				cleanUp();
            }
        }
    }

	@SuppressWarnings("unchecked")
    public void beforeProcessing(final Message message) throws JMSException {

		if (filterConfiguration.shouldProcessContext(AsyncProcess)) {
			final Object encodedTraceeContext = message.getObjectProperty(TraceeConstants.TPIC_HEADER);
			if (encodedTraceeContext != null) {
				final Map<String, String> contextFromMessage = (Map<String, String>) encodedTraceeContext;
				backend.putAll(filterConfiguration.filterDeniedParams(contextFromMessage, AsyncProcess));
			}
		}

		Utilities.generateInvocationIdIfNecessary(backend, filterConfiguration);
    }

    void cleanUp() {
		if (filterConfiguration.shouldProcessContext(AsyncProcess)) {
			backend.clear();
		}
    }

    Message extractMessageParameter(final Object[] parameters) {
	    return (Message) parameters[0];
    }

    boolean isMessageListenerOnMessageMethod(final Method method) {
        return "onMessage".equals(method.getName())
                && method.getParameterTypes().length == 1
                && method.getParameterTypes()[0] == Message.class;
    }
}
