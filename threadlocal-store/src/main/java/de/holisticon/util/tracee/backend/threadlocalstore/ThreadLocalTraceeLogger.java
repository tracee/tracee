package de.holisticon.util.tracee.backend.threadlocalstore;

import de.holisticon.util.tracee.TraceeLogger;

/**
 * TraceeLogger Abstraction for ThreadLocal Logger.
 *
 * @author Tobias Gindler, holisticon AG
 */

final class ThreadLocalTraceeLogger implements TraceeLogger {

    private enum LEVEL {
        ERROR, WARN
    }

    private final Class<?> clazz;

    public ThreadLocalTraceeLogger(final Class<?> clazz) {
        this.clazz = clazz;
    }


    private void createLogEntry(final LEVEL level, final Object message) {
        this.createLogEntry(level, message, null);
    }

    private void createLogEntry(final LEVEL level, final Object message, final Throwable t) {

        final String tmpMessage = level.name()
                + " - (" + this.clazz.getCanonicalName() + ") :"
                + (message != null ? message.toString() : "");

        System.err.println(tmpMessage);
        if (t != null) {
            t.printStackTrace(System.err);
            System.err.println("");
        }
    }

    public void debug(final Object message) {
        // drop debug message
    }

    public void debug(final Object message, final Throwable t) {
        // drop debug message
    }

    public void error(final Object message) {
        this.createLogEntry(LEVEL.ERROR, message);
    }

    public void error(final Object message, final Throwable t) {
        this.createLogEntry(LEVEL.ERROR, message, t);
    }

    public void info(final Object message) {
        // drop info message
    }

    public void info(final Object message, final Throwable t) {
        // drop info message
    }

    public void warn(final Object message) {
        this.createLogEntry(LEVEL.WARN, message);
    }

    public void warn(final Object message, final Throwable t) {
        this.createLogEntry(LEVEL.WARN, message, t);
    }

}
