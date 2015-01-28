package io.tracee.binding.jaxrs2;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import io.tracee.Utilities;
import io.tracee.transport.HttpHeaderTransport;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static io.tracee.configuration.TraceeFilterConfiguration.Channel.IncomingRequest;

@Provider
public class TraceeContainerRequestFilter implements ContainerRequestFilter {

	private final TraceeBackend backend;
	private final HttpHeaderTransport transportSerialization;

	@SuppressWarnings("unused")
	public TraceeContainerRequestFilter() {
		this(Tracee.getBackend());
	}

	TraceeContainerRequestFilter(TraceeBackend backend) {
		this.backend = backend;
		this.transportSerialization = new HttpHeaderTransport(backend.getLoggerFactory());
	}

	@Override
	public final void filter(ContainerRequestContext containerRequestContext) throws IOException {

		if (backend.getConfiguration().shouldProcessContext(IncomingRequest)) {
			final List<String> serializedTraceeHeaders = containerRequestContext.getHeaders().get(TraceeConstants.HTTP_HEADER_NAME);
			if (serializedTraceeHeaders != null && !serializedTraceeHeaders.isEmpty()) {
				final Map<String, String> parsed = transportSerialization.parse(serializedTraceeHeaders);
				backend.putAll(backend.getConfiguration().filterDeniedParams(parsed, IncomingRequest));
			}
		}

		Utilities.generateRequestIdIfNecessary(backend);
		Utilities.generateConversationIdIfNecessary(backend);
	}
}
