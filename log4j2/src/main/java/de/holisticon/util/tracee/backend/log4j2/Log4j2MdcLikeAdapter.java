package de.holisticon.util.tracee.backend.log4j2;

import de.holisticon.util.tracee.MDCLike;
import org.apache.logging.log4j.ThreadContext;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
class Log4j2MdcLikeAdapter implements MDCLike {

	@Override
	public boolean containsKey(String key) {
		return ThreadContext.get(key) != null;
	}

	@Override
	public void put(String key, String value) {
		ThreadContext.put(key, value);
	}

	@Override
	public String get(String key) {
		return ThreadContext.get(key);
	}

	@Override
	public void remove(String key) {
		ThreadContext.remove(key);
	}

	@Override
	public Map<String, String> getCopyOfContext() {
		return new HashMap<String, String>(ThreadContext.getContext());
	}
}
