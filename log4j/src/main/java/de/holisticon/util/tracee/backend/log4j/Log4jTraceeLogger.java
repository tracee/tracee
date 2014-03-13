package de.holisticon.util.tracee.backend.log4j;

import de.holisticon.util.tracee.TraceeLogger;
import org.apache.log4j.Logger;

/**
 * TraceeLogger Abstraction for Log4j.
 *
 * @author Tobias Gindler, holisticon AG
 */

final class Log4jTraceeLogger implements TraceeLogger {

	private final Logger logger;

	public Log4jTraceeLogger(Logger logger) {
		this.logger = logger;
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
		this.logger.error(message, t);
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
