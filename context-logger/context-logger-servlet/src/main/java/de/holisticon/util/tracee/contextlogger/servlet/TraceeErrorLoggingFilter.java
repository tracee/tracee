package de.holisticon.util.tracee.contextlogger.servlet;

import de.holisticon.util.tracee.Tracee;
import de.holisticon.util.tracee.TraceeBackend;
import de.holisticon.util.tracee.contextlogger.ImplicitContext;
import de.holisticon.util.tracee.contextlogger.builder.TraceeContextLogger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet filter to detect uncaught exceptions and to provide some contextual error logs.
 * Created by Tobias Gindler, holisticon AG on 11.12.13.
 */
public class TraceeErrorLoggingFilter implements Filter {


    private TraceeBackend traceeBackend = null;

    @Override
    public final void init(FilterConfig filterConfig) throws ServletException {
        traceeBackend = Tracee.getBackend();
    }

    @Override
    public final void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Exception e) {
            if (servletRequest instanceof HttpServletRequest) {
                handleHttpServletRequest((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, e);
            }

            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            } else if (e instanceof ServletException) {
                throw (ServletException) e;
            } else if (e instanceof IOException) {
                throw (IOException) e;
            }
        }
    }

    private void handleHttpServletRequest(
            HttpServletRequest servletRequest,
            HttpServletResponse servletResponse,
            Exception e) {

        TraceeContextLogger.createDefault().logJsonWithPrefixedMessage("TRACEE SERVLET ERROR CONTEXT LOGGING LISTENER  : ", ImplicitContext.COMMON, ImplicitContext.TRACEE, servletRequest, servletResponse, e);

    }


    @Override
    public final void destroy() {

    }
}
