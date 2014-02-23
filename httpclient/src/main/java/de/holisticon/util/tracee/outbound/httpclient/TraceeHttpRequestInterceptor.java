package de.holisticon.util.tracee.outbound.httpclient;

import de.holisticon.util.tracee.Tracee;
import de.holisticon.util.tracee.TraceeBackend;
import de.holisticon.util.tracee.TraceeConstants;
import de.holisticon.util.tracee.configuration.TraceeFilterConfiguration;
import de.holisticon.util.tracee.transport.HttpJsonHeaderTransport;
import de.holisticon.util.tracee.transport.TransportSerialization;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.util.Map;

import static de.holisticon.util.tracee.configuration.TraceeFilterConfiguration.Channel.OutgoingRequest;

/**
 * TO DO: how to use it.
 *
 * @author Daniel Wegener (Holisticon AG)
 */
public class TraceeHttpRequestInterceptor implements HttpRequestInterceptor {

	public TraceeHttpRequestInterceptor() {
		this(Tracee.getBackend());
	}

	TraceeHttpRequestInterceptor(TraceeBackend backend) {
		this.backend = backend;
		this. transportSerialization =  new HttpJsonHeaderTransport(backend.getLoggerFactory());
	}


	private final TraceeBackend backend;
	private final TransportSerialization<String> transportSerialization;

	@Override
	public final void process(HttpRequest httpRequest, HttpContext httpContext) throws HttpException, IOException {

		if (!backend.isEmpty() && backend.getConfiguration().shouldProcessContext(OutgoingRequest)) {
			final Map<String,String> filteredParams = backend.getConfiguration().filterDeniedParams(backend, OutgoingRequest);
			final String contextAsHeader = transportSerialization.render(filteredParams);
			httpRequest.setHeader(TraceeConstants.HTTP_HEADER_NAME, contextAsHeader);
		}

	}

}
