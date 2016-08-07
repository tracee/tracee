package io.tracee.binding.springhttpclient;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import io.tracee.configuration.TraceeFilterConfiguration;
import io.tracee.testhelper.FieldAccessUtil;
import io.tracee.testhelper.PermitAllTraceeFilterConfiguration;
import io.tracee.testhelper.SimpleTraceeBackend;
import io.tracee.transport.HttpHeaderTransport;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
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
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TraceeClientHttpRequestInterceptorTest {

	private final HttpHeaderTransport transportSerializationMock = new HttpHeaderTransport();
	private final TraceeBackend backend = new SimpleTraceeBackend();
	private final TraceeFilterConfiguration filterConfiguration = new PermitAllTraceeFilterConfiguration();
	private final TraceeClientHttpRequestInterceptor unit =
			new TraceeClientHttpRequestInterceptor(backend, transportSerializationMock, filterConfiguration);
	private final byte[] payload = new byte[]{};
	private final ClientHttpRequestExecution clientHttpRequestExecutionMock = mock(ClientHttpRequestExecution.class);

	@Before
	public void setupMocks() throws IOException {
		when(clientHttpRequestExecutionMock.execute(any(HttpRequest.class), any(byte[].class))).thenAnswer(new Answer<ClientHttpResponse>() {
			@Override
			public ClientHttpResponse answer(InvocationOnMock invocation) throws Throwable {
				final HttpHeaders headers = new HttpHeaders();
				headers.add(TraceeConstants.TPIC_HEADER, "fromResponse=true");
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

	private Matcher<HttpRequest> headers(Matcher<Map<? extends String, ? extends String>> submatcher) {
		return new FeatureMatcher<HttpRequest, Map<? extends String, ? extends String>>(submatcher, "header", "header map") {
			@Override
			protected Map<String, String> featureValueOf(HttpRequest httpRequest) {
				return httpRequest.getHeaders().toSingleValueMap();
			}
		};
	}

	@Test
	public void defaultConstructorUsesTraceeBackend() {
		final TraceeClientHttpRequestInterceptor interceptor = new TraceeClientHttpRequestInterceptor();
		assertThat((TraceeBackend) FieldAccessUtil.getFieldVal(interceptor, "backend"), is(Tracee.getBackend()));
	}

}
