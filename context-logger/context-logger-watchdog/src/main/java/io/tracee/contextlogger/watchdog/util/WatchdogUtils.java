package io.tracee.contextlogger.watchdog.util;

import io.tracee.contextlogger.watchdog.Watchdog;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * Utility class for watchdog aspect
 * Created by Tobias Gindler, holisticon AG on 03.04.14.
 */
public final class WatchdogUtils {

    private WatchdogUtils() {

    }

    /**
     * Tries to get the watchdog annotation for the called method or the methods class.
     *
     * @param proceedingJoinPoint The aspectj join point
     * @return The watchdog annotation of the method, the class or null if neither the method or class aren't annotated with the watchdog annotation
     */
    public static Watchdog getWatchdogAnnotation(final ProceedingJoinPoint proceedingJoinPoint) {

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
     * Checks whether the passed Throwable is contained in methods throws part.
     *
     * @param proceedingJoinPoint The aspectj join point
     * @param thrownException     The Throwable which must be looked for
     * @return true if Throwable was found and must be suppressed, otherwise false
     */
    public static boolean checkIfMethodThrowsContainsPassedException(final ProceedingJoinPoint proceedingJoinPoint, Throwable thrownException) {

        if (proceedingJoinPoint == null || thrownException == null) {
            return false;
        }

        Class[] throwsClassesFromMethodSignature = getDefinedThrowsFromMethodSignature(proceedingJoinPoint);
        return checkClassIsDefinedInThrowsException(throwsClassesFromMethodSignature, thrownException);
    }

    /**
     * Checks whether the passed Throwable is defined included in passed classes array or is subtype of one of the included classes.
     *
     * @param classes         the classes to search
     * @param thrownException the Throwable which must be searched for
     * @return true if Throwable was found, otherwise false
     */
    public static boolean checkClassIsDefinedInThrowsException(Class[] classes, Throwable thrownException) {

        // return false if either passed classes array or thrownException are null.
        if (classes == null || thrownException == null) {
            return false;
        }

        // loop through classes array to check for matching type
        for (Class clazz : classes) {

            if (clazz.isInstance(thrownException)) {
                return true;
            }

        }

        return false;
    }

    /**
     * Gets all Exceptions declared at the throws part of the method signature.
     * @param proceedingJoinPoint the proceeding join point to get the method signature from.
     * @return All defined exceptions that must be caught (are defined in the method signature in the throws part)
     */
    public static Class[] getDefinedThrowsFromMethodSignature(final ProceedingJoinPoint proceedingJoinPoint) {

        if (proceedingJoinPoint == null) {
            return new Class[0];
        }

        // get watchdog annotation from method
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();

        return methodSignature.getMethod().getExceptionTypes();
    }


    /**
     * Checks whether the exception should be processed or not.
     * @param watchdogAnnotation the watchdog annotation to check.
     * @return true, if passed watchdogAnnotation is not null and not disabled vie system properties, otherwise false.
     */
    public static boolean checkProcessWatchdog(final Watchdog watchdogAnnotation, final ProceedingJoinPoint proceedingJoinPoint, final Throwable throwable) {
        // check if watchdog aspect processing is deactivated by annotation
        if (watchdogAnnotation != null && watchdogAnnotation.isActive()) {

            // checks if throws annotations must be suppressed
            boolean throwableIsPartOfThrowsDeclaration = WatchdogUtils.checkIfMethodThrowsContainsPassedException(proceedingJoinPoint, throwable);

            if (!watchdogAnnotation.suppressThrowsExceptions() || (watchdogAnnotation.suppressThrowsExceptions() && !throwableIsPartOfThrowsDeclaration)) {

                return true;

            }

        }

        return false;
    }

}
