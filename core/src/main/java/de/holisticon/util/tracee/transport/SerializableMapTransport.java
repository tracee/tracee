package de.holisticon.util.tracee.transport;

import de.holisticon.util.tracee.TraceeBackend;

import java.util.HashMap;
import java.util.Map;

/**
 * Marshalling for TraceeBackend to a serializable Map.
 *
 * @author Daniel Wegener (Holisticon AG)
 */
public abstract class SerializableMapTransport implements TransportSerialization<Map<String,String> > {


    @Override
    public void mergeToBackend(TraceeBackend backend, Map<String,String> serialized) {
        backend.putAll(serialized);
    }

    @Override
    public Map<String,String> render(TraceeBackend backend) {
        return new HashMap<String, String>(backend);
    }


}
