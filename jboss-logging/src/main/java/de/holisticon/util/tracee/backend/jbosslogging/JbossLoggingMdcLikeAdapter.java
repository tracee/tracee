package de.holisticon.util.tracee.backend.jbosslogging;

import de.holisticon.util.tracee.MDCLike;
import org.jboss.logging.MDC;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
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

        return (String) MDC.get(key);
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
            if (!(entry.getValue() instanceof String)) continue;
            copy.put(entry.getKey(), (String) entry.getValue());
        }
        return copy;
    }
}
