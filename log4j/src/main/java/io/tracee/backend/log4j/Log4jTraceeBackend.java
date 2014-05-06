package io.tracee.backend.log4j;

import io.tracee.MDCLike;
import io.tracee.MDCLikeTraceeBackend;
import io.tracee.TraceeLogger;
import io.tracee.TraceeLoggerFactory;
import org.apache.log4j.Logger;

import java.util.Set;

final class Log4jTraceeBackend extends MDCLikeTraceeBackend {

	Log4jTraceeBackend(MDCLike mdcAdapter, ThreadLocal<Set<String>> traceeKeys) {
		super(mdcAdapter, traceeKeys, new TraceeLoggerFactory() {
			@Override
			public TraceeLogger getLogger(Class<?> clazz) {
				return new Log4jTraceeLogger(Logger.getLogger(clazz));
			}
		});
	}

}
