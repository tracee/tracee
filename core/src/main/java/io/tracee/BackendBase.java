package io.tracee;

import io.tracee.configuration.PropertiesBasedTraceeFilterConfiguration;
import io.tracee.configuration.PropertyChain;
import io.tracee.configuration.TraceeFilterConfiguration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static io.tracee.configuration.TraceeFilterConfiguration.*;


public abstract class BackendBase implements TraceeBackend {

	// Use #getPropertyChain to retrieve/get the chain
	private PropertyChain _lazyPropertyChain = null;

	private Map<String, TraceeFilterConfiguration> configurationCache = new ConcurrentHashMap<>();

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
			filterConfiguration = new PropertiesBasedTraceeFilterConfiguration(getPropertyChain(), profileName);
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

	@Override
	public String getInvocationId() {
		return get(TraceeConstants.INVOCATION_ID_KEY);
	}

	@Override
	public String getSessionId() {
		return get(TraceeConstants.SESSION_ID_KEY);
	}

}
