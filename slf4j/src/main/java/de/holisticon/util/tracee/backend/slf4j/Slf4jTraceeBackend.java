package de.holisticon.util.tracee.backend.slf4j;

import de.holisticon.util.tracee.MapLikeTraceeBackend;
import org.slf4j.MDC;

/**
 * @author Daniel
 */
class Slf4jTraceeBackend extends MapLikeTraceeBackend {

    @Override
    public final String getFromMap(String key) {
        return MDC.get(key);
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
