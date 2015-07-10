package io.tracee.binding.httpcomponents;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.configuration.TraceeFilterConfiguration;
import io.tracee.testhelper.FieldAccessUtil;
import io.tracee.testhelper.SimpleTraceeBackend;
import io.tracee.TraceeConstants;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.apache.http.protocol.HttpContext;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

public class TraceeHttpResponseInterceptorTest {

	private final SimpleTraceeBackend backend = SimpleTraceeBackend.createNonLoggingAllPermittingBackend();
    private final TraceeHttpResponseInterceptor unit = new TraceeHttpResponseInterceptor(backend, null);

    @Test
    public void testResponseInterceptorParsesHttpHeaderToBackend() throws Exception {
        final HttpResponse httpResponse = new BasicHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, 404, "not found"));
        httpResponse.setHeader(TraceeConstants.TPIC_HEADER, "foobi=bar");
        unit.process(httpResponse, mock(HttpContext.class));
        assertThat(backend.get("foobi"), equalTo("bar"));
    }

	@Test
	public void defaultConstructorUsesDefaultProfile() {
		final TraceeHttpResponseInterceptor injector = new TraceeHttpResponseInterceptor();
		assertThat((String) FieldAccessUtil.getFieldVal(injector, "profile"), is(TraceeFilterConfiguration.Profile.DEFAULT));
	}

	@Test
	public void defaultConstructorUsesTraceeBackend() {
		final TraceeHttpResponseInterceptor injector = new TraceeHttpResponseInterceptor();
		assertThat((TraceeBackend) FieldAccessUtil.getFieldVal(injector, "backend"), is(Tracee.getBackend()));
	}

	@Test
	public void constructorStoresProfileNameInternal() {
		final TraceeHttpResponseInterceptor injector = new TraceeHttpResponseInterceptor("testProf");
		assertThat((String) FieldAccessUtil.getFieldVal(injector, "profile"), is("testProf"));
	}
}
