package io.tracee.binding.springhttpclient;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import io.tracee.configuration.PropertiesBasedTraceeFilterConfiguration;
import io.tracee.configuration.TraceeFilterConfiguration;
import io.tracee.transport.HttpHeaderTransport;
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
	private final HttpHeaderTransport transportSerialization;
	private final TraceeFilterConfiguration filterConfiguration;

	public TraceeClientHttpRequestInterceptor() {
		this(Tracee.getBackend(), new HttpHeaderTransport(), PropertiesBasedTraceeFilterConfiguration.instance().DEFAULT);
	}

	public TraceeClientHttpRequestInterceptor(TraceeFilterConfiguration filterConfiguration) {
		this(Tracee.getBackend(), new HttpHeaderTransport(), filterConfiguration);
	}

	public TraceeClientHttpRequestInterceptor(TraceeBackend backend, HttpHeaderTransport transportSerialization, TraceeFilterConfiguration filterConfiguration) {
		this.backend = backend;
		this.transportSerialization = transportSerialization;
		this.filterConfiguration = filterConfiguration;
	}

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
		preRequest(request);
		final ClientHttpResponse response = execution.execute(request, body);
		postResponse(response);
		return response;
	}

	private void preRequest(final HttpRequest request) {
		if (!backend.isEmpty() && filterConfiguration.shouldProcessContext(OutgoingRequest)) {
			final Map<String, String> filteredParams = filterConfiguration.filterDeniedParams(backend.copyToMap(), OutgoingRequest);
			request.getHeaders().add(TraceeConstants.TPIC_HEADER, transportSerialization.render(filteredParams));
		}
	}

	private void postResponse(ClientHttpResponse response) {
		final List<String> headers = response.getHeaders().get(TraceeConstants.TPIC_HEADER);
		if (headers != null) {
			if (filterConfiguration.shouldProcessContext(IncomingResponse)) {
				backend.putAll(filterConfiguration.filterDeniedParams(transportSerialization.parse(headers), IncomingResponse));
			}
		}
	}
}
