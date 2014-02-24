package de.holisticon.util.tracee.backend.threadlocalstore;

import de.holisticon.util.tracee.MDCLike;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
class MdcLikeThreadLocalMapAdapter implements MDCLike {

    private final ThreadLocalMap<String, String> threadLocalMap;

    public MdcLikeThreadLocalMapAdapter(ThreadLocalMap<String, String> threadLocalMap) {
        this.threadLocalMap = threadLocalMap;
    }


    @Override
    public boolean containsKey(String key) {
        return threadLocalMap.get().containsKey(key);
    }

    @Override
    public void put(String key, String value) {
        threadLocalMap.get().put(key, value);
    }

    @Override
    public String get(String key) {
        return threadLocalMap.get().get(key);
    }

    @Override
    public void remove(String key) {
        threadLocalMap.get().remove(key);
    }

    @Override
    public Map<String, String> getCopyOfContext() {
        return new HashMap<String, String>(threadLocalMap.get());
    }
}
