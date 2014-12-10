package io.tracee.backend.slf4j;

import io.tracee.MDCLike;
import io.tracee.MDCLikeTraceeBackend;
import io.tracee.TraceeLogger;
import io.tracee.TraceeLoggerFactory;

import java.util.Set;

class Slf4jTraceeBackend extends MDCLikeTraceeBackend {

    Slf4jTraceeBackend(MDCLike mdcAdapter, ThreadLocal<Set<String>> traceeKeys) {
        super(mdcAdapter, traceeKeys, new TraceeLoggerFactory() {
			@Override
			public TraceeLogger getLogger(Class<?> clazz) {
				return new Slf4jTraceeLogger(clazz);
			}
		});
    }

}
