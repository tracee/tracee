package io.tracee;

import io.tracee.configuration.PropertiesBasedTraceeFilterConfiguration;
import io.tracee.configuration.PropertyChain;
import io.tracee.configuration.TraceeFilterConfiguration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static io.tracee.configuration.TraceeFilterConfiguration.*;


public abstract class MDCLikeTraceeBackend implements TraceeBackend {

	// Use #getPropertyChain to retrieve/get the chain
	private PropertyChain _lazyPropertyChain = null;

	private final TraceeLoggerFactory loggerFactory;

	private final ThreadLocal<Set<String>> traceeKeys;

	private Map<String, TraceeFilterConfiguration> configurationCache = new ConcurrentHashMap<String, TraceeFilterConfiguration>();

	/**
	 * Lazily initializes the configuration for this MDCLikeTraceeBackend.
	 */
	@Override
	public final TraceeFilterConfiguration getConfiguration() {
		return getConfiguration(null);
	}

	@Override
	public final TraceeFilterConfiguration getConfiguration(String profileName) {
		if (profileName == null) {
			profileName = Profile.DEFAULT;
		}
		TraceeFilterConfiguration filterConfiguration = configurationCache.get(profileName);
		if (filterConfiguration == null) {
			filterConfiguration = new PropertiesBasedTraceeFilterConfiguration(loggerFactory, getPropertyChain(), profileName);
			configurationCache.put(profileName, filterConfiguration);
		}
		return filterConfiguration;
	}

	private PropertyChain getPropertyChain() {
		if (_lazyPropertyChain == null) {
			_lazyPropertyChain = PropertiesBasedTraceeFilterConfiguration.loadPropertyChain();
		}
		return _lazyPropertyChain;
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

	@Override
	public String getRequestId() {
		return get(TraceeConstants.REQUEST_ID_KEY);
	}

	@Override
	public String getSessionId() {
		return get(TraceeConstants.SESSION_ID_KEY);
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
