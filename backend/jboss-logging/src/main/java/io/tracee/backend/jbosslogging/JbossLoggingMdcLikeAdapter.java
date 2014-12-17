package io.tracee.backend.jbosslogging;

import io.tracee.MDCLike;
import org.jboss.logging.MDC;

import java.util.HashMap;
import java.util.Map;

final class JbossLoggingMdcLikeAdapter implements MDCLike {

    @Override
    public boolean containsKey(String key) {
        return MDC.get(key) != null;
    }

    @Override
    public void put(String key, String value) {
        MDC.put(key, value);
    }

    @Override
    public String get(String key) {
        return String.valueOf(MDC.get(key));
    }

    @Override
    public void remove(String key) {
        MDC.remove(key);
    }

    @Override
    public Map<String, String> getCopyOfContext() {
        final Map<String, Object> map = MDC.getMap();
        final Map<String, String> copy = new HashMap<String, String>(map.size());
        for (Map.Entry<String, Object> entry : map.entrySet()) {
			copy.put(entry.getKey(), String.valueOf(entry.getValue()));
        }
        return copy;
    }
}
