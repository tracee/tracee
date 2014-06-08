package io.tracee.contextlogger.jms;

import io.tracee.contextlogger.ImplicitContext;
import io.tracee.contextlogger.builder.TraceeContextLogger;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.jms.Message;
import java.lang.reflect.Method;

/**
 * Message listener to detect exceptions that happened during jms message processing.
 * In case of an exception contextual information will be written to the log.
 * Created by Tobias Gindler, holisticon AG on 13.03.14.
 */
public class TraceeJmsErrorMessageListener {

    @SuppressWarnings("unused")
    public TraceeJmsErrorMessageListener() {

    }

    @AroundInvoke
    public Object intercept(InvocationContext ctx) throws Exception {
        final boolean isMdbInvocation = isMessageListenerOnMessageMethod(ctx.getMethod());
        try {
            return ctx.proceed();
        } catch (Exception e) {

            if (isMdbInvocation) {
                TraceeContextLogger.createDefault().logJsonWithPrefixedMessage("TRACEE JMS ERROR CONTEXT LOGGING LISTENER  : ",
						ImplicitContext.COMMON, ImplicitContext.TRACEE, ctx, e);
            }

            throw e;
        }
    }

    final boolean isMessageListenerOnMessageMethod(Method method) {
        return "onMessage".equals(method.getName())
                && method.getParameterTypes().length == 1
                && method.getParameterTypes()[0] == Message.class;
    }

}
