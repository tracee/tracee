package de.holisticon.util.tracee.jaxrs.container;

import de.holisticon.util.tracee.*;
import de.holisticon.util.tracee.configuration.TraceeFilterConfiguration;
import de.holisticon.util.tracee.transport.HttpJsonHeaderTransport;
import de.holisticon.util.tracee.transport.TransportSerialization;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.Map;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
@Provider
public class TraceeContainerRequestFilter implements ContainerRequestFilter {


	private final TraceeBackend backend;
	private final TransportSerialization<String> transportSerialization;

	public TraceeContainerRequestFilter() {
		this(Tracee.getBackend());
	}

	TraceeContainerRequestFilter(TraceeBackend backend) {
		this.backend = backend;
		this.transportSerialization = new HttpJsonHeaderTransport(backend.getLoggerFactory());
	}


	@Override
	public void filter(ContainerRequestContext containerRequestContext) throws IOException {
		// generate request id if it doesn't exist
		if (backend.get(TraceeConstants.REQUEST_ID_KEY) == null && backend.getConfiguration().generatedRequestIdLength() != 0) {
			backend.put(TraceeConstants.REQUEST_ID_KEY, Utilities.createRandomAlphanumeric(backend.getConfiguration().generatedRequestIdLength()));
		}

		if (backend.getConfiguration().shouldProcessContext(TraceeFilterConfiguration.MessageType.OutgoingRequest)) {
			final String serializedTraceeHeader = containerRequestContext.getHeaders().getFirst(TraceeConstants.HTTP_HEADER_NAME);
			if (serializedTraceeHeader != null) {
				final Map<String, String> parse = transportSerialization.parse(serializedTraceeHeader);
				backend.putAll(parse);
			}
		}
	}
}
