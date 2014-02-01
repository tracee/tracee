package de.holisticon.util.tracee.jaxrs.container;

import de.holisticon.util.tracee.TraceeBackend;
import de.holisticon.util.tracee.TraceeConstants;
import de.holisticon.util.tracee.TraceeContextSerialization;

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

    private final TraceeBackend backend;
    private final TraceeContextSerialization serialization = new TraceeContextSerialization();

    public TraceeContainerResponseFilter(TraceeBackend backend) {
        this.backend = backend;
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        final String serializedContext = serialization.toHeaderRepresentation(backend);
        if (serializedContext != null)
            responseContext.getHeaders().putSingle(TraceeConstants.HTTP_HEADER_NAME, serializedContext);
        backend.clear();

    }
}
