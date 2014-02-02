package de.holisticon.util.tracee.backend.jbosslogging;

import de.holisticon.util.tracee.MDCLike;
import de.holisticon.util.tracee.MDCLikeTraceeBackend;
import de.holisticon.util.tracee.TraceeLogger;
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
    public final TraceeLogger getLogger(Class<?> clazz) {
        return new JbossLoggingTraceeLogger(clazz);
    }
}
