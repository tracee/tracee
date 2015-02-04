package io.tracee.configuration;

import io.tracee.Utilities;

import java.io.IOException;
import java.util.*;

/**
 * A TraceeFilterConfiguration that is based on a {@link PropertyChain}.
 * The default property chain may be obtained by the {@link #loadPropertyChain()} method.
 */
public final class PropertiesBasedTraceeFilterConfiguration implements TraceeFilterConfiguration {


	static final String TRACEE_CONFIG_PREFIX = "tracee.";
	static final String PROFILED_PREFIX = TRACEE_CONFIG_PREFIX + "profile.";
	static final String DEFAULT_PROFILE_PREFIX = Profile.DEFAULT + ".";
	static final String GENERATE_REQUEST_ID = "requestIdLength";
	static final String GENERATE_SESSION_ID = "sessionIdLength";

	private final PropertyChain propertyChain;
	private final String profileName;

	/**
	 * Loads a layered property chain based on:
	 * <ol>
	 * <li>System properties</li>
	 * <li>merged entries from all {@code /META-INF/tracee.properties} files on the classpath (loaded in undefined order)</li>
	 * <li>merged entries from all {@code /META-INF/tracee.default.properties} files on the classpath (loaded in undefined order)</li>
	 * </ol>
	 */
	public static PropertyChain loadPropertyChain() {
		try {
			final Properties traceeDefaultFileProperties = new TraceePropertiesFileLoader().loadTraceeProperties(TraceePropertiesFileLoader.TRACEE_DEFAULT_PROPERTIES_FILE);
			final Properties traceeFileProperties = new TraceePropertiesFileLoader().loadTraceeProperties(TraceePropertiesFileLoader.TRACEE_PROPERTIES_FILE);
			return PropertyChain.build(System.getProperties(), traceeFileProperties, traceeDefaultFileProperties);
		} catch (IOException ioe) {
			throw new IllegalStateException("Could not load TraceeProperties: " + ioe.getMessage(), ioe);
		}
	}

	public PropertiesBasedTraceeFilterConfiguration(PropertyChain propertyChain) {
		this(propertyChain, null);
	}

	public PropertiesBasedTraceeFilterConfiguration(PropertyChain propertyChain, String profileName) {
		this.propertyChain = propertyChain;
		this.profileName = profileName;
	}

	private String getProfiledOrDefaultProperty(String propertyName) {
		if (profileName != null) {
			final String profiledProperty = propertyChain.getProperty(PROFILED_PREFIX + profileName + '.' + propertyName);
			if (profiledProperty != null)
				return profiledProperty;
		}
		return propertyChain.getProperty(TRACEE_CONFIG_PREFIX + DEFAULT_PROFILE_PREFIX + propertyName);
	}

	@Override
	public boolean shouldProcessParam(String paramName, Channel channel) {
		final String messageTypePropertyValue = getProfiledOrDefaultProperty(channel.name());
		final List<String> patterns = extractPatterns(messageTypePropertyValue);
		return anyPatternMatchesParamName(patterns, paramName);
	}

	@Override
	public boolean shouldProcessContext(Channel channel) {
		final String messageTypePropertyValue = getProfiledOrDefaultProperty(channel.name());
		return !Utilities.isNullOrEmptyString(messageTypePropertyValue);
	}

	@Override
	public boolean shouldGenerateRequestId() {
		return generatedRequestIdLength() > 0;
	}

	@Override
	public int generatedRequestIdLength() {
		return parseIntOrZero(getProfiledOrDefaultProperty(GENERATE_REQUEST_ID));
	}

	@Override
	public boolean shouldGenerateSessionId() {
		return generatedSessionIdLength() > 0;
	}

	@Override
	public int generatedSessionIdLength() {
		return parseIntOrZero(getProfiledOrDefaultProperty(GENERATE_SESSION_ID));
	}

	@Override
	public Map<String, String> filterDeniedParams(Map<String, String> unfiltered, Channel channel) {
		final Map<String, String> filtered = new HashMap<String, String>(unfiltered.size());
		for (Map.Entry<String, String> entry : unfiltered.entrySet()) {
			if (shouldProcessParam(entry.getKey(), channel)) {
				filtered.put(entry.getKey(), entry.getValue());
			}
		}
		return filtered;
	}

	private int parseIntOrZero(String intString) {
		try {
			return Integer.parseInt(intString);
		} catch (NumberFormatException nfe) {
			return 0;
		}
	}

	private boolean anyPatternMatchesParamName(Iterable<String> patterns, String paramName) {
		for (String pattern : patterns) {
			if (patternMatchesParamName(pattern, paramName))
				return true;
		}
		return false;
	}

	private boolean patternMatchesParamName(String pattern, String paramName) {
		// .* matches the whole param and we could speed up this call with a simple equal
		return ".*".equals(pattern) || paramName.matches(pattern);
	}

	private List<String> extractPatterns(String propertyValue) {
		if (propertyValue == null)
			return Collections.emptyList();

		final List<String> trimmedPatterns = new ArrayList<String>();
		final StringTokenizer tokenizer = new StringTokenizer(propertyValue, ",");
		while (tokenizer.hasMoreTokens()) {
			final String trimmedString = tokenizer.nextToken().trim();
			if (!trimmedString.isEmpty()) {
				trimmedPatterns.add(trimmedString);
			}
		}
		return trimmedPatterns;
	}
}
