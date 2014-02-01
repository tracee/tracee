package de.holisticon.util.tracee.jaxrs.client;

import de.holisticon.util.tracee.TraceeBackend;
import de.holisticon.util.tracee.TraceeConstants;
import de.holisticon.util.tracee.TraceeContextSerialization;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
@Provider
public class TraceeClientRequestFilter implements ClientRequestFilter {

    private final TraceeBackend backend;
    private final TraceeContextSerialization traceeContextSerialization = new TraceeContextSerialization();

    public TraceeClientRequestFilter(TraceeBackend backend) {
        this.backend = backend;
    }

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        final String serializedTraceeContext = traceeContextSerialization.toHeaderRepresentation(backend);
        if (serializedTraceeContext == null)
            return;
        requestContext.getHeaders().putSingle(TraceeConstants.HTTP_HEADER_NAME, serializedTraceeContext);
    }
}