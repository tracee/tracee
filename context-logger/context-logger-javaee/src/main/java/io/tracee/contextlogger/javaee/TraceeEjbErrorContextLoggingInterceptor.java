package io.tracee.contextlogger.javaee;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.contextlogger.ImplicitContext;
import io.tracee.contextlogger.builder.TraceeContextLogger;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

/**
 * Message listener to detect exceptions that happened during javaee message processing.
 * In case of an exception contextual information will be written to the log.
 * Created by Tobias Gindler, holisticon AG on 13.03.14.
 */
public class TraceeEjbErrorContextLoggingInterceptor {

    static final String JSON_PREFIXED_MESSAGE = "TRACEE EJB INTERCEPTOR CONTEXT LOGGING LISTENER : ";

    @SuppressWarnings("unused")
    public TraceeEjbErrorContextLoggingInterceptor() {
    }

    @AroundInvoke
    public Object intercept(InvocationContext ctx) throws Exception {
        try {
            return ctx.proceed();
        } catch (Exception e) {

            // now log context informations
            TraceeContextLogger.createDefault().logJsonWithPrefixedMessage(JSON_PREFIXED_MESSAGE, ImplicitContext.COMMON, ImplicitContext.TRACEE, ctx, e);

            throw e;
        }
    }


}
