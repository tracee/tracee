package de.holisticon.util.tracee.contextlogger.data.wrapper;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * Wrapper class for ProceedingJointPoint. Decouples TraceeContextLoggerJsonBuilder class from AspectJ dependencies.
 * @author Tobias Gindler, holisticon AG
 */
public class WatchdogDataWrapper {

    final String annotatedId;
    final ProceedingJoinPoint proceedingJoinPoint;

    private WatchdogDataWrapper(final String annotatedId, final ProceedingJoinPoint proceedingJoinPoint) {
        this.annotatedId = annotatedId;
        this.proceedingJoinPoint = proceedingJoinPoint;
    }

    /**
     * Static method to create a WatchdogDataWrapper instance that wraps a ProceedingJoinPoint instance.
     * @param annotatedId the annotated id or null
     * @param proceedingJoinPoint the instance to wrap
     * @return the wrapper instance
     */
    public static WatchdogDataWrapper wrap(String annotatedId, final ProceedingJoinPoint proceedingJoinPoint) {
        return new WatchdogDataWrapper(annotatedId, proceedingJoinPoint);
    }

    /**
     * Getter for wrapped instance.
     * @return the wrapped instance
     */
    public ProceedingJoinPoint getProceedingJoinPoint() {
        return proceedingJoinPoint;
    }

    /**
     * Getter for the annotated id.
     * @return the annotated id
     */
    public String getAnnotatedId() {
        return annotatedId;
    }
}
