package io.tracee.binding.jaxrs2;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import io.tracee.transport.HttpHeaderTransport;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.ext.Provider;
import java.util.List;
import java.util.Map;

import static io.tracee.configuration.TraceeFilterConfiguration.Channel.IncomingResponse;
import static io.tracee.configuration.TraceeFilterConfiguration.Channel.OutgoingRequest;

@Provider
public class TraceeClientFilter implements ClientRequestFilter, ClientResponseFilter {

	private final TraceeBackend backend;
	private final HttpHeaderTransport transportSerialization;

	@SuppressWarnings("unused")
	public TraceeClientFilter() {
		this(Tracee.getBackend());
	}

	TraceeClientFilter(TraceeBackend backend) {
		this.backend = backend;
		this.transportSerialization = new HttpHeaderTransport();
	}

	/**
	 * This method handles the outgoing request
	 */
	@Override
	public final void filter(final ClientRequestContext requestContext) {
		if (!backend.isEmpty() && backend.getConfiguration().shouldProcessContext(OutgoingRequest)) {
			final Map<String, String> filtered = backend.getConfiguration().filterDeniedParams(backend.copyToMap(), OutgoingRequest);
			requestContext.getHeaders().putSingle(TraceeConstants.TPIC_HEADER, transportSerialization.render(filtered));
		}
	}

	/**
	 * This method handles the incoming response
	 */
	@Override
	public void filter(final ClientRequestContext requestContext, final ClientResponseContext responseContext) {
		final List<String> serializedHeaders = responseContext.getHeaders().get(TraceeConstants.TPIC_HEADER);
		if (serializedHeaders != null && backend.getConfiguration().shouldProcessContext(IncomingResponse)) {
			final Map<String, String> parsed = transportSerialization.parse(serializedHeaders);
			backend.putAll(backend.getConfiguration().filterDeniedParams(parsed, IncomingResponse));
		}
	}
}
