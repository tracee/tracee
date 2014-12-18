package io.tracee.jaxrs.client;

import io.tracee.*;
import io.tracee.transport.HttpHeaderTransport;


import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.Map;

import static io.tracee.configuration.TraceeFilterConfiguration.Channel.OutgoingRequest;

@Provider
public class TraceeClientRequestFilter implements ClientRequestFilter {

	private final TraceeBackend backend;
	private final HttpHeaderTransport transportSerialization;

	@SuppressWarnings("unused")
	public TraceeClientRequestFilter() {
		this(Tracee.getBackend(), new HttpHeaderTransport(Tracee.getBackend().getLoggerFactory()));
	}

	TraceeClientRequestFilter(TraceeBackend backend, HttpHeaderTransport transportSerialization) {
		this.backend = backend;
		this.transportSerialization =  transportSerialization;
	}

    @Override
    public final void filter(ClientRequestContext requestContext) throws IOException {

		Utilities.generateRequestIdIfNecessary(backend);

		if (!backend.isEmpty() && backend.getConfiguration().shouldProcessContext(OutgoingRequest)) {
			final Map<String, String> filtered = backend.getConfiguration().filterDeniedParams(backend, OutgoingRequest);
			final String serializedTraceeContext = transportSerialization.render(filtered);
			if (serializedTraceeContext == null) {
				return;
			}
			requestContext.getHeaders().putSingle(TraceeConstants.HTTP_HEADER_NAME, serializedTraceeContext);
		}
    }
}
