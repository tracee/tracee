package io.tracee;

import java.util.Map;

public interface MDCLike {

    boolean containsKey(String key);

    void put(String key, String value);

    String get(String key);

    void remove(String key);

    Map<String, String> getCopyOfContext();

}
