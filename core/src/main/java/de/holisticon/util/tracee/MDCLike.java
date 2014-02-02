package de.holisticon.util.tracee;

import java.util.Map;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
public interface MDCLike {

    boolean containsKey(String key);

    public void put(String key, String value);

    public String get(String key);

    public void remove(String key);

    public Map<String,String> getCopyOfContext();

}
