package io.tracee.outbound.httpclient;


import io.tracee.SimpleTraceeBackend;
import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import io.tracee.transport.HttpHeaderTransport;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Arrays;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMapOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TraceeHttpClientDecoratorTest {

	private final TraceeBackend backendMock = spy(SimpleTraceeBackend.createNonLoggingAllPermittingBackend());
	private final HttpMethod httpMethodMock = mock(HttpMethod.class);
	private final HttpHeaderTransport transportSerializationMock = mock(HttpHeaderTransport.class);
	private final HttpClient delegate = mock(HttpClient.class);
	private final TraceeHttpClientDecorator unit = new TraceeHttpClientDecorator(delegate, backendMock, transportSerializationMock, null);

	@Test
	public void doNotRenderTpicHeaderIfBackendIsEmpty() throws IOException {
		unit.executeMethod(null, httpMethodMock, null);
		verify(httpMethodMock, never()).setRequestHeader(anyString(), anyString());
	}

	@Test
	public void testContextWrittenToRequest() throws IOException {
		backendMock.put("foo", "bar");
		when(transportSerializationMock.render(anyMapOf(String.class, String.class))).thenReturn("foo=bar");
		unit.executeMethod(null, httpMethodMock, null);
		verify(httpMethodMock).setRequestHeader(eq(TraceeConstants.HTTP_HEADER_NAME), eq("foo=bar"));
	}

	@Test
	public void testContextParsedFromResponse() throws IOException {
		final Header responseHeader = new Header(TraceeConstants.HTTP_HEADER_NAME, "foo=bar");
		final Header[] responseHeaders = new Header[] { responseHeader };
		when(httpMethodMock.getResponseHeaders(eq(TraceeConstants.HTTP_HEADER_NAME))).thenReturn(responseHeaders);
		when(httpMethodMock.isRequestSent()).thenReturn(true);

		unit.executeMethod(null, httpMethodMock, null);

		verify(transportSerializationMock).parse(eq(Arrays.asList("foo=bar")));
		verify(backendMock).putAll(Mockito.anyMapOf(String.class, String.class));
	}

	@Test
	public void executeMethodDelegates() throws IOException {
		unit.executeMethod(httpMethodMock);
		verify(delegate).executeMethod(any(HostConfiguration.class),eq(httpMethodMock),any(HttpState.class));
	}

	@Test
	public void executeMethodDelegatesWithTwoParams() throws IOException {
		unit.executeMethod(mock(HostConfiguration.class),httpMethodMock);
		verify(delegate).executeMethod(any(HostConfiguration.class),eq(httpMethodMock),any(HttpState.class));
	}

	@Test
	public void setStateDelegates() throws IOException {
		final HttpState httpState = mock(HttpState.class);
		unit.setState(httpState);
		verify(delegate).setState(eq(httpState));
	}

	@Test
	public void setStrictModeDelegates() throws IOException {
		unit.setStrictMode(true);
		verify(delegate).setStrictMode(true);
	}

	@Test
	public void getStateDelegates() throws IOException {
		final HttpState httpState = mock(HttpState.class);
		when(delegate.getState()).thenReturn(httpState);
		assertThat(unit.getState(), equalTo(httpState));
	}

	@Test
	public void setTimeoutDelegates() throws IOException {
		unit.setTimeout(42);
		verify(delegate).setTimeout(42);
	}

	@Test
	public void setHttpConnectionFactoryTimeoutDelegates() throws IOException {
		unit.setHttpConnectionFactoryTimeout(42L);
		verify(delegate).setHttpConnectionFactoryTimeout(42L);
	}

	@Test
	public void setConnectionTimeoutDelegates() throws IOException {
		unit.setConnectionTimeout(42);
		verify(delegate).setConnectionTimeout(42);
	}

	@Test
	public void getHostDelegates() throws IOException {
		when(delegate.getHost()).thenReturn("host");
		assertThat(unit.getHost(), equalTo("host"));
	}

	@Test
	public void getPortDelegates() throws IOException {
		when(delegate.getPort()).thenReturn(42);
		assertThat(unit.getPort(), equalTo(42));
	}

	@Test
	public void getHostConfigurationDelegates() throws IOException {
		final HostConfiguration hostConfiguration = mock(HostConfiguration.class);
		when(delegate.getHostConfiguration()).thenReturn(hostConfiguration);
		assertThat(unit.getHostConfiguration(), equalTo(hostConfiguration));
	}

	@Test
	public void setHostConfigurationDelegates() throws IOException {
		final HostConfiguration hostConfiguration = mock(HostConfiguration.class);
		unit.setHostConfiguration(hostConfiguration);
		verify(delegate).setHostConfiguration(eq(hostConfiguration));
	}

	@Test
	public void getHttpConnectionManagerDelegates() throws IOException {
		final HttpConnectionManager httpConnectionManager = mock(HttpConnectionManager.class);
		when(delegate.getHttpConnectionManager()).thenReturn(httpConnectionManager);
		assertThat(unit.getHttpConnectionManager(), equalTo(httpConnectionManager));
	}

	@Test
	public void setHttpConnectionManagerDelegates() throws IOException {
		final HttpConnectionManager httpConnectionManager = mock(HttpConnectionManager.class);
		unit.setHttpConnectionManager(httpConnectionManager);
		verify(delegate).setHttpConnectionManager(eq(httpConnectionManager));
	}

	@Test
	public void getParamsDelegates() throws IOException {
		final HttpClientParams httpClientParams = mock(HttpClientParams.class);
		when(delegate.getParams()).thenReturn(httpClientParams);
		assertThat(unit.getParams(), equalTo(httpClientParams));
	}

	@Test
	public void setParamsDelegates() throws IOException {
		final HttpClientParams httpClientParams = mock(HttpClientParams.class);
		unit.setParams(httpClientParams);
		verify(delegate).setParams(eq(httpClientParams));
	}


}
