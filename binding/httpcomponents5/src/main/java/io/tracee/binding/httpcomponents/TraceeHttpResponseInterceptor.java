package io.tracee.binding.httpcomponents;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import io.tracee.configuration.TraceeFilterConfiguration;
import io.tracee.configuration.TraceeFilterConfiguration.Profile;
import io.tracee.transport.HttpHeaderTransport;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.HttpResponseInterceptor;
import org.apache.hc.core5.http.protocol.HttpContext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static io.tracee.configuration.TraceeFilterConfiguration.Channel.IncomingResponse;


public class TraceeHttpResponseInterceptor implements HttpResponseInterceptor {

	private final TraceeBackend backend;
	private final HttpHeaderTransport transportSerialization;
	private final String profile;

	public TraceeHttpResponseInterceptor() {
		this(Profile.DEFAULT);
	}

	public TraceeHttpResponseInterceptor(String profile) {
		this(Tracee.getBackend(), profile);
	}

	TraceeHttpResponseInterceptor(TraceeBackend backend, String profile) {
		this.backend = backend;
		this.profile = profile;
		transportSerialization = new HttpHeaderTransport();
	}

	@Override
	public final void process(HttpResponse response, HttpContext context) {
		final TraceeFilterConfiguration filterConfiguration = backend.getConfiguration(profile);
		final Iterator<Header> headerIterator = response.headerIterator(TraceeConstants.TPIC_HEADER);
		if (headerIterator != null && headerIterator.hasNext() && filterConfiguration.shouldProcessContext(IncomingResponse)) {
			final List<String> stringTraceeHeaders = new ArrayList<>();
			while (headerIterator.hasNext()) {
				stringTraceeHeaders.add(headerIterator.next().getValue());
			}
			backend.putAll(filterConfiguration.filterDeniedParams(transportSerialization.parse(stringTraceeHeaders), IncomingResponse));
		}
	}
}
