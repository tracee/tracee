package io.tracee.contextlogger.javaee;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import io.tracee.contextlogger.TraceeContextLogger;
import io.tracee.contextlogger.api.ErrorMessage;
import io.tracee.contextlogger.api.ImplicitContext;
import io.tracee.contextlogger.contextprovider.tracee.TraceeMessage;

/**
 * Message listener to detect exceptions that happened during javaee message processing.
 * In case of an exception contextual information will be written to the log.
 * Created by Tobias Gindler, holisticon AG on 13.03.14.
 */
public class TraceeErrorContextLoggingInterceptor {

    static final String JSON_PREFIXED_MESSAGE = "TRACEE EJB INTERCEPTOR CONTEXT LOGGING LISTENER : ";

    @SuppressWarnings("unused")
    public TraceeErrorContextLoggingInterceptor() {
    }

    @AroundInvoke
    public Object intercept(final InvocationContext ctx) throws Exception {
        try {
            return ctx.proceed();
        }
        catch (Exception e) {

            // try to get error message annotation
            ErrorMessage errorMessage = ctx.getMethod().getAnnotation(ErrorMessage.class);

            // now log context informations
            if (errorMessage == null) {
                TraceeContextLogger.createDefault().logJsonWithPrefixedMessage(JSON_PREFIXED_MESSAGE, ImplicitContext.COMMON, ImplicitContext.TRACEE, ctx,
                        e);
            }
            else {
                TraceeContextLogger.createDefault().logJsonWithPrefixedMessage(JSON_PREFIXED_MESSAGE, TraceeMessage.wrap(errorMessage.value()),
                        ImplicitContext.COMMON, ImplicitContext.TRACEE, ctx, e);
            }
            throw e;
        }
    }
}
