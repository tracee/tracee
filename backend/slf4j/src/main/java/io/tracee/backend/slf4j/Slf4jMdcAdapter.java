package io.tracee.backend.slf4j;

import io.tracee.MDCLike;
import org.slf4j.MDC;

import java.util.Map;

public final class Slf4jMdcAdapter implements MDCLike {

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
        return MDC.get(key);
    }

    @Override
    public void remove(String key) {
        MDC.remove(key);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, String> getCopyOfContext() {
        return (Map<String, String>) MDC.getCopyOfContextMap();
    }
}
