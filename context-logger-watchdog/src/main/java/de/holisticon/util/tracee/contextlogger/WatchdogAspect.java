package de.holisticon.util.tracee.contextlogger;

import de.holisticon.util.tracee.contextlogger.json.generator.datawrapper.WatchdogDataWrapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import de.holisticon.util.tracee.Tracee;
import de.holisticon.util.tracee.TraceeBackend;
import de.holisticon.util.tracee.contextlogger.json.generator.TraceeContextLoggerJsonCreator;

/**
 * Watchdog Assert class.
 * This aspects logs method calls of Watchdog annotated classes and methods in case of an exception is thrown during the execution of the method.
 *
 * Created by Tobias Gindler, holisticon AG on 16.02.14.
 */

@Aspect
public class WatchdogAspect {

    @Pointcut("(execution(* *(..)) && @annotation(de.holisticon.util.tracee.contextlogger.Watchdog))")
    void withinWatchdogAnnotatedMethods() {
    }

    @Pointcut("within(@de.holisticon.util.tracee.contextlogger.Watchdog *)")
    void withinClassWithWatchdogAnnotation() {

    }

    @Pointcut("execution(public * *(..))")
    void publicMethods() {

    }

    @Around("withinWatchdogAnnotatedMethods() || (publicMethods() && withinClassWithWatchdogAnnotation()) ")
    public Object guard(final ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        try {
            return proceedingJoinPoint.proceed();
        }
        catch (final Throwable e) {

            // make sure that original exception will be passed through
            try {
                // Now create log output
                final TraceeBackend traceeBackend = Tracee.getBackend();

                final TraceeContextLoggerJsonCreator errorJsonCreator = TraceeContextLoggerJsonCreator.createJsonCreator()
                        .addPrefixedMessage("TRACEE WATCHDOG :")
                        .addWatchdogCategory(WatchdogDataWrapper.wrap(proceedingJoinPoint))
                        .addCommonCategory().addExceptionCategory(e).addTraceeCategory(traceeBackend);

                traceeBackend.getLoggerFactory().getLogger(WatchdogAspect.class).error(errorJsonCreator);

            } catch (Throwable error) {

            }

            // rethrow exception
            throw e;
        }

    }

}
