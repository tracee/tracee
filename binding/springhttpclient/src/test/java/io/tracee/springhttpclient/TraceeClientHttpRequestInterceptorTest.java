package io.tracee.springhttpclient;

import io.tracee.*;
import io.tracee.transport.HttpHeaderTransport;

import org.hamcrest.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TraceeClientHttpRequestInterceptorTest {

	private static final String USED_PROFILE = "A_PROFILE";
	private final HttpHeaderTransport transportSerializationMock = new HttpHeaderTransport(NoopTraceeLoggerFactory.INSTANCE);
	private final TraceeBackend backend = new SimpleTraceeBackend(new PermitAllTraceeFilterConfiguration(), NoopTraceeLoggerFactory.INSTANCE);
	private final TraceeClientHttpRequestInterceptor unit =
			new TraceeClientHttpRequestInterceptor(backend, transportSerializationMock, USED_PROFILE);
	private final byte[] payload = new byte[] {};
	private final ClientHttpRequestExecution clientHttpRequestExecutionMock = mock(ClientHttpRequestExecution.class);

	@Before
	public void setupMocks() throws IOException {
		when(clientHttpRequestExecutionMock.execute(any(HttpRequest.class), any(byte[].class))).thenAnswer(new Answer<ClientHttpResponse>() {
			@Override
			public ClientHttpResponse answer(InvocationOnMock invocation) throws Throwable {
				final HttpHeaders headers = new HttpHeaders();
				headers.add(TraceeConstants.HTTP_HEADER_NAME,"fromResponse=true");
				return new SimpleClientHttpResponse(HttpStatus.NO_CONTENT, "yawn", headers);
			}
		});

	}

	@Test
	public void testInterceptPropagatesContextToRequest() throws Exception {
		final HttpRequest request = new SimpleClientHttpRequestFactory().createRequest(URI.create("http://foo.bar"), HttpMethod.GET);
		backend.put("inClientBeforeRequest", "true");
		unit.intercept(request, payload, clientHttpRequestExecutionMock);
		verify(clientHttpRequestExecutionMock).execute(argThat(headers(hasEntry(TraceeConstants.HTTP_HEADER_NAME, "inClientBeforeRequest=true"))), any(byte[].class));
	}

	@Test
	public void testInterceptReadsContextFromClientResponse() throws Exception {
		final HttpRequest request = new SimpleClientHttpRequestFactory().createRequest(URI.create("http://foo.bar"), HttpMethod.GET);
		unit.intercept(request, payload, clientHttpRequestExecutionMock);
		assertThat(backend, hasEntry("fromResponse", "true"));
	}

	final Matcher<HttpRequest> headers(Matcher<Map<? extends String, ? extends String>> submatcher) {
		return new FeatureMatcher<HttpRequest, Map<? extends String,? extends String>>(submatcher,"header", "header map") {
			@Override
			protected Map<String, String> featureValueOf(HttpRequest httpRequest) {
				return httpRequest.getHeaders().toSingleValueMap();
			}
		};
	}



}
