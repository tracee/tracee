package de.holisticon.util.tracee;

import java.util.Map;
import java.util.TreeMap;

/**
 * @authorDaniel
 */
public class TraceeContext extends TreeMap<String,String> {

    private static final String ENTRY_SEPARATOR = ",";
    private static final String VALUE_SEPARATOR = "=";

    public TraceeContext(){}

    public TraceeContext(String serialized) {
        final String[] entries = serialized.split("^\\"+ENTRY_SEPARATOR);


        for (String entry : entries) {
            final String[] split = entry.split("^\\"+VALUE_SEPARATOR);
            put(split[0], split[1]);
        }
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : entrySet()) {
            sb.append(escape(entry.getKey()));
            sb.append(escape(entry.getValue()));
        }
        if (sb.length()>0) sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }

    String unescape(String escaped) {
        if (escaped == null) return null;
        return escaped
                .replace("\\"+VALUE_SEPARATOR, VALUE_SEPARATOR)
                .replace("\\"+ENTRY_SEPARATOR, ENTRY_SEPARATOR)
                .replace("\\\\", "\\");
    }

    String escape(String unescaped) {
        if (unescaped == null) return null;
        return unescaped
                .replace("\\","\\\\")
                .replace(ENTRY_SEPARATOR, "\\"+ENTRY_SEPARATOR)
                .replace(VALUE_SEPARATOR, "\\"+VALUE_SEPARATOR);
    }
}
