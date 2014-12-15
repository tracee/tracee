package io.tracee.backend.slf4j;

import io.tracee.TraceeLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TraceeLogger Abstraction for SLF4J.
 */
final class Slf4jTraceeLogger implements TraceeLogger {

    private final Logger logger;

    public Slf4jTraceeLogger(final Class<?> clazz) {
        this(LoggerFactory.getLogger(clazz));
    }

	Slf4jTraceeLogger(final Logger logger) {
		this.logger = logger;
	}

    public void debug(Object message) {
        logger.debug(nullsafeString(message));
    }

    public void debug(Object message, Throwable t) {
        logger.debug(nullsafeString(message), t);
    }

    public void error(Object message) {
        logger.error(nullsafeString(message));
    }

    public void error(Object message, Throwable t) {
        logger.error(nullsafeString(message), t);
    }

    public void info(Object message) {
        logger.info(nullsafeString(message));
    }

    public void info(Object message, Throwable t) {
        logger.info(nullsafeString(message), t);
    }

    public void warn(Object message) {
        logger.warn(nullsafeString(message));
    }

	public void warn(Object message, Throwable t) {
        logger.warn(nullsafeString(message), t);
    }

	private String nullsafeString(Object message) {
		return message != null ? message.toString() : "";
	}
}
