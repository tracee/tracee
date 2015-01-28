package io.tracee;

import io.tracee.configuration.TraceeFilterConfiguration;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A testhelper for TraceeBackend dependent tests.
 */
public class SimpleTraceeBackend implements TraceeBackend {


	private Map<String, String> valuesBeforeLastClear = Collections.emptyMap();

	private Map<String, String> backendValues = new HashMap<String, String>();

	public static SimpleTraceeBackend createNonLoggingAllPermittingBackend() {
		return new SimpleTraceeBackend(new PermitAllTraceeFilterConfiguration(), new NoopTraceeLoggerFactory());
	}

	public SimpleTraceeBackend(TraceeFilterConfiguration configuration, TraceeLoggerFactory loggerFactory) {
		this.configuration = configuration;
		this.loggerFactory = loggerFactory;
	}

	private final TraceeFilterConfiguration configuration;

	private final TraceeLoggerFactory loggerFactory;

	/**
	 * {@inheritDoc}
	 * <br /><strong>This implementation ignores profiles and always uses the default configuration.</strong>
	 */
	@Override
	public TraceeFilterConfiguration getConfiguration(String profileName) {
		return configuration;
	}

	@Override
	public TraceeFilterConfiguration getConfiguration() {
		return configuration;
	}

	@Override
	public TraceeLoggerFactory getLoggerFactory() {
		return loggerFactory;
	}

	@Override
	public boolean containsKey(String key) {
		return backendValues.containsKey(key);
	}

	@Override
	public String get(String key) {
		return backendValues.get(key);
	}

	@Override
	public int size() {
		return backendValues.size();
	}

	@Override
	public void clear() {
		this.valuesBeforeLastClear = new HashMap<String, String>(this.copyToMap());
		backendValues.clear();
	}

	@Override
	public boolean isEmpty() {
		return backendValues.isEmpty();
	}

	@Override
	public void put(String key, String value) {
		backendValues.put(key, value);
	}

	@Override
	public void putAll(Map<? extends String, ? extends String> m) {
		backendValues.putAll(m);
	}

	@Override
	public Map<String, String> copyToMap() {
		return new HashMap<String, String>(backendValues);
	}

	@Override
	public void remove(String key) {
		backendValues.remove(key);
	}

	public Map<String, String> getValuesBeforeLastClear() {
		return valuesBeforeLastClear;
	}

	public String getRequestId() {
		return get(TraceeConstants.REQUEST_ID_KEY);
	}

	public String getSessionId() {
		return get(TraceeConstants.SESSION_ID_KEY);
	}

	public String getConversationId() {
		return get(TraceeConstants.CONVERSATION_ID_KEY);
	}
}
