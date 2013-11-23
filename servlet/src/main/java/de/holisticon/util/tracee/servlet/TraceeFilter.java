package de.holisticon.util.tracee.servlet;

import de.holisticon.util.tracee.Tracee;
import de.holisticon.util.tracee.TraceeBackend;
import de.holisticon.util.tracee.TraceeConstants;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * <h2>Configuration</h2>
 * You may configure the following init-parameters in servlet xml.
 * <ul>
 * <li>{@link de.holisticon.util.tracee.servlet.TraceeFilter#ACCEPT_INCOMING_CONTEXT_KEY}</li>
 * <li>{@link de.holisticon.util.tracee.servlet.TraceeFilter#HEADER_NAME_PARAM_KEY}</li>
 * <li>{@link de.holisticon.util.tracee.servlet.TraceeFilter#RESPOND_WITH_CONTEXT_KEY}</li>
 * </ul>
 *
 * @author Daniel
 */
public class TraceeFilter implements Filter {

    public static final String TRACEE_CONTEXT_KEY = "de.holisticon.util.tracee.servlet.TRACEE_CONTEXT_KEY";

    /**
     * Init-Param key to.
     */
    public static final String ACCEPT_INCOMING_CONTEXT_KEY =
            "de.holisticon.util.tracee.servlet.TraceeFilter.acceptIncomingContext";

    /**
     * Init-Param to configure if the.
     */
    public static final String RESPOND_WITH_CONTEXT_KEY =
            "de.holisticon.util.tracee.servlet.TraceeFilter.respondWithContextKey";

    /**
     * Init-Param key for the name of the request header that may contain the incoming
     * (defaults to {@link de.holisticon.util.tracee.TraceeConstants#HTTP_HEADER_NAME}).
     */
    public static final String HEADER_NAME_PARAM_KEY = "de.holisticon.util.tracee.servlet.TraceeFilter.headerName";

    private String headerName = TraceeConstants.HTTP_HEADER_NAME;
    private boolean acceptIncomingContext = false;
    private boolean respondWithContext = false;
    private TraceeBackend backend = null;

    @Override
    public final void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest && servletResponse instanceof HttpServletResponse) {
            doFilterHttp((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, filterChain);
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    private void doFilterHttp(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {

        if (acceptIncomingContext)
            mergeIncomingContextToBackend(request);

        if (!backend.contains(TraceeConstants.REQUEST_ID_KEY)) {
            backend.put(TraceeConstants.REQUEST_ID_KEY, generateRandomHexString());
        }

        if (!backend.contains(TraceeConstants.SESSION_ID_KEY)) {
            final HttpSession session = request.getSession(false);
            if (session != null) {
                backend.put(TraceeConstants.SESSION_ID_KEY, anonymizedSessionKey(session.getId()));
            }
        }

        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            // shit happens
        }

        if (respondWithContext && backend.isEmpty())
            response.setHeader(headerName, backend.toHeaderRepresentation());

        backend.clear();
    }

    private String anonymizedSessionKey(String sessionKey) {
        // TO DO: how about some salt?
        return Integer.toHexString(sessionKey.hashCode());
    }

    private void mergeIncomingContextToBackend(HttpServletRequest request) {
        final Enumeration headers = request.getHeaders(headerName);
        if (headers == null) {
            throw new IllegalStateException("Could not read headers with name '"
                    + headerName + "'. The access seem to be forbidden by the container");
        }
        while (headers.hasMoreElements()) {
            backend.merge((String) headers.nextElement());
        }
    }

    private String generateRandomHexString() {
        Random r = ThreadLocalRandom.current();
        return new UUID(r.nextLong(), r.nextLong()).toString().replace("-", "");
    }

    @Override
    public final void init(FilterConfig filterConfig) throws ServletException {
        //ensure spi scanning on filter initialization
        backend = Tracee.getBackend();
        final String configuredHeaderName = filterConfig.getInitParameter(HEADER_NAME_PARAM_KEY);
        if (configuredHeaderName != null)
            headerName = configuredHeaderName;
        if (Boolean.parseBoolean(filterConfig.getInitParameter(ACCEPT_INCOMING_CONTEXT_KEY)))
            acceptIncomingContext = true;
        if (Boolean.parseBoolean(filterConfig.getInitParameter(RESPOND_WITH_CONTEXT_KEY)))
            respondWithContext = true;
    }


    @Override
    public final void destroy() {
    }
}
