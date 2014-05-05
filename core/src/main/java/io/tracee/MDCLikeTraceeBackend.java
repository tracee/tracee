package io.tracee;

import io.tracee.configuration.PropertiesBasedTraceeFilterConfiguration;
import io.tracee.configuration.PropertyChain;
import io.tracee.configuration.TraceeFilterConfiguration;
import io.tracee.configuration.TraceePropertiesFileLoader;

import java.io.IOException;
import java.util.*;


public class MDCLikeTraceeBackend implements TraceeBackend {


	public static final String TRACEE_PROPERTIES_FILE = "META-INF/tracee.properties";
	public static final String TRACEE_DEFAULT_PROPERTIES_FILE = "META-INF/tracee.default.properties";

	private PropertyChain lazyPropertyChain = null;

	/**
	 * Lazily initializes the configuration for this MDCLikeTraceeBackend.
	 */
	@Override
	public final TraceeFilterConfiguration getConfiguration() {
		if (lazyPropertyChain == null) {
			lazyPropertyChain = loadPropertyChain();
		}
		return new PropertiesBasedTraceeFilterConfiguration(lazyPropertyChain);
	}

	@Override
	public final TraceeFilterConfiguration getConfiguration(String profileName) {
		if (lazyPropertyChain == null) {
			lazyPropertyChain = loadPropertyChain();
		}
		return new PropertiesBasedTraceeFilterConfiguration(lazyPropertyChain, profileName);
	}

	private PropertyChain loadPropertyChain() {
		try {
			final Properties traceeDefaultFileProperties = new TraceePropertiesFileLoader().loadTraceeProperties(TRACEE_DEFAULT_PROPERTIES_FILE);
			final Properties traceeFileProperties = new TraceePropertiesFileLoader().loadTraceeProperties(TRACEE_PROPERTIES_FILE);
			return PropertyChain.build(System.getProperties(), traceeFileProperties, traceeDefaultFileProperties);
		} catch (IOException ioe) {
			throw new IllegalStateException("Could not load TraceeProperties: " + ioe.getMessage(), ioe);
		}

	}

	protected MDCLikeTraceeBackend(MDCLike mdcAdapter, ThreadLocal<Set<String>> traceeKeys, TraceeLoggerFactory loggerFactory) {
        this.mdcAdapter = mdcAdapter;
        this.traceeKeys = traceeKeys;
		this.loggerFactory = loggerFactory;
    }

	private final TraceeLoggerFactory loggerFactory;

    private final MDCLike mdcAdapter;

    private final ThreadLocal<Set<String>> traceeKeys;


    @Override
    public final boolean containsKey(Object key) {
        return key instanceof String && traceeKeys.get().contains(key) && mdcAdapter.containsKey((String) key);
    }

    @Override
    public final boolean containsValue(Object value) {
        if (value == null) throw new NullPointerException("null values are not allowed.");
        for (String valueInInnerMap : traceeKeys.get()) {
            if (value.equals(valueInInnerMap))
                return true;
        }
        return false;
    }

    @Override
    public final int size() {
        return traceeKeys.get().size();
    }

    @Override
    public final boolean isEmpty() {
        return traceeKeys.get().isEmpty();
    }

    @Override
    public final String get(Object key) {
        if ((key instanceof String) && traceeKeys.get().contains(key))
            return mdcAdapter.get((String) key);
        else
            return null;
    }

    @Override
    public final Set<String> keySet() {
        return Collections.unmodifiableSet(traceeKeys.get());
    }

    @Override
    public final Collection<String> values() {
        final Collection<String> values = new ArrayList<String>(traceeKeys.get());
        for (String traceeKey : traceeKeys.get()) {
            values.add(mdcAdapter.get(traceeKey));
        }
        return Collections.unmodifiableCollection(values);
    }

    @Override
    public final String put(String key, String value) {
        if (key == null) throw new NullPointerException("null keys are not allowed.");
        if (value == null) throw new NullPointerException("null values are not allowed.");
        final Set<String> registeredKeys = traceeKeys.get();
        if (!registeredKeys.contains(key)) {
            registeredKeys.add(key);
        }
        final String current = mdcAdapter.get(key);
        mdcAdapter.put(key, value);
        return current;
    }

    @Override
    public final String remove(Object key) {
        if (key == null) throw new NullPointerException("null keys are not allowed.");
        if ((key instanceof String) && traceeKeys.get().remove(key)) {
            final String current = mdcAdapter.get((String) key);
            mdcAdapter.remove((String) key);
            return current;
        } else {
            return null;
        }
    }

    /**
     * Removes all tracee values from the underlying MDC and removes the thread local traceeKeys set.
     */
    @Override
    public final void clear() {
        final Set<String> keys = traceeKeys.get();
        for (String s : keys) {
            mdcAdapter.remove(s);
        }
        traceeKeys.remove();
    }

    @Override
    public final void putAll(Map<? extends String, ? extends String> entries) {
        for (Map.Entry<? extends String, ? extends String> entry : entries.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }



    @Override
    public final Set<Map.Entry<String, String>> entrySet() {
        final Set<Map.Entry<String, String>> entries = new HashSet<Map.Entry<String, String>>();
        for (String traceeKey : traceeKeys.get()) {
            entries.add(new Entry(traceeKey));
        }
        return Collections.unmodifiableSet(entries);
    }

	@Override
	public final TraceeLoggerFactory getLoggerFactory() {
		return loggerFactory;
	}


	private final class Entry implements Map.Entry<String, String> {

        public final String key;

        private Entry(String key) {
            this.key = key;
        }

        @Override
        public String getKey() {
            return key;
        }

        @Override
        public String getValue() {
            return MDCLikeTraceeBackend.this.get(key);
        }

        @Override
        public String setValue(String value) {
            return MDCLikeTraceeBackend.this.put(key, value);
        }
    }

}
