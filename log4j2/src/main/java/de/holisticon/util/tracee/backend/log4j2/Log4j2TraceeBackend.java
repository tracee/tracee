package de.holisticon.util.tracee.backend.log4j2;

import de.holisticon.util.tracee.MDCLike;
import de.holisticon.util.tracee.MDCLikeTraceeBackend;
import de.holisticon.util.tracee.TraceeLogger;
import de.holisticon.util.tracee.TraceeLoggerFactory;
import org.apache.logging.log4j.LogManager;

import java.util.Set;


/**
 * @author Daniel Wegener (Holisticon AG)
 */
final class Log4j2TraceeBackend extends MDCLikeTraceeBackend {

	Log4j2TraceeBackend(MDCLike mdcAdapter, ThreadLocal<Set<String>> traceeKeys) {
		super(mdcAdapter, traceeKeys);
	}

	@Override
	public TraceeLoggerFactory getLoggerFactory() {
		return new TraceeLoggerFactory() {
			@Override
			public TraceeLogger getLogger(Class<?> clazz) {
				return new Log4J2TraceeLogger(LogManager.getLogger(clazz));
			}
		};
	}

}
