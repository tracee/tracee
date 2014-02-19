package de.holisticon.util.tracee.contextlogger.json.generator.datawrapper;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * Wrapper class for ProceedingJointPoint. Decouples TraceeContextLoggerJsonCreator class from AspectJ dependencies.
 * @author Tobias Gindler, holisticon AG
 */
public class WatchdogDataWrapper {

    final ProceedingJoinPoint proceedingJoinPoint;

    private WatchdogDataWrapper(final ProceedingJoinPoint proceedingJoinPoint) {
        this.proceedingJoinPoint = proceedingJoinPoint;
    }

    /**
     * Static method to create a WatchdogDataWrapper instance that wraps a ProceedingJoinPoint instance.
     * @param proceedingJoinPoint the instance to wrap
     * @return the wrapper instance
     */
    public static WatchdogDataWrapper wrap(final ProceedingJoinPoint proceedingJoinPoint) {
        return new WatchdogDataWrapper(proceedingJoinPoint);
    }

    /**
     * Getter for wrapped instance.
     * @return the wrapped instance
     */
    public ProceedingJoinPoint getProceedingJoinPoint() {
        return proceedingJoinPoint;
    }

}
