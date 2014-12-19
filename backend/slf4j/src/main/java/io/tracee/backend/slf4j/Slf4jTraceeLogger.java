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

    public void debug(String message) {
        logger.debug(nullsafeString(message));
    }

    public void debug(String message, Throwable t) {
        logger.debug(nullsafeString(message), t);
    }

	@Override
	public boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}

	public void error(String message) {
        logger.error(nullsafeString(message));
    }

    public void error(String message, Throwable t) {
        logger.error(nullsafeString(message), t);
    }

	@Override
	public boolean isErrorEnabled() {
		return logger.isErrorEnabled();
	}

    public void info(String message) {
        logger.info(nullsafeString(message));
    }

    public void info(String message, Throwable t) {
        logger.info(nullsafeString(message), t);
    }

	@Override
	public boolean isInfoEnabled() {
		return logger.isInfoEnabled();
	}

    public void warn(String message) {
        logger.warn(nullsafeString(message));
    }

	public void warn(String message, Throwable t) {
        logger.warn(nullsafeString(message), t);
    }

	@Override
	public boolean isWarnEnabled() {
		return logger.isWarnEnabled();
	}

	private String nullsafeString(String message) {
		return message != null ? message : "";
	}
}
