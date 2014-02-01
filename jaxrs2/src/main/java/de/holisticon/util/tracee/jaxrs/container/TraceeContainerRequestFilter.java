package de.holisticon.util.tracee.jaxrs.container;

import de.holisticon.util.tracee.TraceeBackend;
import de.holisticon.util.tracee.TraceeConstants;
import de.holisticon.util.tracee.TraceeContextSerialization;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
@Provider
public class TraceeContainerRequestFilter implements ContainerRequestFilter {

    private final TraceeBackend backend;
    private final TraceeContextSerialization serialization = new TraceeContextSerialization();

    public TraceeContainerRequestFilter(TraceeBackend backend) {
        this.backend = backend;
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        final String serializedTraceeHeader = containerRequestContext.getHeaders().getFirst(TraceeConstants.HTTP_HEADER_NAME);
        if (serializedTraceeHeader != null)
            serialization.merge(backend, serializedTraceeHeader);
    }
}
