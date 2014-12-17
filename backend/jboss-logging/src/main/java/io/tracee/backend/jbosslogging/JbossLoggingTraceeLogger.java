package io.tracee.backend.jbosslogging;

import io.tracee.TraceeLogger;
import org.jboss.logging.Logger;

/**
 * TraceeLogger abstraction for Jboss Logging.
 */
final class JbossLoggingTraceeLogger implements TraceeLogger {

    private final Logger logger;

    public JbossLoggingTraceeLogger(Logger logger) {
        this.logger = logger;
    }

    public void debug(final String message) {
        logger.debug(message);
    }

    public void debug(final String message, final Throwable t) {
        logger.debug(message, t);
    }

	@Override
	public boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}

	public void error(final String message) {
        this.logger.error(message);
    }

    public void error(final String message, final Throwable t) {
        logger.error(message, t);
    }

	@Override
	public boolean isErrorEnabled() {
		return logger.isEnabled(Logger.Level.ERROR);
	}

    public void info(final String message) {
        logger.info(message);
    }

    public void info(final String message, final Throwable t) {
        logger.info(message, t);
    }

	@Override
	public boolean isInfoEnabled() {
		return logger.isInfoEnabled();
	}

    public void warn(final String message) {
        logger.warn(message);
    }

    public void warn(final String message, final Throwable t) {
        logger.warn(message, t);
    }

	@Override
	public boolean isWarnEnabled() {
		return logger.isEnabled(Logger.Level.WARN);
	}
}
