package de.holisticon.util.tracee.backend.slf4j;

import de.holisticon.util.tracee.MapLikeTraceeBackend;
import de.holisticon.util.tracee.TraceeBackend;
import org.slf4j.MDC;

import java.util.Collections;

/**
 * @author Daniel
 */
class Slf4jTraceeBackend extends MapLikeTraceeBackend {

    @Override
    public String get(String key) {
        return MDC.get(key);
    }

    @Override
    public boolean contains(String key) {
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
