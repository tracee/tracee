package de.holisticon.util.tracee.configuration;

import de.holisticon.util.tracee.Utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
public final class PropertiesBasedTraceeFilterConfiguration implements TraceeFilterConfiguration {

	public static final String CONFIGURATION_KEY_PREFIX = "tracee.";
	public static final String GENERATE_REQUEST_ID = CONFIGURATION_KEY_PREFIX+"generatedRequestIdLength";
	public static final String GENERATE_SESSION_ID = CONFIGURATION_KEY_PREFIX + "generatedSessionIdLength";

	public PropertiesBasedTraceeFilterConfiguration(Properties traceeFileProperties) {
		this.traceeFileProperties = traceeFileProperties;
	}

	private final Properties traceeFileProperties;


	private String getPropertyWithFallback(String key) {
		return System.getProperty(key, traceeFileProperties.getProperty(key));
	}

	@Override
	public boolean shouldPropagate(String paramName, MessageType channel) {
		final String messageTypePropertyValue = getPropertyWithFallback(CONFIGURATION_KEY_PREFIX + channel.name());
		final List<String> patterns = extractPatterns(messageTypePropertyValue);
		return anyPatternMatchesParamName(patterns, paramName);
	}

	@Override
	public boolean shouldProcessContext(MessageType channel) {
		final String messageTypePropertyValue = getPropertyWithFallback(CONFIGURATION_KEY_PREFIX + channel.name());
		return !Utilities.isNullOrEmptyString(messageTypePropertyValue);
	}

	@Override
	public int generatedRequestIdLength() {
		return parseIntOrZero( getPropertyWithFallback(GENERATE_REQUEST_ID));
	}

	@Override
	public int generatedSessionIdLength() {
		return parseIntOrZero( getPropertyWithFallback(GENERATE_SESSION_ID));
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
		return paramName.matches(pattern);
	}

	private List<String> extractPatterns(String propertyValue) {
		if (propertyValue == null)
			return Collections.emptyList();

		final String[] rawPatterns = propertyValue.split(",");
		final List<String> trimmedPatterns = new ArrayList<String>(rawPatterns.length);
		for (String rawPattern : rawPatterns) {
			final String trimmedPattern = rawPattern.trim();
			if (!trimmedPattern.isEmpty()) {
				trimmedPatterns.add(trimmedPattern);
			}
		}
		return trimmedPatterns;
	}
}