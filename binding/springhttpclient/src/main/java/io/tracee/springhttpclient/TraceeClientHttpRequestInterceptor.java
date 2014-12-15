package io.tracee.springhttpclient;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import io.tracee.configuration.TraceeFilterConfiguration;
import io.tracee.transport.HttpJsonHeaderTransport;
import io.tracee.transport.TransportSerialization;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static io.tracee.configuration.TraceeFilterConfiguration.Channel.IncomingResponse;
import static io.tracee.configuration.TraceeFilterConfiguration.Channel.OutgoingRequest;

public final class TraceeClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {

	private final TraceeBackend backend;
	private final TransportSerialization<String> transportSerialization;
	private final String profile;

	public TraceeClientHttpRequestInterceptor() {
		this(Tracee.getBackend(), new HttpJsonHeaderTransport(Tracee.getBackend().getLoggerFactory()), null);
	}

	public TraceeClientHttpRequestInterceptor(String profile) {
		this(Tracee.getBackend(), new HttpJsonHeaderTransport(Tracee.getBackend().getLoggerFactory()), profile);
	}

	protected TraceeClientHttpRequestInterceptor(TraceeBackend backend, TransportSerialization<String> transportSerialization, String profile) {
		this.backend = backend;
		this.transportSerialization = transportSerialization;
		this.profile = profile;
	}

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
		preRequest(request);
		final ClientHttpResponse response = execution.execute(request, body);
		postResponse(response);
		return response;
	}

	private void preRequest(HttpRequest request) {
		final TraceeFilterConfiguration filterConfiguration = backend.getConfiguration(profile);
		if (!backend.isEmpty() && filterConfiguration.shouldProcessContext(OutgoingRequest)) {
			final Map<String, String> filteredParams = filterConfiguration.filterDeniedParams(backend, OutgoingRequest);
			final String contextAsHeader = transportSerialization.render(filteredParams);
			request.getHeaders().add(TraceeConstants.HTTP_HEADER_NAME, contextAsHeader);
		}
	}

	private void postResponse(ClientHttpResponse response) {
		final List<String> headers = response.getHeaders().get(TraceeConstants.HTTP_HEADER_NAME);
		if (headers != null) {
			final TraceeFilterConfiguration filterConfiguration = backend.getConfiguration(profile);

			if (filterConfiguration.shouldProcessContext(IncomingResponse)) {
				for (String header : headers) {
					final Map<String, String> parsedContext = transportSerialization.parse(header);
					backend.putAll(filterConfiguration.filterDeniedParams(parsedContext, IncomingResponse));
				}
			}
		}
	}
}
