package io.tracee.binding.httpcomponents;

import io.tracee.SimpleTraceeBackend;
import io.tracee.TraceeConstants;
import org.apache.http.HttpRequest;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.protocol.HttpContext;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;

public class TraceeHttpRequestInterceptorTest {

	private final SimpleTraceeBackend backend = SimpleTraceeBackend.createNonLoggingAllPermittingBackend();
	private final TraceeHttpRequestInterceptor unit = new TraceeHttpRequestInterceptor(backend, null);

    @Test
    public void testRequestInterceptorWritesTraceeContextToRequestHeader() throws Exception {
        final HttpRequest httpRequest = new BasicHttpRequest("GET", "http://localhost/pew");

        backend.put("foo", "bar");

        unit.process(httpRequest, mock(HttpContext.class));

        assertThat("HttpRequest contains TracEE Context Header", httpRequest.containsHeader(TraceeConstants.HTTP_HEADER_NAME), equalTo(true));
        assertThat(httpRequest.getFirstHeader(TraceeConstants.HTTP_HEADER_NAME).getValue(), equalTo("foo=bar"));
    }


}
