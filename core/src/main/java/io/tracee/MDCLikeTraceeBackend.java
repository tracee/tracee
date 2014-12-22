package io.tracee;

import io.tracee.configuration.PropertiesBasedTraceeFilterConfiguration;
import io.tracee.configuration.PropertyChain;
import io.tracee.configuration.TraceeFilterConfiguration;
import io.tracee.configuration.TraceePropertiesFileLoader;

import java.io.IOException;
import java.util.*;


public abstract class MDCLikeTraceeBackend implements TraceeBackend {

	public static final String TRACEE_PROPERTIES_FILE = "META-INF/tracee.properties";
	public static final String TRACEE_DEFAULT_PROPERTIES_FILE = "META-INF/tracee.default.properties";

	private PropertyChain lazyPropertyChain = null;

	private final TraceeLoggerFactory loggerFactory;

	private final ThreadLocal<Set<String>> traceeKeys;

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

	protected MDCLikeTraceeBackend(ThreadLocal<Set<String>> traceeKeys, TraceeLoggerFactory loggerFactory) {
        this.traceeKeys = traceeKeys;
		this.loggerFactory = loggerFactory;
    }

    @Override
    public final boolean containsKey(String key) {
        return key != null && traceeKeys.get().contains(key) && getFromMdc(key) != null;
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
    public final String get(String key) {
        if ((key != null) && traceeKeys.get().contains(key))
            return getFromMdc(key);
        else
            return null;
    }

    public final void put(String key, String value) {
        if (key == null) throw new NullPointerException("null keys are not allowed.");
        if (value == null) throw new NullPointerException("null values are not allowed.");
        final Set<String> registeredKeys = traceeKeys.get();
        if (!registeredKeys.contains(key)) {
            registeredKeys.add(key);
        }
        putToMdc(key, value);
    }

	@Override
    public final void remove(String key) {
        if (key == null) throw new NullPointerException("null keys are not allowed.");
        if (traceeKeys.get().remove(key)) {
            removeFromMdc(key);
        }
    }

    /**
     * Removes all tracee values from the underlying MDC and removes the thread local traceeKeys set.
     */
    @Override
    public final void clear() {
        final Set<String> keys = new HashSet<String>(traceeKeys.get());
        for (String key : keys) {
            remove(key);
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
	public Map<String, String> copyToMap() {
		final Map<String, String> traceeMap = new HashMap<String, String>();
		final Set<String> keys = traceeKeys.get();
		for (String traceeKey : keys) {
			final String value = getFromMdc(traceeKey);
			if (value != null) {
				traceeMap.put(traceeKey, value);
			}
		}
		return traceeMap;
	}

	@Override
	public final TraceeLoggerFactory getLoggerFactory() {
		return loggerFactory;
	}

	protected abstract String getFromMdc(String key);

	protected abstract void putToMdc(String key, String value);

	protected abstract void removeFromMdc(String key);

}
