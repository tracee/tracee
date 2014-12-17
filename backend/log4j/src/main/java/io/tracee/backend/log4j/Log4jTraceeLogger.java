package io.tracee.backend.log4j;

import io.tracee.TraceeLogger;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

/**
 * TraceeLogger Abstraction for Log4j.
 */
final class Log4jTraceeLogger implements TraceeLogger {

	private final Logger logger;

	public Log4jTraceeLogger(Logger logger) {
		this.logger = logger;
	}

	public void debug(final String message) {
		this.logger.debug(message);
	}

	public void debug(final String message, final Throwable t) {
		this.logger.debug(message, t);
	}

	@Override
	public boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}

	public void error(final String message) {
		this.logger.error(message);
	}

	public void error(final String message, final Throwable t) {
		this.logger.error(message, t);
	}

	@Override
	public boolean isErrorEnabled() {
		return logger.isEnabledFor(Priority.ERROR);
	}

	public void info(final String message) {
		this.logger.info(message);
	}

	public void info(final String message, final Throwable t) {
		this.logger.info(message, t);
	}

	@Override
	public boolean isInfoEnabled() {
		return logger.isInfoEnabled();
	}

	public void warn(final String message) {
		this.logger.warn(message);
	}

	public void warn(final String message, final Throwable t) {
		this.logger.warn(message, t);
	}

	@Override
	public boolean isWarnEnabled() {
		return logger.isEnabledFor(Priority.WARN);
	}


}
