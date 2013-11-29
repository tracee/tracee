package de.holisticon.util.tracee.backend.jbosslogging;

import de.holisticon.util.tracee.MapLikeTraceeBackend;
import org.jboss.logging.MDC;

/**
 * TraceeBackend provided using the jboss logging {@link MDC}.
 *
 * @author Daniel
 */
class JbossLoggingTraceeBackend extends MapLikeTraceeBackend {


    @Override
    public final String getFromMap(String key) {
        final Object obj = MDC.get(key);
        return obj == null ? null : obj.toString();
    }

    @Override
    protected final void putInMap(String key, String value) {
        MDC.put(key, value);
    }

    @Override
    protected final void removeFromMap(String key) {
        MDC.remove(key);
    }
}
