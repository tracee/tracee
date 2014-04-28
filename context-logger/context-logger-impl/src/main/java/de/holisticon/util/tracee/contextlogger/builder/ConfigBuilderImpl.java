package de.holisticon.util.tracee.contextlogger.builder;

import de.holisticon.util.tracee.contextlogger.profile.Profile;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
public class ConfigBuilderImpl implements ConfigBuilder {

	private ContextLoggerBuilderImpl owningBuilder;

	private Profile profile = null;

	public Profile getProfile() {
		return profile;
	}

	private Map<String, Boolean> manualContextOverrides = new HashMap<String, Boolean>();


	public ConfigBuilderImpl(ContextLoggerBuilderImpl owningBuilder) {
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
	public ContextLoggerBuilder apply() {
		return owningBuilder;
	}

	Map<String, Boolean> getManualContextOverrides() {
		return manualContextOverrides;
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
