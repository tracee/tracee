package de.holisticon.util.tracee.transport;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Marshalling for TraceeBackend to a serializable Map.
 *
 * @author Daniel Wegener (Holisticon AG)
 */
public final class SerializableMapTransport implements TransportSerialization<Map<String, String>> {

	@Override
	public Map<String, String> parse(Map<String, String> serialized) {
		return serialized;
	}

	@Override
	public Map<String, String> render(Map<String, String> backend) {
		if (backend instanceof Serializable) {
			return backend;
		} else {
			return new HashMap<String, String>(backend);
		}
	}

}
