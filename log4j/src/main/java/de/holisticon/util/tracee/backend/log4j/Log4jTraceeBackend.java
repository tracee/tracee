package de.holisticon.util.tracee.backend.log4j;

import de.holisticon.util.tracee.MapLikeTraceeBackend;
import de.holisticon.util.tracee.TraceeLogger;
import org.apache.log4j.MDC;

/**
 * @author Daniel
 */
public class Log4jTraceeBackend extends MapLikeTraceeBackend {

    @Override
    public final String getFromMap(final String key) {
        return (String) MDC.get(key);
    }

    @Override
    protected final void putInMap(final String key, final String value) {
        MDC.put(key, value);
    }

    @Override
    protected final void removeFromMap(final String key) {
        MDC.remove(key);
    }

    @Override
    public final TraceeLogger getLogger(Class<?> clazz) {
        return new Log4JTraceeLogger(clazz);
    }

}
