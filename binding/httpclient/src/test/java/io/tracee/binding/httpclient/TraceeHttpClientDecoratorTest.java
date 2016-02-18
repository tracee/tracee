package io.tracee.binding.httpclient;


import io.tracee.testhelper.SimpleTraceeBackend;
import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import io.tracee.transport.HttpHeaderTransport;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Collections;

import static io.tracee.testhelper.DelegationTestUtil.assertDelegationToSpy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
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
	private final TraceeHttpClientDecorator unit = new TraceeHttpClientDecorator(mock(HttpClient.class), backendMock, transportSerializationMock, null);

	@Test
	public void doNotRenderTpicHeaderIfBackendIsEmpty() throws IOException {
		unit.executeMethod(null, httpMethodMock);
		verify(httpMethodMock, never()).setRequestHeader(anyString(), anyString());
	}

	@Test
	public void testContextWrittenToRequest() throws IOException {
		backendMock.put("foo", "bar");
		when(transportSerializationMock.render(anyMapOf(String.class, String.class))).thenReturn("foo=bar");
		unit.executeMethod(httpMethodMock);
		verify(httpMethodMock).setRequestHeader(eq(TraceeConstants.TPIC_HEADER), eq("foo=bar"));
	}

	@Test
	public void testContextParsedFromResponse() throws IOException {
		final Header responseHeader = new Header(TraceeConstants.TPIC_HEADER, "foo=bar");
		final Header[] responseHeaders = new Header[] { responseHeader };
		when(httpMethodMock.getResponseHeaders(eq(TraceeConstants.TPIC_HEADER))).thenReturn(responseHeaders);
		when(httpMethodMock.isRequestSent()).thenReturn(true);

		unit.executeMethod(null, httpMethodMock, null);

		verify(transportSerializationMock).parse(eq(Collections.singletonList("foo=bar")));
		verify(backendMock).putAll(Mockito.anyMapOf(String.class, String.class));
	}

	@Test
	public void shouldDelegateAllPublicMethods() {
		final HttpClient client = mock(HttpClient.class);
		final HttpClient wrappedClient = TraceeHttpClientDecorator.wrap(client);

		assertDelegationToSpy(client).by(wrappedClient).ignore("executeMethod").verify();
	}

	@Test
	public void wrapOnlyWithTraceeHttpClientDecoratorIfInstanceIsNotOfThisType() {
		final HttpClient httpClient = TraceeHttpClientDecorator.wrap(mock(HttpClient.class));
		final HttpClient wrappedAgainHttpClient = TraceeHttpClientDecorator.wrap(httpClient);
		assertThat(httpClient, is(sameInstance(wrappedAgainHttpClient)));
	}
}
