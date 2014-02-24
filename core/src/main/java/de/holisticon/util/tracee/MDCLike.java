package de.holisticon.util.tracee;

import java.util.Map;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
public interface MDCLike {

    boolean containsKey(String key);

    void put(String key, String value);

    String get(String key);

    void remove(String key);

    Map<String, String> getCopyOfContext();

}
