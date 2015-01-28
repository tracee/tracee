package io.tracee.configuration;

import io.tracee.Utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public final class PropertiesBasedTraceeFilterConfiguration implements TraceeFilterConfiguration {

	public static final String TRACEE_CONFIG_PREFIX = "tracee.";

	static final String DEFAULT_PROFILE_PREFIX = DEFAULT_PROFILE + ".";
	public static final String GENERATE_REQUEST_ID = "requestIdLength";
	public static final String GENERATE_SESSION_ID = "sessionIdLength";
	public static final String GENERATE_CONVERSATION_ID = "conversationIdLength";

	private final PropertyChain propertyChain;
	private final String profileName;

	public PropertiesBasedTraceeFilterConfiguration(PropertyChain propertyChain) {
		this(propertyChain, null);
	}

	public PropertiesBasedTraceeFilterConfiguration(PropertyChain propertyChain, String profileName) {
		this.propertyChain = propertyChain;
		this.profileName = profileName;
	}

	private String getProfiledOrDefaultProperty(String propertyName) {
		if (profileName != null) {
			final String profiledProperty = propertyChain.getProperty(TRACEE_CONFIG_PREFIX + profileName + '.' + propertyName);
			if (profiledProperty != null) {
				return profiledProperty;
			}
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
	public boolean shouldGenerateConversationId() {
		return generatedConversationIdLength() > 0;
	}

	@Override
	public int generatedConversationIdLength() {
		return parseIntOrZero(getProfiledOrDefaultProperty(GENERATE_CONVERSATION_ID));
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
			if (patternMatchesParamName(pattern, paramName)) {
				return true;
			}
		}
		return false;
	}

	private boolean patternMatchesParamName(String pattern, String paramName) {
		return paramName.matches(pattern);
	}

	private List<String> extractPatterns(String propertyValue) {
		if (propertyValue == null) {
			return Collections.emptyList();
		}

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
