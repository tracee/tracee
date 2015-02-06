package io.tracee.binding.httpclient;


import io.tracee.SimpleTraceeBackend;
import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import io.tracee.transport.HttpHeaderTransport;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Arrays;

import static io.tracee.DelegationTestUtil.assertDelegationToSpy;
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
	private final TraceeHttpClientDecorator unit = new TraceeHttpClientDecorator(mock(HttpClient.class), backendMock, transportSerializationMock, null);

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
		verify(httpMethodMock).setRequestHeader(eq(TraceeConstants.TPIC_HEADER), eq("foo=bar"));
	}

	@Test
	public void testContextParsedFromResponse() throws IOException {
		final Header responseHeader = new Header(TraceeConstants.TPIC_HEADER, "foo=bar");
		final Header[] responseHeaders = new Header[] { responseHeader };
		when(httpMethodMock.getResponseHeaders(eq(TraceeConstants.TPIC_HEADER))).thenReturn(responseHeaders);
		when(httpMethodMock.isRequestSent()).thenReturn(true);

		unit.executeMethod(null, httpMethodMock, null);

		verify(transportSerializationMock).parse(eq(Arrays.asList("foo=bar")));
		verify(backendMock).putAll(Mockito.anyMapOf(String.class, String.class));
	}

	@Test
	public void shouldDelegateAllPublicMethods() {
		final HttpClient client = mock(HttpClient.class);
		final HttpClient wrappedClient = TraceeHttpClientDecorator.wrap(client);

		assertDelegationToSpy(client).by(wrappedClient).ignore("executeMethod").verify();
	}
}
