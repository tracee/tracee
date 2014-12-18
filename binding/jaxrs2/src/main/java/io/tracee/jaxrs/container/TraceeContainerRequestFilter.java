package io.tracee.jaxrs.container;

import io.tracee.*;
import io.tracee.transport.HttpHeaderTransport;


import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static io.tracee.configuration.TraceeFilterConfiguration.Channel.OutgoingRequest;

@Provider
public class TraceeContainerRequestFilter implements ContainerRequestFilter {


	private final TraceeBackend backend;
	private final HttpHeaderTransport transportSerialization;

	@SuppressWarnings("unused")
	public TraceeContainerRequestFilter() {
		this(Tracee.getBackend(), new HttpHeaderTransport(Tracee.getBackend().getLoggerFactory()));
	}

	TraceeContainerRequestFilter(TraceeBackend backend, HttpHeaderTransport transportSerialization) {
		this.backend = backend;
		this.transportSerialization = transportSerialization;
	}


	@Override
	public final void filter(ContainerRequestContext containerRequestContext) throws IOException {
		Utilities.generateRequestIdIfNecessary(backend);

		if (backend.getConfiguration().shouldProcessContext(OutgoingRequest)) {
			final List<String> serializedTraceeHeaders = containerRequestContext.getHeaders().get(TraceeConstants.HTTP_HEADER_NAME);
			if (serializedTraceeHeaders != null && !serializedTraceeHeaders.isEmpty()) {
				final Map<String, String> parsed = transportSerialization.parse(serializedTraceeHeaders);
				final Map<String, String> filtered = backend.getConfiguration().filterDeniedParams(parsed, OutgoingRequest);
				backend.putAll(filtered);
			}
		}
	}
}
