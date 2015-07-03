package io.tracee.binding.jaxrs2;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import io.tracee.Utilities;
import io.tracee.transport.HttpHeaderTransport;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static io.tracee.configuration.TraceeFilterConfiguration.Channel.IncomingRequest;
import static io.tracee.configuration.TraceeFilterConfiguration.Channel.OutgoingResponse;

@Provider
public class TraceeContainerFilter implements ContainerRequestFilter, ContainerResponseFilter {

	private final TraceeBackend backend;
	private final HttpHeaderTransport transportSerialization;

	@SuppressWarnings("unused")
	public TraceeContainerFilter() {
		this(Tracee.getBackend());
	}

	TraceeContainerFilter(TraceeBackend backend) {
		this.backend = backend;
		this.transportSerialization = new HttpHeaderTransport();
	}

	/**
	 * This method handles the incoming request
	 */
	@Override
	public final void filter(final ContainerRequestContext containerRequestContext) {

		if (backend.getConfiguration().shouldProcessContext(IncomingRequest)) {
			final List<String> serializedTraceeHeaders = containerRequestContext.getHeaders().get(TraceeConstants.TPIC_HEADER);
			if (serializedTraceeHeaders != null && !serializedTraceeHeaders.isEmpty()) {
				final Map<String, String> parsed = transportSerialization.parse(serializedTraceeHeaders);
				backend.putAll(backend.getConfiguration().filterDeniedParams(parsed, IncomingRequest));
			}
		}

		Utilities.generateInvocationIdIfNecessary(backend);
	}

	/**
	 * This method handles the outgoing response
	 */
	@Override
	public void filter(final ContainerRequestContext requestContext, final ContainerResponseContext responseContext) {
		if (backend.getConfiguration().shouldProcessContext(OutgoingResponse)) {
			final Map<String, String> filtered = backend.getConfiguration().filterDeniedParams(backend.copyToMap(), OutgoingResponse);
			responseContext.getHeaders().putSingle(TraceeConstants.TPIC_HEADER, transportSerialization.render(filtered));
		}

		backend.clear();
	}
}
