package io.tracee.spring;

import io.tracee.contextlogger.ImplicitContext;
import io.tracee.contextlogger.builder.ContextLogger;
import io.tracee.contextlogger.builder.TraceeContextLogger;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;

/**
 * @author <a href="carl.duevel@holisticon.de">Carl Anders Düvel</a>
 */
@Aspect
class LogContextOnExceptionAspect {
    private ContextLogger logger = TraceeContextLogger.createDefault();

    @AfterThrowing(pointcut = "@annotation(io.tracee.spring.LogContextOnException)", throwing = "exception")
    public void logContextOnException(Throwable exception) {
        logger.logJsonWithPrefixedMessage("TRACEE SPRING CONTEXT LOGGING LISTENER : ", ImplicitContext.COMMON, ImplicitContext.TRACEE, exception);
    }
}
