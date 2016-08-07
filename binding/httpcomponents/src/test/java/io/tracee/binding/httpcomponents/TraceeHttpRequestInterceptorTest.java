package io.tracee.binding.httpcomponents;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import io.tracee.configuration.TraceeFilterConfiguration;
import io.tracee.testhelper.FieldAccessUtil;
import io.tracee.testhelper.SimpleTraceeBackend;
import org.apache.http.HttpRequest;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.protocol.HttpContext;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

public class TraceeHttpRequestInterceptorTest {

	private final SimpleTraceeBackend backend = SimpleTraceeBackend.createNonLoggingAllPermittingBackend();
	private final TraceeHttpRequestInterceptor unit = new TraceeHttpRequestInterceptor(backend, null);

	@Test
	public void testRequestInterceptorWritesTraceeContextToRequestHeader() throws Exception {
		final HttpRequest httpRequest = new BasicHttpRequest("GET", "http://localhost/pew");

		backend.put("foo", "bar");

		unit.process(httpRequest, mock(HttpContext.class));

		assertThat("HttpRequest contains TracEE Context Header", httpRequest.containsHeader(TraceeConstants.TPIC_HEADER), equalTo(true));
		assertThat(httpRequest.getFirstHeader(TraceeConstants.TPIC_HEADER).getValue(), equalTo("foo=bar"));
	}

	@Test
	public void defaultConstructorUsesDefaultProfile() {
		final TraceeHttpRequestInterceptor interceptor = new TraceeHttpRequestInterceptor();
		assertThat((String) FieldAccessUtil.getFieldVal(interceptor, "profile"), is(TraceeFilterConfiguration.Profile.DEFAULT));
	}

	@Test
	public void defaultConstructorUsesTraceeBackend() {
		final TraceeHttpRequestInterceptor interceptor = new TraceeHttpRequestInterceptor();
		assertThat((TraceeBackend) FieldAccessUtil.getFieldVal(interceptor, "backend"), is(Tracee.getBackend()));
	}

	@Test
	public void constructorStoresProfileNameInternal() {
		final TraceeHttpRequestInterceptor interceptor = new TraceeHttpRequestInterceptor("testProf");
		assertThat((String) FieldAccessUtil.getFieldVal(interceptor, "profile"), is("testProf"));
	}
}
