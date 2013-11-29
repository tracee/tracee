package de.holisticon.util.tracee;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.util.Map;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
public class TraceeContextSerialization {


    private final Gson gson = new Gson();


    public final String toHeaderRepresentation(TraceeBackend backend) {
        return gson.toJson(backend.extractContext());
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
