package io.tracee;

import io.tracee.configuration.PropertiesBasedTraceeFilterConfiguration;
import io.tracee.configuration.PropertyChain;
import io.tracee.configuration.TraceeFilterConfiguration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static io.tracee.configuration.TraceeFilterConfiguration.Profile;


public abstract class BackendBase implements TraceeBackend {

	private Map<String, TraceeFilterConfiguration> configurationCache = new ConcurrentHashMap<>();

	/**
	 * Lazily initializes the configuration for this MDCLikeTraceeBackend.
	 * @deprecated Create your own TraceeFilterConfiguration PropertiesBasedTraceeFilterConfiguration#instance() from core.
	 */
	@Deprecated
	@Override
	public final TraceeFilterConfiguration getConfiguration() {
		return getConfiguration(null);
	}

	/**
	 * Lazily initializes the configuration for this MDCLikeTraceeBackend.
	 * @deprecated Create your own TraceeFilterConfiguration PropertiesBasedTraceeFilterConfiguration#instance() from core.
	 */
	@Deprecated
	@Override
	public final TraceeFilterConfiguration getConfiguration(String profileName) {
		final String lookupProfile = profileName == null ? Profile.DEFAULT : profileName;

		TraceeFilterConfiguration filterConfiguration = configurationCache.get(lookupProfile);
		if (filterConfiguration == null) {
			filterConfiguration = PropertiesBasedTraceeFilterConfiguration.instance().forProfile(profileName);
			configurationCache.put(lookupProfile, filterConfiguration);
		}
		return filterConfiguration;
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
