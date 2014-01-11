package de.holisticon.util.tracee;

import java.util.*;


/**
 * @author Daniel Wegener (Holisticon AG)
 */
public abstract class MapLikeTraceeBackend implements TraceeBackend {

    public static final String REGISTERED_KEYS_KEY = "de.holisticon.util.tracee.spi.TraceeBackend.REGISTERED_KEYS";


    @Override
    public final boolean contains(String key) {
        return getRegisteredKeys().contains(key);
    }

    @Override
    public final boolean isEmpty() {
        return getFromMap(REGISTERED_KEYS_KEY) == null;
    }

    @Override
    public final String get(String key) {
        if (!getRegisteredKeys().contains(key)) return null;
        return getFromMap(key);
    }

    @Override
    public final Collection<String> getRegisteredKeys() {
        final String serializedKeys = getFromMap(REGISTERED_KEYS_KEY);
        if (serializedKeys == null) return new ArrayList<String>();
        return deserialize(serializedKeys);
    }

    @Override
    public final void put(String key, String value) {
        if (value == null || key == null) return;
        final Collection<String> registeredKeys = getRegisteredKeys();
        if (!registeredKeys.contains(key)) {
            registeredKeys.add(key);
            setRegisteredKeys(registeredKeys);
        }
        putInMap(key, value);
    }

    @Override
    public final void remove(String key) {
        final Collection<String> registeredKeys = getRegisteredKeys();
        registeredKeys.remove(key);
        if (registeredKeys.isEmpty())
            removeFromMap(REGISTERED_KEYS_KEY);
        removeFromMap(key);
    }

    @Override
    public final void clear() {
        for (String s : getRegisteredKeys()) {
            removeFromMap(s);
        }
        removeFromMap(REGISTERED_KEYS_KEY);
    }


    protected abstract void putInMap(String key, String value);

    protected abstract void removeFromMap(String key);

    protected abstract String getFromMap(String key);


    public final void putAll(Map<String, String> entries) {
        for (Map.Entry<String, String> entry : entries.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }


    private void setRegisteredKeys(Collection<String> registeredKeys) {
        if (registeredKeys.isEmpty()) {
            removeFromMap(REGISTERED_KEYS_KEY);
        } else {
            putInMap(REGISTERED_KEYS_KEY, serialize(registeredKeys));
        }
    }

    private String serialize(Collection<String> registeredKeys) {
        final StringBuilder serializedBuilder = new StringBuilder();
        boolean first = true;
        for (String registeredKey : registeredKeys) {
            if (!first) serializedBuilder.append(',');
            first = false;
            serializedBuilder.append(registeredKey);
        }
        return serializedBuilder.toString();
    }

    private Collection<String> deserialize(String serialized) {
        final String[] split = serialized.trim().split(",", 0);
        List<String> list = new ArrayList<String>();
        for (String element : split) {
            list.add(element);
        }
        return list;
    }

    @Override
    public final TreeMap<String, String> extractContext() {
        final TreeMap<String, String> traceeContext = new TreeMap<String, String>();
        for (String s : getRegisteredKeys()) {
            traceeContext.put(s, getFromMap(s));
        }
        return traceeContext;
    }
}
