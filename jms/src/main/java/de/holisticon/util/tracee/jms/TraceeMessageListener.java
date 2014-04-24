package de.holisticon.util.tracee.jms;

import de.holisticon.util.tracee.Tracee;
import de.holisticon.util.tracee.TraceeBackend;
import de.holisticon.util.tracee.TraceeConstants;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.jms.JMSException;
import javax.jms.Message;
import java.lang.reflect.Method;
import java.util.Map;

import static de.holisticon.util.tracee.configuration.TraceeFilterConfiguration.Channel.AsyncProcess;

/**
 * EJB interceptor that parses a TracEE context from message properties and cleans it after message processing.
 *
 * @author Daniel Wegener (Holisticon AG)
 */
public final class TraceeMessageListener {

	private final TraceeBackend backend;

	TraceeMessageListener(TraceeBackend backend) {
		this.backend = backend;
	}

	@SuppressWarnings("unused")
	public TraceeMessageListener() {
		this(Tracee.getBackend());
	}

	@AroundInvoke
    public Object intercept(InvocationContext ctx) throws Exception {
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
    public void beforeProcessing(Message message) throws JMSException {

		if (backend.getConfiguration().shouldProcessContext(AsyncProcess)) {
			final Object encodedTraceeContext = message.getObjectProperty(TraceeConstants.JMS_HEADER_NAME);
			if (encodedTraceeContext != null) {
				final Map<String, String> contextFromMessage = (Map<String, String>) encodedTraceeContext;
				final Map<String, String> filteredContext = backend.getConfiguration().filterDeniedParams(contextFromMessage, AsyncProcess);
				backend.putAll(filteredContext);
			}
		}
    }

    void cleanUp() {
		if (backend.getConfiguration().shouldProcessContext(AsyncProcess)) {
			backend.clear();
		}
    }


    Message extractMessageParameter(Object[] parameters) {
	    return (Message) parameters[0];
    }

    boolean isMessageListenerOnMessageMethod(Method method) {
        return "onMessage".equals(method.getName())
                && method.getParameterTypes().length == 1
                && method.getParameterTypes()[0] == Message.class;
    }


}
