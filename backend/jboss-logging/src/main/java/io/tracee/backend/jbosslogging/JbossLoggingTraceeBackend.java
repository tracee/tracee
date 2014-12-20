package io.tracee.backend.jbosslogging;

import io.tracee.MDCLikeTraceeBackend;
import io.tracee.TraceeLogger;
import io.tracee.TraceeLoggerFactory;
import org.jboss.logging.Logger;
import org.jboss.logging.MDC;

import java.util.Set;

/**
 * TraceeBackend provided using the {@link org.jboss.logging.MDC}.
 */
final class JbossLoggingTraceeBackend extends MDCLikeTraceeBackend {

	JbossLoggingTraceeBackend(ThreadLocal<Set<String>> traceeKeys) {
		super(traceeKeys, new TraceeLoggerFactory() {
			@Override
			public TraceeLogger getLogger(Class<?> clazz) {
				return new JbossLoggingTraceeLogger(Logger.getLogger(clazz));
			}
		});
	}

	@Override
	protected String getFromMdc(String key) {
		final Object mdcValue = MDC.get(key);
		if (mdcValue == null) {
			return null;
		}
		return String.valueOf(mdcValue);
	}

	@Override
	protected void putToMdc(String key, String value) {
		MDC.put(key, value);
	}

	@Override
	protected void removeFromMdc(String key) {
		MDC.remove(key);
	}
}
