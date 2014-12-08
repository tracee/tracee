package io.tracee.outbound.httpclient;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import io.tracee.configuration.TraceeFilterConfiguration;
import io.tracee.transport.HttpJsonHeaderTransport;
import io.tracee.transport.TransportSerialization;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.params.HttpClientParams;

import java.io.IOException;
import java.util.Map;

import static io.tracee.configuration.TraceeFilterConfiguration.Channel.IncomingResponse;
import static io.tracee.configuration.TraceeFilterConfiguration.Channel.OutgoingRequest;

/**
 * Wraps an HttpClient and performs TraceEE context propagation on execute methods.
 */
public class TraceeHttpClientDecorator extends HttpClient {

	private final HttpClient delegate;
	private final TraceeBackend backend;
	private final TransportSerialization<String> transportSerialization;
	private final String profile;

	public static HttpClient wrap(HttpClient httpClient) {
		return TraceeHttpClientDecorator.wrap(httpClient, null);
	}

	public static HttpClient wrap(HttpClient httpClient, String profile) {
		if (httpClient instanceof TraceeHttpClientDecorator) {
			return httpClient;
		} else {
			return new TraceeHttpClientDecorator(httpClient, profile);
		}
	}

	TraceeHttpClientDecorator(HttpClient delegate, TraceeBackend backend, TransportSerialization<String> transportSerialization, String profile) {
		this.delegate = delegate;
		this.backend = backend;
		this.transportSerialization = transportSerialization;
		this.profile = profile;
	}

	TraceeHttpClientDecorator(HttpClient delegate, TraceeBackend backend, String profile) {
		this(delegate, backend, new HttpJsonHeaderTransport(backend.getLoggerFactory()), profile);
	}

	public TraceeHttpClientDecorator(HttpClient delegate, String profile) {
		this(delegate, Tracee.getBackend(), profile);
	}


	@Override
	public int executeMethod(HttpMethod method) throws IOException, HttpException {
		return this.executeMethod(null, method, null);
	}

	@Override
	public int executeMethod(HostConfiguration hostConfiguration, HttpMethod method) throws IOException, HttpException {
		return this.executeMethod(hostConfiguration, method, null);
	}

	@Override
	public int executeMethod(HostConfiguration hostconfig, HttpMethod method, HttpState state) throws IOException, HttpException {
		preRequest(method);
		try {
			int result = delegate.executeMethod(hostconfig, method, state);
			postResponse(method);
			return result;
		} finally {
			cancel();
		}
	}

	private void cancel() {
		//TODO: do we need to cleanup anything?
	}

	private void preRequest(HttpMethod httpMethod) {
		final TraceeFilterConfiguration filterConfiguration = backend.getConfiguration(profile);
		if (!backend.isEmpty() && filterConfiguration.shouldProcessContext(OutgoingRequest)) {
			final Map<String, String> filteredParams = filterConfiguration.filterDeniedParams(backend, OutgoingRequest);
			final String contextAsHeader = transportSerialization.render(filteredParams);
			httpMethod.setRequestHeader(TraceeConstants.HTTP_HEADER_NAME, contextAsHeader);
		}
	}


	private void postResponse(HttpMethod httpMethod) {
		if (!httpMethod.isRequestSent()) return;
		final Header[] responseHeaders = httpMethod.getResponseHeaders(TraceeConstants.HTTP_HEADER_NAME);
		final TraceeFilterConfiguration filterConfiguration = backend.getConfiguration(profile);
		if (responseHeaders.length > 0 && filterConfiguration.shouldProcessContext(IncomingResponse)) {
			final String serializedContext = responseHeaders[0].getValue();

			final Map<String, String> parsedContext = transportSerialization.parse(serializedContext);
			backend.putAll(filterConfiguration.filterDeniedParams(parsedContext, IncomingResponse));
		}
	}


	@Override
	public synchronized HttpState getState() {
		return delegate.getState();
	}

	@Override
	public synchronized void setState(HttpState state) {
		delegate.setState(state);
	}

	@Override
	public synchronized void setStrictMode(boolean strictMode) {
		delegate.setStrictMode(strictMode);
	}

	@Override
	public synchronized boolean isStrictMode() {
		return delegate.isStrictMode();
	}

	@Override
	public synchronized void setTimeout(int newTimeoutInMilliseconds) {
		delegate.setTimeout(newTimeoutInMilliseconds);
	}

	@Override
	public synchronized void setHttpConnectionFactoryTimeout(long timeout) {
		delegate.setHttpConnectionFactoryTimeout(timeout);
	}

	@Override
	public synchronized void setConnectionTimeout(int newTimeoutInMilliseconds) {
		delegate.setConnectionTimeout(newTimeoutInMilliseconds);
	}

	@Override
	public String getHost() {
		return delegate.getHost();
	}

	@Override
	public int getPort() {
		return delegate.getPort();
	}

	@Override
	public synchronized HostConfiguration getHostConfiguration() {
		return delegate.getHostConfiguration();
	}

	@Override
	public synchronized void setHostConfiguration(HostConfiguration hostConfiguration) {
		delegate.setHostConfiguration(hostConfiguration);
	}

	@Override
	public synchronized HttpConnectionManager getHttpConnectionManager() {
		return delegate.getHttpConnectionManager();
	}

	@Override
	public synchronized void setHttpConnectionManager(HttpConnectionManager httpConnectionManager) {
		delegate.setHttpConnectionManager(httpConnectionManager);
	}

	@Override
	public HttpClientParams getParams() {
		return delegate.getParams();
	}

	@Override
	public void setParams(HttpClientParams params) {
		delegate.setParams(params);
	}
}
