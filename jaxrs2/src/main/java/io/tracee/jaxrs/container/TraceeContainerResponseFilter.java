package io.tracee.jaxrs.container;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import io.tracee.transport.HttpJsonHeaderTransport;
import io.tracee.transport.TransportSerialization;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.Map;

import static io.tracee.configuration.TraceeFilterConfiguration.Channel.OutgoingResponse;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
@Provider
public class TraceeContainerResponseFilter implements ContainerResponseFilter {

	private final TraceeBackend backend;
	private final TransportSerialization<String> transportSerialization;

	TraceeContainerResponseFilter(TraceeBackend backend, TransportSerialization<String> transportSerialization) {
		this.backend = backend;
		this.transportSerialization = transportSerialization;
	}

	@SuppressWarnings("unused")
	public TraceeContainerResponseFilter() {
		this(Tracee.getBackend(), new HttpJsonHeaderTransport(Tracee.getBackend().getLoggerFactory()));
	}


	@Override
	public final void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {

		if (backend.getConfiguration().shouldProcessContext(OutgoingResponse)) {
			final Map<String, String> filtered = backend.getConfiguration().filterDeniedParams(backend, OutgoingResponse);
			final String serializedContext = transportSerialization.render(filtered);
			if (serializedContext != null)
				responseContext.getHeaders().putSingle(TraceeConstants.HTTP_HEADER_NAME, serializedContext);
		}

		backend.clear();

	}
}
