package io.tracee.binding.jms;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import io.tracee.Utilities;
import io.tracee.transport.HttpHeaderTransport;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.jms.JMSException;
import javax.jms.Message;
import java.lang.reflect.Method;
import java.util.Map;

import static io.tracee.configuration.TraceeFilterConfiguration.Channel.AsyncProcess;
import static java.util.Collections.singletonList;

/**
 * EJB interceptor that parses a TracEE context from message properties and cleans it after message processing.
 */
public final class TraceeMessageListener {

	private final TraceeBackend backend;

	private final HttpHeaderTransport httpHeaderSerialization;

	TraceeMessageListener(TraceeBackend backend) {
		this.backend = backend;
		this.httpHeaderSerialization = new HttpHeaderTransport();
	}

	@SuppressWarnings("unused")
	public TraceeMessageListener() {
		this(Tracee.getBackend());
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

		if (backend.getConfiguration().shouldProcessContext(AsyncProcess)) {
			final String encodedTraceeContext = message.getStringProperty(TraceeConstants.TPIC_HEADER);
			if (encodedTraceeContext != null) {
				final Map<String, String> contextFromMessage = httpHeaderSerialization.parse(singletonList(encodedTraceeContext));
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

    Message extractMessageParameter(final Object[] parameters) {
	    return (Message) parameters[0];
    }

    boolean isMessageListenerOnMessageMethod(final Method method) {
        return "onMessage".equals(method.getName())
                && method.getParameterTypes().length == 1
                && method.getParameterTypes()[0] == Message.class;
    }
}
