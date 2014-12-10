package io.tracee.jaxrs.container;

import io.tracee.*;
import io.tracee.transport.HttpJsonHeaderTransport;
import io.tracee.transport.TransportSerialization;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.Map;

import static io.tracee.configuration.TraceeFilterConfiguration.Channel.OutgoingRequest;

@Provider
public class TraceeContainerRequestFilter implements ContainerRequestFilter {


	private final TraceeBackend backend;
	private final TransportSerialization<String> transportSerialization;

	@SuppressWarnings("unused")
	public TraceeContainerRequestFilter() {
		this(Tracee.getBackend(), new HttpJsonHeaderTransport(Tracee.getBackend().getLoggerFactory()));
	}

	TraceeContainerRequestFilter(TraceeBackend backend, TransportSerialization<String> transportSerialization) {
		this.backend = backend;
		this.transportSerialization = transportSerialization;
	}


	@Override
	public final void filter(ContainerRequestContext containerRequestContext) throws IOException {
		// generate request id if it doesn't exist
		if (backend.get(TraceeConstants.REQUEST_ID_KEY) == null && backend.getConfiguration().shouldGenerateRequestId()) {
			backend.put(TraceeConstants.REQUEST_ID_KEY, Utilities.createRandomAlphanumeric(backend.getConfiguration().generatedRequestIdLength()));
		}

		if (backend.getConfiguration().shouldProcessContext(OutgoingRequest)) {
			final String serializedTraceeHeader = containerRequestContext.getHeaders().getFirst(TraceeConstants.HTTP_HEADER_NAME);
			if (serializedTraceeHeader != null) {
				final Map<String, String> parsed = transportSerialization.parse(serializedTraceeHeader);
				final Map<String, String> filtered = backend.getConfiguration().filterDeniedParams(parsed, OutgoingRequest);
				backend.putAll(filtered);
			}
		}
	}
}
