package de.holisticon.util.tracee.backend.threadlocalstore;

import de.holisticon.util.tracee.MapLikeTraceeBackend;

import java.util.Map;

/**
 * @author Daniel
 */
public class ThreadLocalTraceeBackend extends MapLikeTraceeBackend {

    private final ThreadLocalMap threadLocalMap;

    public ThreadLocalTraceeBackend(ThreadLocalMap threadLocalMap) {
        this.threadLocalMap = threadLocalMap;
    }

    @Override
    public String get(String key) {
        return threadLocalMap.get().get(key);
    }

    @Override
    public boolean contains(String key) {
        return threadLocalMap.get().containsKey(key);
    }

    @Override
    public void putAll(Map<String, String> values) {
        threadLocalMap.get().putAll(values);
    }

    @Override
    protected void putInMap(String key, String value) {
        threadLocalMap.get().put(key,value);
    }

    @Override
    protected void removeFromMap(String key) {
        threadLocalMap.get().remove(key);
    }
}
