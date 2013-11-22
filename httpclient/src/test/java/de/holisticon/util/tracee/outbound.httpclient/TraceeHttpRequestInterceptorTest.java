package de.holisticon.util.tracee.outbound.httpclient;

import de.holisticon.util.tracee.TraceeConstants;
import org.apache.http.HttpRequest;
import org.apache.http.protocol.HttpContext;
import org.junit.Test;

import static org.mockito.Matchers.contains;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author Daniel
 */
public class TraceeHttpRequestInterceptorTest {

    final TraceeHttpRequestInterceptor unit = new TraceeHttpRequestInterceptor();

    @Test
    public void testProcess() throws Exception {
        final HttpRequest httpRequest = mock(HttpRequest.class);
        unit.process(httpRequest , mock(HttpContext.class));
        verify(httpRequest).addHeader(eq(TraceeConstants.HTTP_HEADER_NAME), contains(""));


    }


}
