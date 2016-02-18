package io.tracee.binding.httpclient;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import io.tracee.configuration.TraceeFilterConfiguration;
import io.tracee.transport.HttpHeaderTransport;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.params.HttpClientParams;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.tracee.configuration.TraceeFilterConfiguration.Channel.IncomingResponse;
import static io.tracee.configuration.TraceeFilterConfiguration.Channel.OutgoingRequest;
import static io.tracee.configuration.TraceeFilterConfiguration.Profile.*;

/**
 * Wraps an HttpClient and performs TraceEE context propagation on execute methods.
 */
public class TraceeHttpClientDecorator extends HttpClient {

	private final HttpClient delegate;
	private final TraceeBackend backend;
	private final HttpHeaderTransport transportSerialization;
	private final String profile;

	public static HttpClient wrap(HttpClient httpClient) {
		return wrap(httpClient, DEFAULT);
	}

	public static HttpClient wrap(HttpClient httpClient, String profile) {
		if (httpClient instanceof TraceeHttpClientDecorator) {
			return httpClient;
		} else {
			return new TraceeHttpClientDecorator(httpClient, profile);
		}
	}

	TraceeHttpClientDecorator(HttpClient delegate, TraceeBackend backend, HttpHeaderTransport transportSerialization, String profile) {
		this.delegate = delegate;
		this.backend = backend;
		this.transportSerialization = transportSerialization;
		this.profile = profile;
	}

	TraceeHttpClientDecorator(HttpClient delegate, TraceeBackend backend, String profile) {
		this(delegate, backend, new HttpHeaderTransport(), profile);
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
			final Map<String, String> filteredParams = filterConfiguration.filterDeniedParams(backend.copyToMap(), OutgoingRequest);
			httpMethod.setRequestHeader(TraceeConstants.TPIC_HEADER, transportSerialization.render(filteredParams));
		}
	}


	private void postResponse(HttpMethod httpMethod) {
		if (!httpMethod.isRequestSent()) return;
		final Header[] responseHeaders = httpMethod.getResponseHeaders(TraceeConstants.TPIC_HEADER);
		final TraceeFilterConfiguration filterConfiguration = backend.getConfiguration(profile);
		if (responseHeaders != null && responseHeaders.length > 0 && filterConfiguration.shouldProcessContext(IncomingResponse)) {
			final List<String> stringTraceeHeaders = new ArrayList<>();
			for (Header header : responseHeaders) {
				stringTraceeHeaders.add(header.getValue());
			}

			backend.putAll(filterConfiguration.filterDeniedParams(transportSerialization.parse(stringTraceeHeaders), IncomingResponse));
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
	@SuppressWarnings("deprecation")
	public synchronized void setStrictMode(boolean strictMode) {
		delegate.setStrictMode(strictMode);
	}

	@Override
	@SuppressWarnings("deprecation")
	public synchronized boolean isStrictMode() {
		return delegate.isStrictMode();
	}

	@Override
	@SuppressWarnings("deprecation")
	public synchronized void setTimeout(int newTimeoutInMilliseconds) {
		delegate.setTimeout(newTimeoutInMilliseconds);
	}

	@Override
	@SuppressWarnings("deprecation")
	public synchronized void setHttpConnectionFactoryTimeout(long timeout) {
		delegate.setHttpConnectionFactoryTimeout(timeout);
	}

	@Override
	@SuppressWarnings("deprecation")
	public synchronized void setConnectionTimeout(int newTimeoutInMilliseconds) {
		delegate.setConnectionTimeout(newTimeoutInMilliseconds);
	}

	@Override
	@SuppressWarnings("deprecation")
	public String getHost() {
		return delegate.getHost();
	}

	@Override
	@SuppressWarnings("deprecation")
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
