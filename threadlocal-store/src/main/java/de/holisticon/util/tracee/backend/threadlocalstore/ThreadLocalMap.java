package de.holisticon.util.tracee.backend.threadlocalstore;

import java.util.HashMap;
import java.util.Map;

public class ThreadLocalMap extends InheritableThreadLocal<Map<String, String>> {

    @Override
    protected Map<String, String> initialValue() {
        return new HashMap<String, String>();
    }

    @Override
    protected Map<String, String> childValue(Map<String, String> parentValue) {
        if (parentValue == null) {
            return null;
        } else {
            return new HashMap<String, String>(parentValue);
        }
    }

}