package de.holisticon.util.tracee.backend.log4j;

import de.holisticon.util.tracee.MDCLike;
import org.apache.log4j.MDC;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
public class Log4jMdcLikeAdapter implements MDCLike {

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
        return (String)MDC.get(key);
    }

    @Override
    public void remove(String key) {
        MDC.remove(key);
    }

    @Override
    public Map<String, String> getCopyOfContext() {
        final HashMap<String,String> copy = new HashMap<String, String>();
        final Hashtable context = MDC.getContext();
        final Enumeration keys = context.keys();
        while (keys.hasMoreElements()) {
            final String key = (String)keys.nextElement();
            copy.put(key, (String)context.get(key));
        }
        return copy;
    }
}
