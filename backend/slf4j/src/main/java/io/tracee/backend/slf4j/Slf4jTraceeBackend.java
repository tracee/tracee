package io.tracee.backend.slf4j;

import io.tracee.MDCLikeTraceeBackend;
import io.tracee.TraceeLogger;
import io.tracee.TraceeLoggerFactory;
import org.slf4j.MDC;

import java.util.Set;

class Slf4jTraceeBackend extends MDCLikeTraceeBackend {

	Slf4jTraceeBackend(ThreadLocal<Set<String>> traceeKeys) {
		super(traceeKeys, new TraceeLoggerFactory() {
			@Override
			public TraceeLogger getLogger(Class<?> clazz) {
				return new Slf4jTraceeLogger(clazz);
			}
		});
	}

	@Override
	protected String getFromMdc(String key) {
		return MDC.get(key);
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
