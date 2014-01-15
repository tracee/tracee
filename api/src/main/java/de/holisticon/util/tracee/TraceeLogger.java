package de.holisticon.util.tracee;

/**
 * Abstraction interface for tracee logging.
 * It will resolve to the underlying logging system.
 *
 * @author Tobias Gindler, holisticon AG
 */
public interface TraceeLogger {
    void debug(final Object message);

    void debug(final Object message, final Throwable t);

    void error(final Object message);

    void error(final Object message, final Throwable t);

    void info(final Object message);

    void info(final Object message, final Throwable t);

    void warn(final Object message);

    void warn(final Object message, final Throwable t);
}
