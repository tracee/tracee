package io.tracee.backend.log4j2;

import io.tracee.MDCLike;
import io.tracee.MDCLikeTraceeBackend;
import io.tracee.TraceeLogger;
import io.tracee.TraceeLoggerFactory;
import org.apache.logging.log4j.LogManager;

import java.util.Set;


final class Log4j2TraceeBackend extends MDCLikeTraceeBackend {

	Log4j2TraceeBackend(MDCLike mdcAdapter, ThreadLocal<Set<String>> traceeKeys) {
		super(mdcAdapter, traceeKeys, new TraceeLoggerFactory() {
			@Override
			public TraceeLogger getLogger(Class<?> clazz) {
				return new Log4J2TraceeLogger(LogManager.getLogger(clazz));
			}
		});
	}

}
