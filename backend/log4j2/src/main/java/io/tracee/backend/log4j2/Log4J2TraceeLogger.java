package io.tracee.backend.log4j2;

import io.tracee.TraceeLogger;
import org.apache.logging.log4j.Logger;


/**
 * TraceeLogger Abstraction for Log4J2.
 */
final class Log4J2TraceeLogger implements TraceeLogger {

	private final Logger logger;

	public Log4J2TraceeLogger(Logger logger) {
		this.logger = logger;
	}

	public void debug(final Object message) {
		logger.debug(message);
	}

	public void debug(final Object message, final Throwable t) {
		logger.debug(message, t);
	}

	@Override
	public boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}

	public void error(final Object message) {
		logger.error(message);
	}

	public void error(final Object message, final Throwable t) {
		logger.error(message, t);
	}

	@Override
	public boolean isErrorEnabled() {
		return logger.isErrorEnabled();
	}

	public void info(final Object message) {
		logger.info(message);
	}

	public void info(final Object message, final Throwable t) {
		logger.info(message, t);
	}

	@Override
	public boolean isInfoEnabled() {
		return logger.isInfoEnabled();
	}

	public void warn(final Object message) {
		logger.warn(message);
	}

	public void warn(final Object message, final Throwable t) {
		logger.warn(message, t);
	}

	@Override
	public boolean isWarnEnabled() {
		return logger.isWarnEnabled();
	}


}
