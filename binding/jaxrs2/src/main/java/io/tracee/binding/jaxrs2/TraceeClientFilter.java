package io.tracee.binding.jaxrs2;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import io.tracee.configuration.PropertiesBasedTraceeFilterConfiguration;
import io.tracee.configuration.TraceeFilterConfiguration;
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
	private final TraceeFilterConfiguration filterConfiguration;

	/**
	 * @deprecated use full ctor
	 */
	@Deprecated
	public TraceeClientFilter() {
		this(Tracee.getBackend(), PropertiesBasedTraceeFilterConfiguration.instance().DEFAULT);
	}

	TraceeClientFilter(TraceeBackend backend, TraceeFilterConfiguration filterConfiguration) {
		this.backend = backend;
		this.filterConfiguration = filterConfiguration;
		this.transportSerialization = new HttpHeaderTransport();
	}

	/**
	 * This method handles the outgoing request
	 */
	@Override
	public void filter(final ClientRequestContext requestContext) {
		if (!backend.isEmpty() && filterConfiguration.shouldProcessContext(OutgoingRequest)) {
			final Map<String, String> filtered = filterConfiguration.filterDeniedParams(backend.copyToMap(), OutgoingRequest);
			requestContext.getHeaders().putSingle(TraceeConstants.TPIC_HEADER, transportSerialization.render(filtered));
		}
	}

	/**
	 * This method handles the incoming response
	 */
	@Override
	public void filter(final ClientRequestContext requestContext, final ClientResponseContext responseContext) {
		final List<String> serializedHeaders = responseContext.getHeaders().get(TraceeConstants.TPIC_HEADER);
		if (serializedHeaders != null && filterConfiguration.shouldProcessContext(IncomingResponse)) {
			final Map<String, String> parsed = transportSerialization.parse(serializedHeaders);
			backend.putAll(filterConfiguration.filterDeniedParams(parsed, IncomingResponse));
		}
	}
}
