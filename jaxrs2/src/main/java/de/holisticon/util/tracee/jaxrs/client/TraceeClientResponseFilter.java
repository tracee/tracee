package de.holisticon.util.tracee.jaxrs.client;

import de.holisticon.util.tracee.Tracee;
import de.holisticon.util.tracee.TraceeBackend;
import de.holisticon.util.tracee.TraceeConstants;
import de.holisticon.util.tracee.transport.HttpJsonHeaderTransport;
import de.holisticon.util.tracee.transport.TransportSerialization;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
@Provider
public class TraceeClientResponseFilter implements ClientResponseFilter {

    private final TraceeBackend backend = Tracee.getBackend();
    private final TransportSerialization<String> transportSerialization = new HttpJsonHeaderTransport();

    @Override
    public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {
        final String serializedContext = responseContext.getHeaders().getFirst(TraceeConstants.HTTP_HEADER_NAME);
        if (serializedContext != null)
            transportSerialization.mergeToBackend(backend, serializedContext);
    }

}
