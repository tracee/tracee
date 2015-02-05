package io.tracee.binding.jaxrs2;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import io.tracee.transport.HttpHeaderTransport;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.Map;

import static io.tracee.configuration.TraceeFilterConfiguration.Channel.OutgoingResponse;

@Provider
public class TraceeContainerResponseFilter implements ContainerResponseFilter {

	private final TraceeBackend backend;
	private final HttpHeaderTransport transportSerialization;

	TraceeContainerResponseFilter(TraceeBackend backend) {
		this.backend = backend;
		this.transportSerialization = new HttpHeaderTransport(backend.getLoggerFactory());
	}

	@SuppressWarnings("unused")
	public TraceeContainerResponseFilter() {
		this(Tracee.getBackend());
	}

	@Override
	public final void filter(final ContainerRequestContext requestContext,
							 final ContainerResponseContext responseContext) throws IOException {

		if (backend.getConfiguration().shouldProcessContext(OutgoingResponse)) {
			final Map<String, String> filtered = backend.getConfiguration().filterDeniedParams(backend.copyToMap(), OutgoingResponse);
			responseContext.getHeaders().putSingle(TraceeConstants.TPIC_HEADER, transportSerialization.render(filtered));
		}

		backend.clear();
	}
}
