package io.tracee.contextlogger.builder.config;

import io.tracee.contextlogger.api.ConfigBuilder;
import io.tracee.contextlogger.api.ContextLoggerBuilder;
import io.tracee.contextlogger.api.internal.Configuration;
import io.tracee.contextlogger.profile.Profile;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation class to create a configuration by using the fluent api.
 */
public class ConfigBuilderImpl implements Configuration {

	private ContextLoggerBuilder owningBuilder;

	private Profile profile = null;

    private boolean keepOrder = false;

	private Map<String, Boolean> manualContextOverrides = new HashMap<String, Boolean>();


	public ConfigBuilderImpl(ContextLoggerBuilder owningBuilder) {
		this.owningBuilder = owningBuilder;
	}

	@Override
	public final ConfigBuilderImpl enforceProfile(Profile profile) {
		this.profile = profile;
		return this;
	}

	@Override
	public ConfigBuilder enable(String... contexts) {
		fillManualContextOverrideMap(contexts, true);
		return this;
	}

	@Override
	public ConfigBuilder disable(String... contexts) {
		fillManualContextOverrideMap(contexts, false);
		return this;
	}

    @Override
    public ConfigBuilder keepOrder() {
        this.keepOrder = true;
        return this;
    }

    @Override
	public ContextLoggerBuilder apply() {
		return owningBuilder;
	}

	public Map<String, Boolean> getManualContextOverrides() {
		return manualContextOverrides;
	}

    public Profile getProfile() {
        return profile;
    }

    public boolean getKeepOrder() {
        return keepOrder;
    }


	/**
	 * Adds passed contexts value pairs to manualContextOverrides.
	 *
	 * @param contexts The property name of the context data.
	 * @param value    the value which should be set.
	 */
	private void fillManualContextOverrideMap(final String[] contexts, final boolean value) {
		if (contexts != null) {

			for (String context : contexts) {

				if (!context.isEmpty()) {
					this.manualContextOverrides.put(context, value);
				}

			}

		}
	}
}
