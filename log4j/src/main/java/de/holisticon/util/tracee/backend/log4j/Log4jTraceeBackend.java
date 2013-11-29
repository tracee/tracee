package de.holisticon.util.tracee.backend.log4j;

import de.holisticon.util.tracee.MapLikeTraceeBackend;
import org.apache.log4j.MDC;

/**
 * @author Daniel
 */
public class Log4jTraceeBackend extends MapLikeTraceeBackend {

    @Override
    protected final void putInMap(String key, String value) {
        MDC.put(key, value);
    }

    @Override
    protected final void removeFromMap(String key) {
        MDC.remove(key);
    }


    @Override
    public final String getFromMap(String key) {
        return (String) MDC.get(key);
    }

}
