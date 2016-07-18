package io.tracee.backend.slf4j;

import io.tracee.BackendBase;
import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

class Slf4jTraceeBackend extends BackendBase {

	/**
	 * This set contains all MDC-Keys managed by tracee.
	 * This bookkeeping is required to ensure that operations like {@link Slf4jTraceeBackend#clear()} do not remove
	 * TracEE unrelated keys from the MDC.
	 */
	protected final ThreadLocal<Set<String>> traceeKeys;

	Slf4jTraceeBackend(ThreadLocal<Set<String>> traceeKeys) {
		this.traceeKeys = traceeKeys;
	}

	@Override
	public boolean containsKey(String key) {
		return key != null && traceeKeys.get().contains(key) && MDC.get(key) != null;
	}

	@Override
	public int size() {
		return traceeKeys.get().size();
	}

	@Override
	public boolean isEmpty() {
		return traceeKeys.get().isEmpty();
	}

	@Override
	public String get(String key) {
		if ((key != null) && traceeKeys.get().contains(key))
			return MDC.get(key);
		else
			return null;
	}

	@Override
	public void put(String key, String value) throws IllegalArgumentException {
		if (key == null) throw new IllegalArgumentException("null keys are not allowed.");
		if (value == null) throw new IllegalArgumentException("null values are not allowed.");
		final Set<String> registeredKeys = traceeKeys.get();
		if (!registeredKeys.contains(key)) {
			registeredKeys.add(key);
		}
		MDC.put(key, value);
	}

	@Override
	public void remove(String key) throws IllegalArgumentException {
		if (key == null) throw new IllegalArgumentException("null keys are not allowed.");
		if (traceeKeys.get().remove(key)) {
			MDC.remove(key);
		}
	}

	@Override
	public void clear() {
		for (String key : traceeKeys.get()) {
			MDC.remove(key);
		}
		traceeKeys.remove();
	}

	@Override
	public void putAll(Map<? extends String, ? extends String> entries) {
		for (Map.Entry<? extends String, ? extends String> entry : entries.entrySet()) {
			put(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public Map<String, String> copyToMap() {
		final Map<String, String> traceeMap = new HashMap<>();
		final Set<String> keys = traceeKeys.get();
		for (String traceeKey : keys) {
			final String value = MDC.get(traceeKey);
			if (value != null) {
				traceeMap.put(traceeKey, value);
			}
		}
		return traceeMap;
	}
}
