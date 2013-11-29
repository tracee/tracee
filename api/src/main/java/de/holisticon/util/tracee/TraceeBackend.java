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
     * @return {@code true} if this backend contains no context information, {@code false} otherwise.
     */
    boolean isEmpty();

    /**
     * Clears all context information from this backend.
     */
    void clear();

    Collection<String> getRegisteredKeys();

    /**
     * @param key   ignored if <code>null</code>
     * @param value ignored if <code>null</code>
     */
    void put(String key, String value);

    void remove(String key);

    String get(String key);

    boolean contains(String key);


    /**
     * @return a map (copy) of all entries within this TraceeBackend
     */
    TreeMap<String, String> extractContext();

    void putAll(Map<String, String> values);

}
