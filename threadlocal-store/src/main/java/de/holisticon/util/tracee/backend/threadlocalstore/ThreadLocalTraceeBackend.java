package de.holisticon.util.tracee.backend.threadlocalstore;

import de.holisticon.util.tracee.MapLikeTraceeBackend;
import de.holisticon.util.tracee.TraceeLogger;

/**
 * @author Daniel
 */
public class ThreadLocalTraceeBackend extends MapLikeTraceeBackend {

    private final ThreadLocalMap threadLocalMap;

    public ThreadLocalTraceeBackend(final ThreadLocalMap threadLocalMap) {
        this.threadLocalMap = threadLocalMap;
    }

    @Override
    protected final String getFromMap(final String key) {
        return this.threadLocalMap.get().get(key);
    }

    @Override
    public final TraceeLogger getLogger(final Class<?> clazz) {
        return new ThreadLocalTraceeLogger(clazz);
    }

    @Override
    protected final void putInMap(final String key, final String value) {
        this.threadLocalMap.get().put(key, value);
    }

    @Override
    protected final void removeFromMap(final String key) {
        this.threadLocalMap.get().remove(key);
    }
}
