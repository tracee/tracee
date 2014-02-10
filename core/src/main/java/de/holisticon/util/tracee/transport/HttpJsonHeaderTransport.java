package de.holisticon.util.tracee.transport;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import de.holisticon.util.tracee.TraceeBackend;
import de.holisticon.util.tracee.TraceeLogger;
import de.holisticon.util.tracee.TraceeLoggerFactory;

import java.util.Collections;
import java.util.Map;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
public class HttpJsonHeaderTransport implements TransportSerialization<String> {

    private final Gson gson = new Gson();
	private final TraceeLogger logger;

	public HttpJsonHeaderTransport(TraceeLoggerFactory traceeLoggerFactory) {
		logger = traceeLoggerFactory.getLogger(HttpJsonHeaderTransport.class);
	}

    @Override
    public Map<String,String> parse(String serialized) {
        try {
            return gson.fromJson(serialized, new TypeToken<Map<String, String>>() { } .getType());

        } catch (JsonParseException e) {
			logger.debug("Failed to parse header. Ignoring: \"" + serialized + "\"");
			return Collections.emptyMap();
        }
    }

    @Override
    public String render(Map<String,String> backend) {
		if (backend.isEmpty()) 
			return null;
        
		return gson.toJson(backend);
    }

}
