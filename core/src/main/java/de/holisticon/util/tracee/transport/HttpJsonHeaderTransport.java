package de.holisticon.util.tracee.transport;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import de.holisticon.util.tracee.TraceeBackend;

import java.util.Map;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
public class HttpJsonHeaderTransport implements TransportSerialization<String> {

    private final Gson gson = new Gson();

    @Override
    public void mergeToBackend(TraceeBackend backend, String serialized) {
        try {
            final Map<?, ?> map = gson.fromJson(serialized, new TypeToken<Map<String, String>>() { } .getType());
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                backend.put(entry.getKey().toString(), entry.getValue().toString());
            }
        } catch (JsonParseException e) {
            backend.getLogger(HttpJsonHeaderTransport.class).debug("Failed to parse header. Ignoring: \""+serialized+"\"");
        }
    }

    @Override
    public String render(TraceeBackend backend) {
		if (backend.isEmpty()) 
			return null;
        
		return gson.toJson(backend);
    }
}
