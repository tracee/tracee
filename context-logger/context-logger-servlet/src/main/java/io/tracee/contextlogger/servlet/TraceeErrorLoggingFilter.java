package io.tracee.contextlogger.servlet;

import io.tracee.contextlogger.ImplicitContext;
import io.tracee.contextlogger.builder.TraceeContextLogger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet filter to detect uncaught exceptions and to provide some contextual error logs.
 * Created by Tobias Gindler, holisticon AG on 11.12.13.
 */
public class TraceeErrorLoggingFilter implements Filter {

	static final String LOGGING_PREFIX_MESSAGE = "TRACEE SERVLET ERROR CONTEXT LOGGING LISTENER  : ";

	@Override
    public final void init(FilterConfig filterConfig) throws ServletException {

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

        TraceeContextLogger.createDefault().logJsonWithPrefixedMessage(LOGGING_PREFIX_MESSAGE,
				ImplicitContext.COMMON, ImplicitContext.TRACEE, servletRequest, servletResponse, servletRequest.getSession(false), e);

    }

    @Override
    public final void destroy() {

    }
}
