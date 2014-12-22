package io.tracee.backend.log4j;

import io.tracee.MDCLikeTraceeBackend;
import io.tracee.TraceeLogger;
import io.tracee.TraceeLoggerFactory;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;

import java.util.Set;

final class Log4jTraceeBackend extends MDCLikeTraceeBackend {

	Log4jTraceeBackend(ThreadLocal<Set<String>> traceeKeys) {
		super(traceeKeys, new TraceeLoggerFactory() {
			@Override
			public TraceeLogger getLogger(Class<?> clazz) {
				return new Log4jTraceeLogger(Logger.getLogger(clazz));
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
