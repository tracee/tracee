package de.holisticon.util.tracee.jaxrs.container;

import de.holisticon.util.tracee.Tracee;
import de.holisticon.util.tracee.TraceeBackend;
import de.holisticon.util.tracee.TraceeConstants;
import de.holisticon.util.tracee.transport.HttpJsonHeaderTransport;
import de.holisticon.util.tracee.transport.TransportSerialization;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
@Provider
public class TraceeContainerResponseFilter implements ContainerResponseFilter {

    private final TraceeBackend backend = Tracee.getBackend();
    private final TransportSerialization<String> transportSerialization = new HttpJsonHeaderTransport();

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        final String serializedContext = transportSerialization.render(backend);
        if (serializedContext != null)
            responseContext.getHeaders().putSingle(TraceeConstants.HTTP_HEADER_NAME, serializedContext);
        backend.clear();
    }
}
