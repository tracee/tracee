package io.tracee.outbound.httpcomponents;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import io.tracee.configuration.TraceeFilterConfiguration;
import io.tracee.transport.HttpJsonHeaderTransport;
import io.tracee.transport.TransportSerialization;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.util.Map;

import static io.tracee.configuration.TraceeFilterConfiguration.Channel.OutgoingRequest;

public class TraceeHttpRequestInterceptor implements HttpRequestInterceptor {

	public TraceeHttpRequestInterceptor() {
		this(null);
	}
	public TraceeHttpRequestInterceptor(String profile) {
		this(Tracee.getBackend(), profile);
	}

	TraceeHttpRequestInterceptor(TraceeBackend backend, String profile) {
		this.backend = backend;
		this.transportSerialization = new HttpJsonHeaderTransport(backend.getLoggerFactory());
		this.profile = profile;
	}

	private final TraceeBackend backend;
	private final TransportSerialization<String> transportSerialization;
	private final String profile;

	@Override
	public final void process(HttpRequest httpRequest, HttpContext httpContext) throws HttpException, IOException {
		final TraceeFilterConfiguration filterConfiguration = backend.getConfiguration(profile);
		if (!backend.isEmpty() && filterConfiguration.shouldProcessContext(OutgoingRequest)) {
			final Map<String, String> filteredParams = filterConfiguration.filterDeniedParams(backend, OutgoingRequest);
			final String contextAsHeader = transportSerialization.render(filteredParams);
			httpRequest.setHeader(TraceeConstants.HTTP_HEADER_NAME, contextAsHeader);
		}
	}

}
