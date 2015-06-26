package io.tracee.configuration;

import io.tracee.Utilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * A TraceeFilterConfiguration that is based on a {@link PropertyChain}.
 * The default property chain may be obtained by the {@link #loadPropertyChain()} method.
 */
public final class PropertiesBasedTraceeFilterConfiguration implements TraceeFilterConfiguration {

	static final String TRACEE_CONFIG_PREFIX = "tracee.";
	static final String PROFILED_PREFIX = TRACEE_CONFIG_PREFIX + "profile.";
	static final String TRACEE_DEFAULT_PROFILE_PREFIX = TRACEE_CONFIG_PREFIX + Profile.DEFAULT + ".";
	static final String GENERATE_INVOCATION_ID = "invocationIdLength";
	static final String GENERATE_SESSION_ID = "sessionIdLength";

	private final PropertyChain propertyChain;
	private final String profileName;

	private final Map<String, List<Pattern>> patternCache = new ConcurrentHashMap<String, List<Pattern>>();

	private static final Logger logger = LoggerFactory.getLogger(PropertiesBasedTraceeFilterConfiguration.class);

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

	public PropertiesBasedTraceeFilterConfiguration(PropertyChain propertyChain,
													String profileName) {
		this.propertyChain = propertyChain;
		this.profileName = profileName;
	}

	private String getProfiledOrDefaultProperty(final String propertyName) {
		if (profileName != null && !Profile.DEFAULT.equals(profileName)) {
			final String profiledProperty = propertyChain.getProperty(PROFILED_PREFIX + profileName + '.' + propertyName);
			if (profiledProperty != null)
				return profiledProperty;
		}
		return propertyChain.getProperty(TRACEE_DEFAULT_PROFILE_PREFIX + propertyName);
	}

	@Override
	public boolean shouldProcessParam(String paramName, Channel channel) {
		final String messageTypePropertyValue = getProfiledOrDefaultProperty(channel.name());
		final List<Pattern> patterns = retrievePatternsForPropertyValue(messageTypePropertyValue);
		return anyPatternMatchesParamName(patterns, paramName);
	}

	@Override
	public boolean shouldProcessContext(final Channel channel) {
		final String messageTypePropertyValue = getProfiledOrDefaultProperty(channel.name());
		return !Utilities.isNullOrEmptyString(messageTypePropertyValue);
	}

	@Override
	public boolean shouldGenerateInvocationId() {
		return generatedInvocationIdLength() > 0;
	}

	@Override
	public int generatedInvocationIdLength() {
		return parseIntOrZero(getProfiledOrDefaultProperty(GENERATE_INVOCATION_ID));
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
	public Map<String, String> filterDeniedParams(final Map<String, String> unfiltered, final Channel channel) {
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

	private boolean anyPatternMatchesParamName(Iterable<Pattern> patterns, String paramName) {
		for (Pattern pattern : patterns) {
			if (patternMatchesParamName(pattern, paramName))
				return true;
		}
		return false;
	}

	private boolean patternMatchesParamName(Pattern pattern, String paramName) {
		return ".*".equals(pattern.pattern()) || pattern.matcher(paramName).matches();
	}

	private List<Pattern> retrievePatternsForPropertyValue(final String propertyValue) {
		if (propertyValue == null) {
			return Collections.emptyList();
		}
		final List<Pattern> patterns = patternCache.get(propertyValue);
		if (patterns != null) {
			return patterns;
		}

		final List<Pattern> unmodPatterns = Collections.unmodifiableList(extractPatterns(propertyValue));
		patternCache.put(propertyValue, unmodPatterns);
		return unmodPatterns;
	}

	private List<Pattern> extractPatterns(final String propertyValue) {
		if (propertyValue == null)
			return Collections.emptyList();

		final List<Pattern> trimmedPatterns = new ArrayList<Pattern>();
		final StringTokenizer tokenizer = new StringTokenizer(propertyValue, ",");
		while (tokenizer.hasMoreTokens()) {
			final String trimmedString = tokenizer.nextToken().trim();
			if (!trimmedString.isEmpty()) {
				try {
					trimmedPatterns.add(Pattern.compile(trimmedString));
				} catch (PatternSyntaxException e) {
					logger.error("Can not compile pattern '" + trimmedString + "'. Message: " + e.getMessage() + " -- Ignore pattern");
					logger.debug("Detailed Exception cause: " + e.getMessage(), e);
				}
			}
		}
		return trimmedPatterns;
	}
}
