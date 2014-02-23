package de.holisticon.util.tracee.outbound.httpclient;

import de.holisticon.util.tracee.SimpleTraceeBackend;
import de.holisticon.util.tracee.Tracee;
import de.holisticon.util.tracee.TraceeBackend;
import de.holisticon.util.tracee.TraceeConstants;
import org.apache.http.*;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.apache.http.protocol.HttpContext;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
public class TraceeHttpResponseInterceptorTest {

	final SimpleTraceeBackend backend = SimpleTraceeBackend.createNonLoggingAllPermittingBackend();
    final TraceeHttpResponseInterceptor unit = new TraceeHttpResponseInterceptor(backend);

    @Test
    public void testProcess() throws Exception {
        final HttpResponse httpResponse = new BasicHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, 404, "not found"));
        httpResponse.setHeader(TraceeConstants.HTTP_HEADER_NAME, "{\"foo\":\"bar\"}");
        unit.process(httpResponse, mock(HttpContext.class));
        assertThat(backend.get("foo"), equalTo("bar"));
    }


}
