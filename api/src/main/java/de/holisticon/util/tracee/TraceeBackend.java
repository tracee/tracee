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

    /**
     * @param key a non-null identifier.
     * @return {@code true} if this backend contains an entry for the given key. {@code false} otherwise.
     */
    boolean contains(String key);

    /**
     * @return a map (copy) of all entries within this TraceeBackend
     */
    TreeMap<String, String> extractContext();

    /**
     * Gets the value for a given key from this backend.
     * @param key a non-null identifier.
     * @return the stored value or {@code null} if not present.
     */
    String get(String key);

    /**
     * Gets a logger proxy that delegates to a concrete logging framework at runtime. Should only be used in tracee
     * adapters.
     * @param clazz the returned logger will be named after clazz
     */
    TraceeLogger getLogger(Class<?> clazz);

    /**
     * @return a collection of keys withing this backend. The returned Collection is a copy and may be modified.
     */
    Collection<String> getRegisteredKeys();

    /**
     * @return {@code true} if this backend contains no context information, {@code false} otherwise.
     */
    boolean isEmpty();


    /**
     * Puts a key into this backend.
     * @param key   ignored if {@code null}
     * @param value ignored if {@code null}
     */
    void put(String key, String value);

    /**
     * Puts all values from the given Map into this backend. Entries with {@code null} values will be ignored.
     */
    void putAll(Map<String, String> values);

    /**
     * Removes the entry with the given key from this backend. Does nothing if the key is {@code null} or not found.
     * @param key ignored if {@code null}
     */
    void remove(String key);

}
