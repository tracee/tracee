package de.holisticon.util.tracee.jms;

import de.holisticon.util.tracee.Tracee;
import de.holisticon.util.tracee.TraceeConstants;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.jms.JMSException;
import javax.jms.Message;
import java.lang.reflect.Method;
import java.util.TreeMap;

/**
 * EJB interceptor that resembles a Tracee context from message properties and clears it after message processing.
 *
 * @author Daniel Wegener (Holisticon AG)
 */
public class TraceeMessageListener {

    @AroundInvoke
    public Object intercept(InvocationContext ctx) throws Exception {
        final boolean provideContext = isMessageListenerOnMessageMethod(ctx.getMethod());

        final Message message = extractParameter(ctx.getParameters());
        try {
            if (provideContext) {
                beforeProcessing(ctx, message);
            }
            return ctx.proceed();
        } finally {
            if (provideContext) {
                afterProcessing(ctx, message);

            }
        }
    }

    public void beforeProcessing(InvocationContext ctx, Message message) throws JMSException {
        final Object encodedTraceeContext = message.getObjectProperty(TraceeConstants.JMS_HEADER_NAME);
        if (encodedTraceeContext != null) {
            final TreeMap<String, String> encodedTraceeProperties = (TreeMap<String, String>) encodedTraceeContext;
            Tracee.getBackend().putAll(encodedTraceeProperties);
        }
    }

    public void afterProcessing(InvocationContext ctx, Message message) {
        Tracee.getBackend().clear();
    }


    Message extractParameter(Object[] parameters) {
        if (parameters != null && parameters.length == 1 && parameters[0] instanceof Message) {
            return (Message) parameters[0];
        }
        return null;
    }

    boolean isMessageListenerOnMessageMethod(Method method) {
        return "onMessage".equals(method.getName())
                && method.getParameterTypes().length == 1
                && method.getParameterTypes()[0] == Message.class;
    }


}
