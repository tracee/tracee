package io.tracee.backend.jbosslogging;

import io.tracee.MDCLike;
import io.tracee.MDCLikeTraceeBackend;
import io.tracee.TraceeLogger;
import io.tracee.TraceeLoggerFactory;
import org.jboss.logging.Logger;

import java.util.Set;

/**
 * TraceeBackend provided using the {@link org.jboss.logging.MDC}.
 */
final class JbossLoggingTraceeBackend extends MDCLikeTraceeBackend {

    JbossLoggingTraceeBackend(MDCLike mdcAdapter, ThreadLocal<Set<String>> traceeKeys) {
        super(mdcAdapter, traceeKeys, new TraceeLoggerFactory() {
			@Override
			public TraceeLogger getLogger(Class<?> clazz) {
				return new JbossLoggingTraceeLogger(Logger.getLogger(clazz));
			}
		});
    }

}
