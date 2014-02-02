package de.holisticon.util.tracee.outbound.httpclient;

import de.holisticon.util.tracee.Tracee;
import de.holisticon.util.tracee.TraceeBackend;
import de.holisticon.util.tracee.TraceeConstants;
import de.holisticon.util.tracee.transport.HttpJsonHeaderTransport;
import de.holisticon.util.tracee.transport.TransportSerialization;
import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
public class TraceeHttpResponseInterceptor implements HttpResponseInterceptor {


    private final TraceeBackend backend = Tracee.getBackend();
    private final TransportSerialization<String> transportSerialization = new HttpJsonHeaderTransport();

    @Override
    public void process(HttpResponse response, HttpContext context) throws HttpException, IOException {
        final Header traceeHeader = response.getFirstHeader(TraceeConstants.HTTP_HEADER_NAME);
        if (traceeHeader != null) {
            transportSerialization.mergeToBackend(backend, traceeHeader.getValue());
        }
    }
}
