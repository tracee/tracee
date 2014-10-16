package io.tracee.contextlogger.watchdog;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.contextlogger.TraceeContextLogger;
import io.tracee.contextlogger.api.ErrorMessage;
import io.tracee.contextlogger.api.ImplicitContext;
import io.tracee.contextlogger.contextprovider.aspectj.WatchdogDataWrapper;
import io.tracee.contextlogger.contextprovider.tracee.TraceeMessage;
import io.tracee.contextlogger.watchdog.util.WatchdogUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Watchdog Assert class.
 * This aspect logs method calls of Watchdog annotated classes and methods in case of an exception is thrown during the execution of the method.
 * <p/>
 * Created by Tobias Gindler, holisticon AG on 16.02.14.
 */

@Aspect
public class WatchdogAspect {

    private final boolean active;

    public WatchdogAspect() {
        this(Boolean.valueOf(System.getProperty(Constants.SYSTEM_PROPERTY_IS_ACTIVE, "true")));
    }

    WatchdogAspect(boolean active) {
        this.active = active;
    }

    @SuppressWarnings("unused")
    @Pointcut("(execution(* *(..)) && @annotation(io.tracee.contextlogger.watchdog.Watchdog))")
    void withinWatchdogAnnotatedMethods() {
    }

    @SuppressWarnings("unused")
    @Pointcut("within(@io.tracee.contextlogger.watchdog.Watchdog *)")
    void withinClassWithWatchdogAnnotation() {

    }

    @SuppressWarnings("unused")
    @Pointcut("execution(public * *(..))")
    void publicMethods() {

    }

    @SuppressWarnings("unused")
    @Around("withinWatchdogAnnotatedMethods() || (publicMethods() && withinClassWithWatchdogAnnotation()) ")
    public Object guard(final ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        try {
            return proceedingJoinPoint.proceed();
        } catch (final Throwable e) {

            // check if watchdog processing is flagged as active
            if (active) {

                // Now create toJson output
                final TraceeBackend traceeBackend = Tracee.getBackend();

                // make sure that original exception will be passed through
                try {

                    // get watchdog annotation
                    Watchdog watchdog = WatchdogUtils.getWatchdogAnnotation(proceedingJoinPoint);

                    // check if watchdog aspect processing is deactivated by annotation
                    if (WatchdogUtils.checkProcessWatchdog(watchdog, proceedingJoinPoint, e)) {

                        String annotatedId = watchdog.id().isEmpty() ? null : watchdog.id();
                        sendErrorReportToConnectors(traceeBackend, proceedingJoinPoint, annotatedId, e);

                    }

                } catch (Throwable error) {
                    // will be ignored
                    traceeBackend.getLoggerFactory().getLogger(WatchdogAspect.class).error("error", error);
                }
            }
            // rethrow exception
            throw e;
        }

    }

    /**
     * Sends the error reports to all connectors.
     *
     * @param traceeBackend       the tracee backend
     * @param proceedingJoinPoint the aspectj calling context
     * @param annotatedId         the id defined in the watchdog annotation
     */
    void sendErrorReportToConnectors(TraceeBackend traceeBackend, ProceedingJoinPoint proceedingJoinPoint, String annotatedId, Throwable e) {

        // try to get error message annotation
        ErrorMessage errorMessage = WatchdogUtils.getErrorMessageAnnotation(proceedingJoinPoint);

        if (errorMessage == null) {
            TraceeContextLogger.createDefault().logJsonWithPrefixedMessage("TRACEE WATCHDOG ERROR CONTEXT LISTENER :", ImplicitContext.COMMON,
                    ImplicitContext.TRACEE, WatchdogDataWrapper.wrap(annotatedId, proceedingJoinPoint), e);
        } else {
            TraceeContextLogger.createDefault().logJsonWithPrefixedMessage("TRACEE WATCHDOG ERROR CONTEXT LISTENER :",
                    TraceeMessage.wrap(errorMessage.value()), ImplicitContext.COMMON, ImplicitContext.TRACEE,
                    WatchdogDataWrapper.wrap(annotatedId, proceedingJoinPoint), e);
        }
    }

}
