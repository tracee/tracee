package io.tracee.binding.httpcomponents;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import io.tracee.configuration.PropertiesBasedTraceeFilterConfiguration;
import io.tracee.configuration.TraceeFilterConfiguration;
import io.tracee.testhelper.FieldAccessUtil;
import io.tracee.testhelper.PermitAllTraceeFilterConfiguration;
import io.tracee.testhelper.SimpleTraceeBackend;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.HttpVersion;
import org.apache.hc.core5.http.message.BasicHttpResponse;
import org.apache.hc.core5.http.message.BasicStatusLine;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.Mockito.mock;

public class TraceeHttpResponseInterceptorTest {

	private final SimpleTraceeBackend backend = new SimpleTraceeBackend();
	private final TraceeFilterConfiguration filterConfiguration = PermitAllTraceeFilterConfiguration.INSTANCE;
	private final TraceeHttpResponseInterceptor unit = new TraceeHttpResponseInterceptor(backend, filterConfiguration);

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
		assertThat((TraceeFilterConfiguration) FieldAccessUtil.getFieldVal(injector, "filterConfiguration"),
			sameInstance(PropertiesBasedTraceeFilterConfiguration.instance().DEFAULT));
	}

	@Test
	public void defaultConstructorUsesTraceeBackend() {
		final TraceeHttpResponseInterceptor injector = new TraceeHttpResponseInterceptor();
		assertThat((TraceeBackend) FieldAccessUtil.getFieldVal(injector, "backend"), is(Tracee.getBackend()));
	}

	@Test
	public void constructorStoresProfileNameInternal() {
		final TraceeHttpResponseInterceptor injector = new TraceeHttpResponseInterceptor("testProf");
		assertThat((TraceeFilterConfiguration) FieldAccessUtil.getFieldVal(injector, "filterConfiguration"),
			sameInstance(PropertiesBasedTraceeFilterConfiguration.instance().forProfile("testProf")));
	}
}
