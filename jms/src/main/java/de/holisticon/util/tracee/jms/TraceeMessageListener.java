package de.holisticon.util.tracee.jms;

import de.holisticon.util.tracee.Tracee;
import de.holisticon.util.tracee.TraceeBackend;
import de.holisticon.util.tracee.TraceeConstants;
import de.holisticon.util.tracee.configuration.TraceeFilterConfiguration;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.jms.JMSException;
import javax.jms.Message;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.TreeMap;

/**
 * EJB interceptor that resembles a Tracee context from message properties and clears it after message processing.
 *
 * @author Daniel Wegener (Holisticon AG)
 */
public class TraceeMessageListener {

	private final TraceeBackend backend = Tracee.getBackend();

    @AroundInvoke
    public Object intercept(InvocationContext ctx) throws Exception {
        final boolean provideContext = isMessageListenerOnMessageMethod(ctx.getMethod());
        try {
            if (provideContext) {
                beforeProcessing(ctx, extractMessageParameter(ctx.getParameters()));
            }
            return ctx.proceed();
        } finally {
            if (provideContext) {
                afterProcessing(ctx, extractMessageParameter(ctx.getParameters()));
            }
        }
    }

    public void beforeProcessing(InvocationContext ctx, Message message) throws JMSException {

		if (backend.getConfiguration().shouldProcessContext(TraceeFilterConfiguration.MessageType.AsyncProcess)) {
			final Object encodedTraceeContext = message.getObjectProperty(TraceeConstants.JMS_HEADER_NAME);
			if (encodedTraceeContext != null) {
				final Map<String, String> encodedTraceeProperties = (Map<String, String>) encodedTraceeContext;
				Tracee.getBackend().putAll(encodedTraceeProperties);
			}
		}
    }

    public void afterProcessing(InvocationContext ctx, Message message) {
		if (backend.getConfiguration().shouldProcessContext(TraceeFilterConfiguration.MessageType.AsyncProcess)) {
			Tracee.getBackend().clear();
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
