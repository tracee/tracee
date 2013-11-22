package de.holisticon.util.tracee.backend.jbosslogging;

import de.holisticon.util.tracee.MapLikeTraceeBackend;
import org.jboss.logging.MDC;

import java.util.Map;

/**
 * TraceeBackend provided using the jboss logging {@link MDC}.
 *
 * @author Daniel
 */
class JbossLoggingTraceeBackend extends MapLikeTraceeBackend {



    @Override
    public String getFromMap(String key) {
        final Object obj = MDC.get(key);
        return obj == null? null : obj.toString();
    }

    @Override
    public boolean mapContains(String key) {
        return MDC.get(key) != null;
    }


    @Override
    protected void putInMap(String key, String value) {
        MDC.put(key,value);
    }

    @Override
    protected void removeFromMap(String key) {
        MDC.remove(key);
    }
}
