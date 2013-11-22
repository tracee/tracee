package de.holisticon.util.tracee.outbound.httpclient;

import de.holisticon.util.tracee.Tracee;
import de.holisticon.util.tracee.TraceeConstants;
import de.holisticon.util.tracee.TraceeContext;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;

/**
 * TODO: how to use it
 *
 * @author Daniel
 */
public class TraceeHttpRequestInterceptor implements HttpRequestInterceptor {

    @Override
    public void process(HttpRequest httpRequest, HttpContext httpContext) throws HttpException, IOException {
        final TraceeContext traceeContext = Tracee.getBackend().extractContext();
        httpRequest.setHeader(TraceeConstants.HTTP_HEADER_NAME, traceeContext.toString());
    }

}
