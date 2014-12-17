package io.tracee.backend.threadlocalstore;

import io.tracee.TraceeLogger;

/**
 * TraceeLogger Abstraction for ThreadLocal Logger.
 */
final class ThreadLocalTraceeLogger implements TraceeLogger {

    enum LEVEL {
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
		System.err.println(buildLogString(level, message));
        if (t != null) {
            t.printStackTrace(System.err);
            System.err.println();
        }
    }

	String buildLogString(LEVEL level, Object message) {
		return level.name()	+ " - (" + this.clazz.getCanonicalName() + ") :"
					+ (message != null ? message.toString() : "");
	}

	public void debug(final Object message) {
        // drop debug message
    }

    public void debug(final Object message, final Throwable t) {
        // drop debug message
    }

	@Override
	public boolean isDebugEnabled() {
		return false;
	}

	public void error(final Object message) {
        this.createLogEntry(LEVEL.ERROR, message);
    }

    public void error(final Object message, final Throwable t) {
        this.createLogEntry(LEVEL.ERROR, message, t);
    }

	@Override
	public boolean isErrorEnabled() {
		return true;
	}

    public void info(final Object message) {
        // drop info message
    }

    public void info(final Object message, final Throwable t) {
        // drop info message
    }

	@Override
	public boolean isInfoEnabled() {
		return false;
	}

    public void warn(final Object message) {
        this.createLogEntry(LEVEL.WARN, message);
    }

    public void warn(final Object message, final Throwable t) {
        this.createLogEntry(LEVEL.WARN, message, t);
    }

	@Override
	public boolean isWarnEnabled() {
		return true;
	}
}
