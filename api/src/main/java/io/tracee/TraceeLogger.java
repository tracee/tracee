package io.tracee;

/**
 * Abstraction interface for tracee logging.
 * It will resolve to the underlying logging system.
 */
public interface TraceeLogger {
    void debug(final String message);

    void debug(final String message, final Throwable t);

	boolean isDebugEnabled();

    void error(final String message);

    void error(final String message, final Throwable t);

	boolean isErrorEnabled();

    void info(final String message);

    void info(final String message, final Throwable t);

	boolean isInfoEnabled();

    void warn(final String message);

    void warn(final String message, final Throwable t);

	boolean isWarnEnabled();
}
