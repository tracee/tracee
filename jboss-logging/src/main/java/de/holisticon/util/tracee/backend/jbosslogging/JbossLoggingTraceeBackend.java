package de.holisticon.util.tracee.backend.jbosslogging;

import de.holisticon.util.tracee.MDCLike;
import de.holisticon.util.tracee.MDCLikeTraceeBackend;
import de.holisticon.util.tracee.TraceeLogger;
import de.holisticon.util.tracee.TraceeLoggerFactory;
import org.jboss.logging.MDC;

import java.util.Set;

/**
 * TraceeBackend provided using the jboss logging {@link MDC}.
 *
 * @author Daniel
 */
final class JbossLoggingTraceeBackend extends MDCLikeTraceeBackend {

    JbossLoggingTraceeBackend(MDCLike mdcAdapter, ThreadLocal<Set<String>> traceeKeys) {
        super(mdcAdapter, traceeKeys);
    }

    @Override
    public final TraceeLoggerFactory getLoggerFactory() {
		return new TraceeLoggerFactory() {
			@Override
			public TraceeLogger getLogger(Class<?> clazz) {
				return new JbossLoggingTraceeLogger(clazz);
			}
		};
    }
}
