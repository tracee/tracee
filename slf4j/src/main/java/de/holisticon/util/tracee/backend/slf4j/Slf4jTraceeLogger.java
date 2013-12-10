package de.holisticon.util.tracee.backend.slf4j;

import de.holisticon.util.tracee.TraceeLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TraceeLogger Abstraction for SLF4J.
 *
 * @author Tobias Gindler, holisticon AG
 */

public final class Slf4jTraceeLogger implements TraceeLogger {

    private final Logger logger;

    public Slf4jTraceeLogger(final Class<?> clazz) {
        this.logger = LoggerFactory.getLogger(clazz);
    }

    public void debug(Object message) {
        logger.debug(message != null ? message.toString() : "");
    }

    public void debug(Object message, Throwable t) {
        logger.debug(message != null ? message.toString() : "", t);
    }

    public void error(Object message) {
        logger.error(message != null ? message.toString() : "");
    }

    public void error(Object message, Throwable t) {
        logger.error(message != null ? message.toString() : "", t);
    }

    public void info(Object message) {
        logger.info(message != null ? message.toString() : "");
    }

    public void info(Object message, Throwable t) {
        logger.info(message != null ? message.toString() : "", t);
    }

    public void warn(Object message) {
        logger.warn(message != null ? message.toString() : "");
    }

    public void warn(Object message, Throwable t) {
        logger.warn(message != null ? message.toString() : "", t);
    }
}
