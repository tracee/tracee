package de.holisticon.util.tracee;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
public abstract class MapLikeTraceeBackend implements TraceeBackend {

    public static final String REGISTERED_KEYS_KEY = "de.holisticon.util.tracee.spi.TraceeBackend.REGISTERED_KEYS";

    @Override
    public final Collection<String> getRegisteredKeys() {
        final String serializedKeys = get(REGISTERED_KEYS_KEY);
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
            serializedBuilder.append(registeredKey);
            if (!first) serializedBuilder.append(',');
            first = false;
        }
        return serializedBuilder.toString();
    }

    private Collection<String> deserialize(String serialized) {
        final String[] split = serialized.trim().split(",",0);
            return Arrays.asList(split);

    }

    @Override
    public TraceeContext extractContext() {
        final TraceeContext traceeContext = new TraceeContext();
        for (String s : getRegisteredKeys()) {
            traceeContext.put(s, get(s));
        }
        return traceeContext;
    }
}
