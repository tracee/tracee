package de.holisticon.util.tracee.backend.log4j;

import org.apache.log4j.Logger;

import de.holisticon.util.tracee.TraceeLogger;

/**
 * TraceeLogger Abstraction for Log4J.
 *
 * @author Tobias Gindler, holisticon AG
 */

public final class Log4JTraceeLogger implements TraceeLogger {

    private final Logger logger;

    public Log4JTraceeLogger(final Class<?> clazz) {
        this.logger = Logger.getLogger(clazz);
    }

    public void debug(final Object message) {
        this.logger.debug(message);
    }

    public void debug(final Object message, final Throwable t) {
        this.logger.debug(message, t);
    }

    public void error(final Object message) {
        this.logger.error(message);
    }

    public void error(final Object message, final Throwable t) {
        this.logger.debug(message, t);
    }

    public void info(final Object message) {
        this.logger.info(message);
    }

    public void info(final Object message, final Throwable t) {
        this.logger.info(message, t);
    }

    public void warn(final Object message) {
        this.logger.warn(message);
    }

    public void warn(final Object message, final Throwable t) {
        this.logger.warn(message, t);
    }


}
