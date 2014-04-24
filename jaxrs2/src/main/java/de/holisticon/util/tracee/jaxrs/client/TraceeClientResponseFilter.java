package de.holisticon.util.tracee.jaxrs.client;

import de.holisticon.util.tracee.Tracee;
import de.holisticon.util.tracee.TraceeBackend;
import de.holisticon.util.tracee.TraceeConstants;
import de.holisticon.util.tracee.transport.HttpJsonHeaderTransport;
import de.holisticon.util.tracee.transport.TransportSerialization;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.Map;

import static de.holisticon.util.tracee.configuration.TraceeFilterConfiguration.Channel.IncomingResponse;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
@Provider
public class TraceeClientResponseFilter implements ClientResponseFilter {

    private final TraceeBackend backend;
    private final TransportSerialization<String> transportSerialization;

	@SuppressWarnings("unused")
	public TraceeClientResponseFilter() {
		this(Tracee.getBackend(), new HttpJsonHeaderTransport(Tracee.getBackend().getLoggerFactory()));
	}

	TraceeClientResponseFilter(TraceeBackend backend, TransportSerialization<String> transportSerialization) {
		this.backend = backend;
		this.transportSerialization =  transportSerialization;
	}

	@Override
    public final void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {
        final String serializedContext = responseContext.getHeaders().getFirst(TraceeConstants.HTTP_HEADER_NAME);
        if (serializedContext != null && backend.getConfiguration().shouldProcessContext(IncomingResponse)) {
			final Map<String, String> parsed = transportSerialization.parse(serializedContext);
			final Map<String, String> filtered = backend.getConfiguration().filterDeniedParams(parsed, IncomingResponse);
			backend.putAll(filtered);
		}

    }

}
