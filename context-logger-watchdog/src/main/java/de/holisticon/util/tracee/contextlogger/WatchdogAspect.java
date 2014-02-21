package de.holisticon.util.tracee.contextlogger;


import de.holisticon.util.tracee.contextlogger.connector.ConnectorFactory;
import de.holisticon.util.tracee.contextlogger.json.generator.datawrapper.WatchdogDataWrapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import de.holisticon.util.tracee.Tracee;
import de.holisticon.util.tracee.TraceeBackend;
import de.holisticon.util.tracee.contextlogger.json.generator.TraceeContextLoggerJsonCreator;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * Watchdog Assert class.
 * This aspects logs method calls of Watchdog annotated classes and methods in case of an exception is thrown during the execution of the method.
 * <p/>
 * Created by Tobias Gindler, holisticon AG on 16.02.14.
 */

@Aspect
public class WatchdogAspect {

    public static final boolean WATCHDOG_IS_ACTIVE = Boolean.valueOf(System.getProperty("de.holisticon.util.tracee.contextlogger.Watchdog.isActive", "true"));

    @SuppressWarnings("unused")
    @Pointcut("(execution(* *(..)) && @annotation(de.holisticon.util.tracee.contextlogger.Watchdog))")
    void withinWatchdogAnnotatedMethods() {
    }

    @SuppressWarnings("unused")
    @Pointcut("within(@de.holisticon.util.tracee.contextlogger.Watchdog *)")
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
            if (WATCHDOG_IS_ACTIVE) {
                // make sure that original exception will be passed through
                try {

                    // Now create log output
                    final TraceeBackend traceeBackend = Tracee.getBackend();

                    // get watchdog annotation
                    Watchdog watchdog = getWatchdogAnnotation(proceedingJoinPoint);
                    String annotatedId = watchdog.id().isEmpty() ? null : watchdog.id();

                    boolean mustPreventExceptionLogging = mustSuppressException(proceedingJoinPoint, e);

                    if (!watchdog.suppressThrowsExceptions() || (watchdog.suppressThrowsExceptions() && !mustPreventExceptionLogging)) {

                        final TraceeContextLoggerJsonCreator errorJsonCreator = TraceeContextLoggerJsonCreator.createJsonCreator()
                                .addPrefixedMessage("TRACEE WATCHDOG :")
                                .addWatchdogCategory(WatchdogDataWrapper.wrap(annotatedId, proceedingJoinPoint))
                                .addCommonCategory()
                                .addExceptionCategory(e)
                                .addTraceeCategory(traceeBackend);

                        ConnectorFactory.sendErrorReportToConnectors(errorJsonCreator);
                    }

                } catch (Throwable error) {
                    // will be ignored
                    Tracee.getBackend().getLoggerFactory().getLogger(WatchdogAspect.class).error("error",error);
                }
            }
            // rethrow exception
            throw e;
        }

    }


    private Watchdog getWatchdogAnnotation(final ProceedingJoinPoint proceedingJoinPoint) {

        // get watchdog annotation at class
        Watchdog clazzAnnotation = (Watchdog) proceedingJoinPoint.getSignature().getDeclaringType().getAnnotation(Watchdog.class);

        // get watchdog annotation from method
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        Watchdog methodAnnotation = methodSignature.getMethod().getAnnotation(Watchdog.class);

        return methodAnnotation != null ? methodAnnotation : clazzAnnotation;

    }

    boolean mustSuppressException(final ProceedingJoinPoint proceedingJoinPoint, Throwable thrownException) {

        // get watchdog annotation from method
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();

        Class[] classes = methodSignature.getMethod().getExceptionTypes();

        return checkClassIsThrowsException(classes, thrownException);
    }

    boolean checkClassIsThrowsException(Class[] classes, Throwable thrownException) {

        for (Class clazz : classes) {

            if (clazz.isInstance(thrownException)) {
                return true;
            }

        }

        return false;
    }


}
