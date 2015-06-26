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
		return new SimpleTraceeBackend(new PermitAllTraceeFilterConfiguration());
	}

	public SimpleTraceeBackend(TraceeFilterConfiguration configuration) {
		this.configuration = configuration;
	}

	private final TraceeFilterConfiguration configuration;

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

	@Override
	public String getInvocationId() {
		return get(TraceeConstants.INVOCATION_ID_KEY);
	}

	@Override
	public String getSessionId() {
		return get(TraceeConstants.SESSION_ID_KEY);
	}

	public Map<String, String> getValuesBeforeLastClear() {
		return valuesBeforeLastClear;
	}
}
