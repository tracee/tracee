package io.tracee;

/**
 * Abstraction interface for tracee logging.
 * It will resolve to the underlying logging system.
 */
public interface TraceeLogger {
    void debug(final Object message);

    void debug(final Object message, final Throwable t);

	boolean isDebugEnabled();

    void error(final Object message);

    void error(final Object message, final Throwable t);

	boolean isErrorEnabled();

    void info(final Object message);

    void info(final Object message, final Throwable t);

	boolean isInfoEnabled();

    void warn(final Object message);

    void warn(final Object message, final Throwable t);

	boolean isWarnEnabled();
}
