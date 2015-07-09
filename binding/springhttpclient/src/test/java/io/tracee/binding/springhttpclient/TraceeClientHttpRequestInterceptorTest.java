package io.tracee.binding.springhttpclient;

import io.tracee.*;
import io.tracee.configuration.TraceeFilterConfiguration;
import io.tracee.testhelper.FieldAccessUtil;
import io.tracee.testhelper.PermitAllTraceeFilterConfiguration;
import io.tracee.testhelper.SimpleTraceeBackend;
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
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TraceeClientHttpRequestInterceptorTest {

	private static final String USED_PROFILE = "A_PROFILE";
	private final HttpHeaderTransport transportSerializationMock = new HttpHeaderTransport();
	private final TraceeBackend backend = new SimpleTraceeBackend(new PermitAllTraceeFilterConfiguration());
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
				headers.add(TraceeConstants.TPIC_HEADER,"fromResponse=true");
				return new SimpleClientHttpResponse(HttpStatus.NO_CONTENT, "yawn", headers);
			}
		});
	}

	@Test
	public void testInterceptPropagatesContextToRequest() throws Exception {
		final HttpRequest request = new SimpleClientHttpRequestFactory().createRequest(URI.create("http://foo.bar"), HttpMethod.GET);
		backend.put("inClientBeforeRequest", "true");
		unit.intercept(request, payload, clientHttpRequestExecutionMock);
		verify(clientHttpRequestExecutionMock).execute(argThat(headers(hasEntry(TraceeConstants.TPIC_HEADER, "inClientBeforeRequest=true"))), any(byte[].class));
	}

	@Test
	public void testInterceptReadsContextFromClientResponse() throws Exception {
		final HttpRequest request = new SimpleClientHttpRequestFactory().createRequest(URI.create("http://foo.bar"), HttpMethod.GET);
		unit.intercept(request, payload, clientHttpRequestExecutionMock);
		assertThat(backend.get("fromResponse"), is("true"));
	}

	final Matcher<HttpRequest> headers(Matcher<Map<? extends String, ? extends String>> submatcher) {
		return new FeatureMatcher<HttpRequest, Map<? extends String,? extends String>>(submatcher,"header", "header map") {
			@Override
			protected Map<String, String> featureValueOf(HttpRequest httpRequest) {
				return httpRequest.getHeaders().toSingleValueMap();
			}
		};
	}

	@Test
	public void defaultConstructorUsesDefaultProfile() {
		final TraceeClientHttpRequestInterceptor interceptor = new TraceeClientHttpRequestInterceptor();
		MatcherAssert.assertThat((String) FieldAccessUtil.getFieldVal(interceptor, "profile"), is(TraceeFilterConfiguration.Profile.DEFAULT));
	}

	@Test
	public void defaultConstructorUsesTraceeBackend() {
		final TraceeClientHttpRequestInterceptor interceptor = new TraceeClientHttpRequestInterceptor();
		MatcherAssert.assertThat((TraceeBackend) FieldAccessUtil.getFieldVal(interceptor, "backend"), is(Tracee.getBackend()));
	}

	@Test
	public void constructorStoresProfileNameInternal() {
		final TraceeClientHttpRequestInterceptor interceptor = new TraceeClientHttpRequestInterceptor("testProf");
		MatcherAssert.assertThat((String) FieldAccessUtil.getFieldVal(interceptor, "profile"), is("testProf"));
	}

}
