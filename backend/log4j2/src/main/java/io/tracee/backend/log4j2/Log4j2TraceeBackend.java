package io.tracee.backend.log4j2;

import io.tracee.MDCLikeTraceeBackend;
import io.tracee.TraceeLogger;
import io.tracee.TraceeLoggerFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.ThreadContext;

import java.util.Set;


final class Log4j2TraceeBackend extends MDCLikeTraceeBackend {

	Log4j2TraceeBackend(ThreadLocal<Set<String>> traceeKeys) {
		super(traceeKeys, new TraceeLoggerFactory() {
			@Override
			public TraceeLogger getLogger(Class<?> clazz) {
				return new Log4J2TraceeLogger(LogManager.getLogger(clazz));
			}
		});
	}

	@Override
	protected String getFromMdc(String key) {
		return ThreadContext.get(key);
	}

	@Override
	protected void putToMdc(String key, String value) {
		ThreadContext.put(key, value);
	}

	@Override
	protected void removeFromMdc(String key) {
		ThreadContext.remove(key);
	}
}
