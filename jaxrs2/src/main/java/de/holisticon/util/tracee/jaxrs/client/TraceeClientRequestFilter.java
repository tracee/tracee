package de.holisticon.util.tracee.jaxrs.client;

import de.holisticon.util.tracee.*;
import de.holisticon.util.tracee.transport.HttpJsonHeaderTransport;
import de.holisticon.util.tracee.transport.TransportSerialization;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
@Provider
public class TraceeClientRequestFilter implements ClientRequestFilter {

    private final TraceeBackend backend = Tracee.getBackend();

    private final TransportSerialization<String> transportSerialization = new HttpJsonHeaderTransport();

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {

        // generate request id if it doesn't exist
        if (backend.get(TraceeConstants.REQUEST_ID_KEY) == null) {
            backend.put(TraceeConstants.REQUEST_ID_KEY, Utilities.createRandomAlphanumeric(32));
        }

        final String serializedTraceeContext = transportSerialization.render(backend);
        if (serializedTraceeContext == null)
            return;
        requestContext.getHeaders().putSingle(TraceeConstants.HTTP_HEADER_NAME, serializedTraceeContext);
    }
}