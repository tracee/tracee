package de.holisticon.util.tracee.outbound.httpclient;

import de.holisticon.util.tracee.Tracee;
import de.holisticon.util.tracee.TraceeBackend;
import de.holisticon.util.tracee.TraceeConstants;
import de.holisticon.util.tracee.transport.HttpJsonHeaderTransport;
import de.holisticon.util.tracee.transport.TransportSerialization;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.params.HttpClientParams;

import java.io.IOException;
import java.util.Map;

import static de.holisticon.util.tracee.configuration.TraceeFilterConfiguration.Channel.IncomingResponse;
import static de.holisticon.util.tracee.configuration.TraceeFilterConfiguration.Channel.OutgoingRequest;

/**
 * Wraps an HttpClient and performs TraceEE context propagation on execute methods.
 * @author Daniel Wegener (Holisticon AG)
 */
public class TraceeHttpClientDecorator extends HttpClient {

	private final HttpClient delegate;
	private final TraceeBackend backend;
	private final TransportSerialization<String> transportSerialization;


	public static HttpClient wrap(HttpClient httpClient) {
		if (httpClient instanceof TraceeHttpClientDecorator) {
			return httpClient;
		} else {
			return new TraceeHttpClientDecorator(httpClient);
		}
	}

	TraceeHttpClientDecorator(HttpClient delegate, TraceeBackend backend, TransportSerialization<String> transportSerialization) {
		this.delegate = delegate;
		this.backend = backend;
		this.transportSerialization = transportSerialization;
	}

	TraceeHttpClientDecorator(HttpClient delegate, TraceeBackend backend) {
		this(delegate, backend, new HttpJsonHeaderTransport(backend.getLoggerFactory()));
	}

	public TraceeHttpClientDecorator(HttpClient delegate) {
		this(delegate, Tracee.getBackend());
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
		if (!backend.isEmpty() && backend.getConfiguration().shouldProcessContext(OutgoingRequest)) {
			final Map<String, String> filteredParams = backend.getConfiguration().filterDeniedParams(backend, OutgoingRequest);
			final String contextAsHeader = transportSerialization.render(filteredParams);
			httpMethod.setRequestHeader(TraceeConstants.HTTP_HEADER_NAME, contextAsHeader);
		}
	}


	private void postResponse(HttpMethod httpMethod) {
		if (!httpMethod.isRequestSent()) return;
		final Header[] responseHeaders = httpMethod.getResponseHeaders(TraceeConstants.HTTP_HEADER_NAME);

		if (responseHeaders.length > 0 && backend.getConfiguration().shouldProcessContext(IncomingResponse)) {
			final String serializedContext = responseHeaders[0].getValue();

			final Map<String, String> parsedContext = transportSerialization.parse(serializedContext);
			backend.putAll(backend.getConfiguration().filterDeniedParams(parsedContext, IncomingResponse));
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
