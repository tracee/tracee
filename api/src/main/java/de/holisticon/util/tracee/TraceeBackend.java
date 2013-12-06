package de.holisticon.util.tracee;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

/**
 * A backend is expected to be thread local.
 *
 * @author Daniel Wegener (Holisticon AG)
 */
public interface TraceeBackend {

    /**
     * Clears all context information from this backend.
     */
    void clear();

    boolean contains(String key);

    /**
     * @return a map (copy) of all entries within this TraceeBackend
     */
    TreeMap<String, String> extractContext();

    String get(String key);

    TraceeLogger getLogger(Class<?> clazz);

    Collection<String> getRegisteredKeys();

    /**
     * @return {@code true} if this backend contains no context information, {@code false} otherwise.
     */
    boolean isEmpty();


    /**
     * @param key   ignored if <code>null</code>
     * @param value ignored if <code>null</code>
     */
    void put(String key, String value);

    void putAll(Map<String, String> values);

    void remove(String key);

}
