package de.holisticon.util.tracee.jaxrs.container;

import de.holisticon.util.tracee.*;
import de.holisticon.util.tracee.transport.HttpJsonHeaderTransport;
import de.holisticon.util.tracee.transport.TransportSerialization;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
@Provider
public class TraceeContainerRequestFilter implements ContainerRequestFilter {

    private final TraceeBackend backend = Tracee.getBackend();
    private final TransportSerialization<String> transportSerialization = new HttpJsonHeaderTransport();


    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        // generate request id if it doesn't exist
        if (backend.get(TraceeConstants.REQUEST_ID_KEY) == null) {
            backend.put(TraceeConstants.REQUEST_ID_KEY, Utilities.createRandomAlphanumeric(32));
        }

        final String serializedTraceeHeader = containerRequestContext.getHeaders().getFirst(TraceeConstants.HTTP_HEADER_NAME);
        if (serializedTraceeHeader != null)
            transportSerialization.mergeToBackend(backend, serializedTraceeHeader);
    }
}
