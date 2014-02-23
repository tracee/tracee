package de.holisticon.util.tracee.jaxrs.container;

import de.holisticon.util.tracee.Tracee;
import de.holisticon.util.tracee.TraceeBackend;
import de.holisticon.util.tracee.TraceeConstants;
import de.holisticon.util.tracee.configuration.TraceeFilterConfiguration;
import de.holisticon.util.tracee.transport.HttpJsonHeaderTransport;
import de.holisticon.util.tracee.transport.TransportSerialization;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.Map;

import static de.holisticon.util.tracee.configuration.TraceeFilterConfiguration.Channel.OutgoingResponse;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
@Provider
public class TraceeContainerResponseFilter implements ContainerResponseFilter {

	private final TraceeBackend backend;
	private final TransportSerialization<String> transportSerialization;

	TraceeContainerResponseFilter(TraceeBackend backend) {
		this.backend = backend;
		this.transportSerialization = new HttpJsonHeaderTransport(backend.getLoggerFactory());
	}

	public TraceeContainerResponseFilter() {
		this(Tracee.getBackend());
	}


	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {

		if (backend.getConfiguration().shouldProcessContext(OutgoingResponse)) {
			final Map<String,String> filtered = backend.getConfiguration().filterDeniedParams(backend, OutgoingResponse);
			final String serializedContext = transportSerialization.render(filtered);
			if (serializedContext != null)
				responseContext.getHeaders().putSingle(TraceeConstants.HTTP_HEADER_NAME, serializedContext);
		}

		backend.clear();

	}
}
