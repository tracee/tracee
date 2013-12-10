package de.holisticon.util.tracee.backend.slf4j;

import de.holisticon.util.tracee.MapLikeTraceeBackend;
import de.holisticon.util.tracee.TraceeLogger;
import org.slf4j.MDC;

/**
 * @author Daniel
 */
class Slf4jTraceeBackend extends MapLikeTraceeBackend {

    @Override
    public final String getFromMap(final String key) {
        return MDC.get(key);
    }

    @Override
    public final TraceeLogger getLogger(final Class<?> clazz) {
        return new Slf4jTraceeLogger(clazz);
    }

    @Override
    protected final void putInMap(final String key, final String value) {
        MDC.put(key, value);
    }

    @Override
    protected final void removeFromMap(final String key) {
        MDC.remove(key);
    }
}
