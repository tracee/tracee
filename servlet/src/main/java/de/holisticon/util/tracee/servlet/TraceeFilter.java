package de.holisticon.util.tracee.servlet;

import de.holisticon.util.tracee.*;
import de.holisticon.util.tracee.configuration.TraceeFilterConfiguration;
import de.holisticon.util.tracee.transport.HttpJsonHeaderTransport;
import de.holisticon.util.tracee.transport.TransportSerialization;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import static de.holisticon.util.tracee.configuration.TraceeFilterConfiguration.Channel.IncomingRequest;
import static de.holisticon.util.tracee.configuration.TraceeFilterConfiguration.Channel.OutgoingResponse;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
public class TraceeFilter implements Filter {

    private static final String HTTP_HEADER_NAME = TraceeConstants.HTTP_HEADER_NAME;

	// VisibleForTesting
	TraceeBackend backend = null;

	// VisibleForTesting
    TransportSerialization<String> httpJsonHeaderSerialization = null;



    @Override
    public final void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest && servletResponse instanceof HttpServletResponse) {
            doFilterHttp((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, filterChain);
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    private void doFilterHttp(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {


		final TraceeFilterConfiguration configuration = backend.getConfiguration();

		if (configuration.shouldProcessContext(IncomingRequest)) {
			mergeIncomingContextToBackend(request, backend);
		}

        if (configuration.shouldGenerateRequestId() && !backend.containsKey(TraceeConstants.REQUEST_ID_KEY)) {
            backend.put(TraceeConstants.REQUEST_ID_KEY, Utilities.createRandomAlphanumeric(configuration.generatedRequestIdLength()));
        }

        if (configuration.shouldGenerateSessionId() && !backend.containsKey(TraceeConstants.SESSION_ID_KEY)) {
            final HttpSession session = request.getSession(false);
            if (session != null) {
                backend.put(TraceeConstants.SESSION_ID_KEY, anonymizedSessionKey(session.getId(), configuration.generatedSessionIdLength()));
            }
        }

        try {
            filterChain.doFilter(request, response);
            if (configuration.shouldProcessContext(OutgoingResponse) && !backend.isEmpty()) {
				final Map<String, String> filteredContext = backend.getConfiguration().filterDeniedParams(backend, OutgoingResponse);
				response.setHeader(HTTP_HEADER_NAME, httpJsonHeaderSerialization.render(filteredContext));
            }
        } finally {
            // ensure cleanup
            backend.clear();
        }

    }

    private String anonymizedSessionKey(String sessionKey, int length) {
        return Utilities.createAlphanumericHash(sessionKey, length);
    }

    private void mergeIncomingContextToBackend(HttpServletRequest request, TraceeBackend backend) {
        final Enumeration headers = request.getHeaders(HTTP_HEADER_NAME);
        if (headers == null) {
            throw new IllegalStateException("Could not read headers with name '"
                    + HTTP_HEADER_NAME + "'. The access seem to be forbidden by the container.");
        }

		final Map<String,String> parsed = new HashMap<String, String>();
        while (headers.hasMoreElements()) {
			parsed.putAll(httpJsonHeaderSerialization.parse((String) headers.nextElement()));
        }

		final Map<String, String> filtered = backend.getConfiguration().filterDeniedParams(parsed, IncomingRequest);
		backend.putAll(filtered);
    }



    @Override
    public final void init(FilterConfig filterConfig) throws ServletException {
        //ensure spi implementation is available on filter initialization
        backend = Tracee.getBackend();
		httpJsonHeaderSerialization = new HttpJsonHeaderTransport(backend.getLoggerFactory());
    }


    @Override
    public final void destroy() {
		backend = null;
		httpJsonHeaderSerialization = null;
    }
}
