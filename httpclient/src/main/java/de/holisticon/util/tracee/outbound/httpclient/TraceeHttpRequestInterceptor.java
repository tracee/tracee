package de.holisticon.util.tracee.outbound.httpclient;

import de.holisticon.util.tracee.Tracee;
import de.holisticon.util.tracee.TraceeBackend;
import de.holisticon.util.tracee.TraceeConstants;
import de.holisticon.util.tracee.transport.HttpJsonHeaderTransport;
import de.holisticon.util.tracee.transport.TransportSerialization;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;

/**
 * TO DO: how to use it.
 *
 * @author Daniel Wegener (Holisticon AG)
 */
public class TraceeHttpRequestInterceptor implements HttpRequestInterceptor {

    private final TraceeBackend backend = Tracee.getBackend();
    private final TransportSerialization<String> transportSerialization = new HttpJsonHeaderTransport(backend.getLoggerFactory());

    @Override
    public final void process(HttpRequest httpRequest, HttpContext httpContext) throws HttpException, IOException {

		if (!backend.isEmpty()) {
            final String contextAsHeader = transportSerialization.render(backend);
            httpRequest.setHeader(TraceeConstants.HTTP_HEADER_NAME, contextAsHeader);
        }

    }

}
