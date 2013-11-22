package de.holisticon.util.tracee.backend.log4j;

import de.holisticon.util.tracee.MapLikeTraceeBackend;
import org.apache.log4j.MDC;

/**
 * @author Daniel
 */
public class Log4jTraceeBackend extends MapLikeTraceeBackend {

    @Override
    protected void putInMap(String key, String value) {
        MDC.put(key,value);
    }

    @Override
    protected void removeFromMap(String key) {
        MDC.remove(key);
    }

    @Override
    public String get(String key) {
        return (String)MDC.get(key);
    }

    @Override
    public boolean contains(String key) {
        return MDC.get(key) != null;
    }
}
