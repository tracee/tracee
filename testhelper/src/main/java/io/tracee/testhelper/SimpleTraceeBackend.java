package io.tracee.testhelper;

import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import io.tracee.configuration.TraceeFilterConfiguration;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A testhelper for TraceeBackend dependent tests.
 */
public class SimpleTraceeBackend implements TraceeBackend {


	private Map<String, String> valuesBeforeLastClear = Collections.emptyMap();

	private Map<String, String> backendValues = new HashMap<>();



	public SimpleTraceeBackend() {
	}


	@Deprecated
	@Override
	public TraceeFilterConfiguration getConfiguration(String profileName) {
		throw new AssertionError("Used deprecated method: getConfiguration(String profileName)");
	}

	@Deprecated
	@Override
	public TraceeFilterConfiguration getConfiguration() {
		throw new AssertionError("Used deprecated method: getConfiguration()");
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
		this.valuesBeforeLastClear = new HashMap<>(this.copyToMap());
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
		return new HashMap<>(backendValues);
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
