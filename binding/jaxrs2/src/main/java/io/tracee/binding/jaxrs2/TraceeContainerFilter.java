package io.tracee.binding.jaxrs2;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import io.tracee.Utilities;
import io.tracee.configuration.PropertiesBasedTraceeFilterConfiguration;
import io.tracee.configuration.TraceeFilterConfiguration;
import io.tracee.transport.HttpHeaderTransport;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.util.List;
import java.util.Map;

import static io.tracee.configuration.TraceeFilterConfiguration.Channel.IncomingRequest;
import static io.tracee.configuration.TraceeFilterConfiguration.Channel.OutgoingResponse;

@Provider
public class TraceeContainerFilter implements ContainerRequestFilter, ContainerResponseFilter {

	private final TraceeBackend backend;
	private final TraceeFilterConfiguration filterConfiguration;
	private final HttpHeaderTransport transportSerialization;


	/**
	 * @deprecated  use full ctor
	 */
	@Deprecated
	public TraceeContainerFilter() {
		this(Tracee.getBackend(), PropertiesBasedTraceeFilterConfiguration.instance().DEFAULT);
	}

	/**
	 * @deprecated  use full ctor
	 */
	@Deprecated
	public TraceeContainerFilter(String profile) {
		this(Tracee.getBackend(), PropertiesBasedTraceeFilterConfiguration.instance().forProfile(profile));
	}

	public TraceeContainerFilter(TraceeBackend backend, TraceeFilterConfiguration filterConfiguration) {
		this.backend = backend;
		this.filterConfiguration = filterConfiguration;
		this.transportSerialization = new HttpHeaderTransport();
	}

	/**
	 * This method handles the incoming request
	 */
	@Override
	public void filter(final ContainerRequestContext containerRequestContext) {

		if (filterConfiguration.shouldProcessContext(IncomingRequest)) {
			final List<String> serializedTraceeHeaders = containerRequestContext.getHeaders().get(TraceeConstants.TPIC_HEADER);
			if (serializedTraceeHeaders != null && !serializedTraceeHeaders.isEmpty()) {
				final Map<String, String> parsed = transportSerialization.parse(serializedTraceeHeaders);
				backend.putAll(filterConfiguration.filterDeniedParams(parsed, IncomingRequest));
			}
		}

		Utilities.generateInvocationIdIfNecessary(backend, filterConfiguration);
	}

	/**
	 * This method handles the outgoing response
	 */
	@Override
	public void filter(final ContainerRequestContext requestContext, final ContainerResponseContext responseContext) {
		if (filterConfiguration.shouldProcessContext(OutgoingResponse)) {
			final Map<String, String> filtered = filterConfiguration.filterDeniedParams(backend.copyToMap(), OutgoingResponse);
			responseContext.getHeaders().putSingle(TraceeConstants.TPIC_HEADER, transportSerialization.render(filtered));
		}

		backend.clear();
	}
}
