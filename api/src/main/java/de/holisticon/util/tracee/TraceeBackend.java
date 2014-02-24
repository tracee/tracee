package de.holisticon.util.tracee;

import de.holisticon.util.tracee.configuration.TraceeFilterConfiguration;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * A backend is expected to be thread-safe (reads and writes are delegated to thread local state).
 *
 * @author Daniel Wegener (Holisticon AG)
 */
public interface TraceeBackend extends Map<String,String> {


	TraceeFilterConfiguration getConfiguration();

    @Override
    Collection<String> values();

    @Override
    boolean containsValue(Object value);

    @Override
    int size();

    /**
     * Gets a logger proxy that delegates to a concrete logging framework at runtime. Should only be used in tracee
     * adapters.
     */
    TraceeLoggerFactory getLoggerFactory();

    /**
     * Clears all context information from this backend.
     */
    @Override
    void clear();

    /**
     * @param key a non-null identifier.
     * @return {@code true} if this backend contains an entry for the given key. {@code false} otherwise.
     */
    @Override
    boolean containsKey(Object key);

    /**
     * Gets the value for a given key from this backend.
     * @param key a non-null identifier.
     * @return the stored value or {@code null} if not present.
     */
    @Override
    String get(Object key);

    /**
     * @return a Set of keys that are currently carried backend. The returned Collection is a copy and may be modified.
     */
    @Override
    Set<String> keySet();

    /**
     * @return {@code true} if this backend contains no context information, {@code false} otherwise.
     */
    @Override
    boolean isEmpty();

    /**
     * @return a view over all
     */
    @Override
    Set<Map.Entry<String, String>> entrySet();

    /**
     * Puts a key into this backend.
     * @param key   ignored if {@code null}
     * @param value ignored if {@code null}
     */
    @Override
    String put(String key, String value);

    @Override
    void putAll(Map<? extends String, ? extends String> m);

    /**
     * Removes the entry with the given key from this backend. Does nothing if the key is {@code null} or not found.
     * @param key ignored if {@code null}
     */
    @Override
    String remove(Object key);

}
