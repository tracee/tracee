package de.holisticon.util.tracee.servlet;

import de.holisticon.util.tracee.Tracee;
import de.holisticon.util.tracee.TraceeConstants;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 *
 * <h2>Configuration</h2>
 * You may configure the following init-parameters in servlet xml.
 * <dl>
 *     <dt>{@code de.holisticon.util.tracee.servlet.TraceeFilter.headerName}</dt>
 *     <dd>The name of the request header that may contain the request data (Defaults to {@code X-TracEE-Context}).</dd>
 *     <dt>{@code }</dt>
 *     <dd></dd>
 * </dl>
 *
 * @author Daniel
 */
public class TraceeFilter implements Filter {

    public static final String REQUEST_ID_ATTRIBUTE_KEY = "de.holisticon.util.tracee.servlet.REQUEST_ID";
    public static final String HEADER_NAME_PARAM_KEY = "de.holisticon.util.tracee.servlet.TraceeFilter.headerName";
    public static final String HEADER_NAME_DEFAULT = "X-TracEE-Context";


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest) {
            buildRequestContext((HttpServletRequest) servletRequest);
            buildSessionContext((HttpServletRequest)servletRequest);
        }
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            if (servletRequest instanceof HttpServletRequest) {
                closeContext();
            }
        }
    }


    private void buildSessionContext(HttpServletRequest request) {
        final HttpSession session = request.getSession(false);
        final String sessionId = session != null ? session.getId() : null;
        Tracee.getBackend().put(TraceeConstants.SESSION_ID_KEY, sessionId);
    }

    private void buildRequestContext(HttpServletRequest request) {
        final String requestContextFromHeader = request.getHeader(headerName);
        final String requestContextFromRequest = (String)request.getAttribute(REQUEST_ID_ATTRIBUTE_KEY);

        Tracee.getBackend().

    }

    private String getRandomHexString(int numchars){
        Random r = ThreadLocalRandom.current();
        return new UUID(r.nextLong(), r.nextLong()).toString().replace("-","");
    }



    private void closeContext() {
        Tracee.getBackend().clear();
    }

    private String headerName;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //ensure spi scanning on filter initialization
        Tracee.getBackend();
        final String configuredHeaderName = filterConfig.getInitParameter(HEADER_NAME_PARAM_KEY);
        headerName = (configuredHeaderName != null)? configuredHeaderName: HEADER_NAME_DEFAULT;
    }


    @Override
    public void destroy() {
    }
}
