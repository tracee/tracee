package de.holisticon.util.tracee.outbound.httpclient;


import de.holisticon.util.tracee.TraceeBackend;
import de.holisticon.util.tracee.TraceeConstants;
import de.holisticon.util.tracee.configuration.TraceeFilterConfiguration;
import de.holisticon.util.tracee.transport.TransportSerialization;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TraceeHttpClientDecoratorTest {

	private static final String USED_PROFILE = "A_PROFILE";
	private final HttpClient delegateMock = mock(HttpClient.class);
	private final TraceeBackend backendMock = mock(TraceeBackend.class);
	private final TraceeFilterConfiguration configMock = mock(TraceeFilterConfiguration.class);
	private final HttpMethod httpMethodMock = mock(HttpMethod.class);
	@SuppressWarnings("unchecked")
	private final TransportSerialization<String> transportSerializationMock = mock(TransportSerialization.class);
	private final TraceeHttpClientDecorator unit = new TraceeHttpClientDecorator(delegateMock, backendMock, transportSerializationMock, USED_PROFILE);

	@Before
	public void setupMocks() {
		when(backendMock.getConfiguration(eq(USED_PROFILE))).thenReturn(configMock);
		when(configMock.shouldProcessContext(any(TraceeFilterConfiguration.Channel.class))).thenReturn(true);
	}

	@Test
	public void testContextWrittenToRequest() throws IOException {
		when(transportSerializationMock.render(any(TraceeBackend.class))).thenReturn("{ \"foo\":\"bar\" }");
		unit.executeMethod(null, httpMethodMock, null);
		verify(httpMethodMock).setRequestHeader(eq(TraceeConstants.HTTP_HEADER_NAME), eq("{ \"foo\":\"bar\" }"));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testContextParsedFromResponse() throws IOException {
		final Header responseHeader = new Header(TraceeConstants.HTTP_HEADER_NAME, "{ \"foo\":\"bar\" }");
		final Header[] responseHeaders = new Header[] { responseHeader };
		when(httpMethodMock.getResponseHeaders(eq(TraceeConstants.HTTP_HEADER_NAME))).thenReturn(responseHeaders);
		when(httpMethodMock.isRequestSent()).thenReturn(true);

		unit.executeMethod(null, httpMethodMock, null);

		verify(transportSerializationMock).parse(eq("{ \"foo\":\"bar\" }"));
		verify(backendMock).putAll(Mockito.<String, String>anyMap());
	}

}