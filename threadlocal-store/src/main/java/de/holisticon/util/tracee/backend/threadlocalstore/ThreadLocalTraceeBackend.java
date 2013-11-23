package de.holisticon.util.tracee.backend.threadlocalstore;

import de.holisticon.util.tracee.MapLikeTraceeBackend;

/**
 * @author Daniel
 */
public class ThreadLocalTraceeBackend extends MapLikeTraceeBackend {

    private final ThreadLocalMap threadLocalMap;

    public ThreadLocalTraceeBackend(ThreadLocalMap threadLocalMap) {
        this.threadLocalMap = threadLocalMap;
    }

    @Override
    protected final String getFromMap(String key) {
        return threadLocalMap.get().get(key);
    }

    @Override
    protected final boolean mapContains(String key) {
        return threadLocalMap.get().containsKey(key);
    }

    @Override
    protected final void putInMap(String key, String value) {
        threadLocalMap.get().put(key, value);
    }

    @Override
    protected final void removeFromMap(String key) {
        threadLocalMap.get().remove(key);
    }
}
