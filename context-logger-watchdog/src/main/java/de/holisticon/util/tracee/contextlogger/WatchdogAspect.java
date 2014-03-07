package de.holisticon.util.tracee.contextlogger;


import de.holisticon.util.tracee.contextlogger.connector.ConnectorFactory;
import de.holisticon.util.tracee.contextlogger.json.generator.datawrapper.WatchdogDataWrapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import de.holisticon.util.tracee.Tracee;
import de.holisticon.util.tracee.TraceeBackend;
import de.holisticon.util.tracee.contextlogger.json.generator.TraceeContextLoggerJsonBuilder;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * Watchdog Assert class.
 * This aspects logs method calls of Watchdog annotated classes and methods in case of an exception is thrown during the execution of the method.
 * <p/>
 * Created by Tobias Gindler, holisticon AG on 16.02.14.
 */

@Aspect
public class WatchdogAspect {

    public static final boolean WATCHDOG_IS_ACTIVE = Boolean.valueOf(System.getProperty(Constants.SYSTEM_PROPERTY_IS_ACTIVE, "true"));



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

                // Now create log output
                final TraceeBackend traceeBackend = Tracee.getBackend();

                // make sure that original exception will be passed through
                try {

                    // get watchdog annotation
                    Watchdog watchdog = getWatchdogAnnotation(proceedingJoinPoint);

                    // check if watchdog aspect processing is deactivated by annotation
                    if (watchdog.isActive()) {

                        String annotatedId = watchdog.id().isEmpty() ? null : watchdog.id();

                        boolean mustPreventExceptionLogging = mustSuppressException(proceedingJoinPoint, e);

                        if (!watchdog.suppressThrowsExceptions() || (watchdog.suppressThrowsExceptions() && !mustPreventExceptionLogging)) {

                            sendErrorReportToConnectors(traceeBackend, proceedingJoinPoint, annotatedId, e);
                            writeMethodCallToMdc(traceeBackend, proceedingJoinPoint, annotatedId);

                        }

                    }

                } catch (Throwable error) {
                    // will be ignored
                    traceeBackend.getLoggerFactory().getLogger(WatchdogAspect.class).error("error",error);
                }
            }
            // rethrow exception
            throw e;
        }

    }

    /**
     * Adds or enhances an mdc variable by call information.
     * @param traceeBackend the tracee backend
     * @param proceedingJoinPoint the aspectj calling context
     * @param annotatedId the id defined in the watchdog annotation
     */
    void writeMethodCallToMdc (final TraceeBackend traceeBackend, final ProceedingJoinPoint proceedingJoinPoint, final String annotatedId ) {

        final TraceeContextLoggerJsonBuilder errorJsonCreator = TraceeContextLoggerJsonBuilder.createJsonCreator()
                .addWatchdogCategory(WatchdogDataWrapper.wrap(annotatedId, proceedingJoinPoint));

        String json = errorJsonCreator.toString();
        String existingContent = traceeBackend.get(Constants.TRACEE_ATTRIBUTE_NAME);
        traceeBackend.put(Constants.TRACEE_ATTRIBUTE_NAME, existingContent != null ? existingContent + Constants.SEPARATOR + json : json);

    }

    /**
     * Sends the error reports to all connectors.
     * @param traceeBackend the tracee backend
     * @param proceedingJoinPoint the aspectj calling context
     * @param annotatedId the id defined in the watchdog annotation
     */
    void sendErrorReportToConnectors (final TraceeBackend traceeBackend, final ProceedingJoinPoint proceedingJoinPoint, final String annotatedId, final Throwable e ) {

        final TraceeContextLoggerJsonBuilder errorJsonCreator = TraceeContextLoggerJsonBuilder.createJsonCreator()
                .addPrefixedMessage("TRACEE WATCHDOG :")
                .addWatchdogCategory(WatchdogDataWrapper.wrap(annotatedId, proceedingJoinPoint))
                .addCommonCategory()
                .addExceptionCategory(e)
                .addTraceeCategory(traceeBackend);

        ConnectorFactory.sendErrorReportToConnectors(errorJsonCreator);

    }


    /**
     * Tries to get the watchdog annotation for the called method or the methods class.
     * @param proceedingJoinPoint The aspectj join point
     * @return The watchdog annotation of the method, the class or null if neither the method or class aren't annotated with the watchdog annotation
     */
     Watchdog getWatchdogAnnotation(final ProceedingJoinPoint proceedingJoinPoint) {

        // get watchdog annotation from method
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        Watchdog methodAnnotation = methodSignature.getMethod().getAnnotation(Watchdog.class);

        if (methodAnnotation != null) {
            return methodAnnotation;
        }

        // get watchdog annotation at class
        return (Watchdog) proceedingJoinPoint.getSignature().getDeclaringType().getAnnotation(Watchdog.class);

    }

    /**
     * Checks whether the passed Throwable must be suppressed.
     * @param proceedingJoinPoint The aspectj join point
     * @param thrownException The Throwable which must be looked for
     * @return true if Throwable was found and must be suppressed, otherwise false
     */
    boolean mustSuppressException(final ProceedingJoinPoint proceedingJoinPoint, Throwable thrownException) {

        // get watchdog annotation from method
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();

        Class[] classes = methodSignature.getMethod().getExceptionTypes();

        return checkClassIsDefinedInThrowsException(classes, thrownException);
    }

    /**
     * Checks whether the passed Throwable is defined included in passed classes array or is subtype of one of the included classes.
     * @param classes the classes to search
     * @param thrownException the Throwable which must be searched for
     * @return true if Throwable was found, otherwise false
     */
    boolean checkClassIsDefinedInThrowsException(Class[] classes, Throwable thrownException) {

        for (Class clazz : classes) {

            if (clazz.isInstance(thrownException)) {
                return true;
            }

        }

        return false;
    }


}
