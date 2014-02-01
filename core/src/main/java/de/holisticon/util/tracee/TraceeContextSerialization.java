package de.holisticon.util.tracee;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
public class TraceeContextSerialization {

    private final Gson gson = new Gson();

    /**
     * @return a serialized view of the given TraceeBackend or {@code null} if the backend is empty.
     */
    public final String toHeaderRepresentation(TraceeBackend backend) {
        final TreeMap<String,String> context = backend.extractContext();
        if (context.isEmpty()) return null;
        else return gson.toJson(context);
    }

    public final boolean merge(TraceeBackend backend, String serialized) {
        try {
            final Map<?, ?> map = gson.fromJson(serialized, new TypeToken<Map<String, String>>() { } .getType());
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                backend.put(entry.getKey().toString(), entry.getValue().toString());
            }

        } catch (JsonParseException e) {
            return false;
        }
        return true;
    }

}
