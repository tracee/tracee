package de.holisticon.util.tracee.servlet;

import de.holisticon.util.tracee.*;
import de.holisticon.util.tracee.errorlogger.json.generator.TraceeErrorLoggerJsonCreator;

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
    private TraceeLogger traceeLogger = null;

    @Override
    public final void init(FilterConfig filterConfig) throws ServletException {

        traceeBackend = Tracee.getBackend();
        traceeLogger = traceeBackend.getLogger(TraceeErrorLoggingFilter.class);

    }

    @Override
    public final void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        Exception tmpException = null;

        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Exception e) {
            tmpException = e;

            if (servletRequest instanceof HttpServletRequest) {
                handleHttpServletRequest((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, e);
            }

        }

        if (tmpException != null) {
            if (tmpException instanceof RuntimeException) {
                 throw (RuntimeException) tmpException;
            } else {
                 throw new RuntimeException(tmpException);
            }
        }

    }

    private void handleHttpServletRequest(
            HttpServletRequest servletRequest,
            HttpServletResponse servletResponse,
            Exception e) {


        TraceeErrorLoggerJsonCreator errorJsonCreator = TraceeErrorLoggerJsonCreator.createJsonCreator().addServletCategory(servletRequest, servletResponse)
                .addPrefixedMessage("TraceeErrorLoggingFilter - FAULT :")
                .addCommonCategory()
                .addExceptionCategory(e)
                .addTraceeCategory(traceeBackend);

        // write log message
        traceeLogger.error(errorJsonCreator);

    }


    @Override
    public final void destroy() {

    }
}
