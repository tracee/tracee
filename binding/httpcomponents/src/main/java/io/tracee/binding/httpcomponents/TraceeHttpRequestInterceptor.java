package io.tracee.binding.httpcomponents;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import io.tracee.configuration.PropertiesBasedTraceeFilterConfiguration;
import io.tracee.configuration.TraceeFilterConfiguration;
import io.tracee.configuration.TraceeFilterConfiguration.Profile;
import io.tracee.transport.HttpHeaderTransport;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.protocol.HttpContext;

import java.util.Map;

import static io.tracee.configuration.TraceeFilterConfiguration.Channel.OutgoingRequest;

public class TraceeHttpRequestInterceptor implements HttpRequestInterceptor {

	private final TraceeBackend backend;
	private final TraceeFilterConfiguration filterConfiguration;
	private final HttpHeaderTransport transportSerialization;

	/**
	 * @deprecated use full ctor
	 */
	@Deprecated
	public TraceeHttpRequestInterceptor() {
		this(Profile.DEFAULT);
	}

	/**
	 * @deprecated use full ctor
	 */
	@Deprecated
	public TraceeHttpRequestInterceptor(String profile) {
		this(Tracee.getBackend(), PropertiesBasedTraceeFilterConfiguration.instance().forProfile(profile));
	}

	TraceeHttpRequestInterceptor(TraceeBackend backend, TraceeFilterConfiguration filterConfiguration) {
		this.backend = backend;
		this.filterConfiguration = filterConfiguration;
		this.transportSerialization = new HttpHeaderTransport();
	}

	@Override
	public final void process(final HttpRequest httpRequest, final HttpContext httpContext) {
		if (!backend.isEmpty() && filterConfiguration.shouldProcessContext(OutgoingRequest)) {
			final Map<String, String> filteredParams = filterConfiguration.filterDeniedParams(backend.copyToMap(), OutgoingRequest);
			httpRequest.setHeader(TraceeConstants.TPIC_HEADER, transportSerialization.render(filteredParams));
		}
	}
}
