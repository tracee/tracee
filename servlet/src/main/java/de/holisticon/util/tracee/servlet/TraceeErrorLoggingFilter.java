package de.holisticon.util.tracee.servlet;

import de.holisticon.util.tracee.*;
import de.holisticon.util.tracee.errorlogger.json.generator.TraceeErrorLoggerJsonCreator;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
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

        RuntimeException tmpException = null;

        try {
            filterChain.doFilter(servletRequest,servletResponse);
        } catch (RuntimeException e) {
            tmpException = e;

            if (servletRequest instanceof HttpServletRequest && servletResponse instanceof HttpServletResponse) {
                handleHttpServletRequest((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, e);
            }

        }

        if (tmpException != null) {
            throw tmpException;
        }

    }

    private final void handleHttpServletRequest(
            HttpServletRequest servletRequest,
            HttpServletResponse servletResponse,
            Exception e) {


        String errorJson = TraceeErrorLoggerJsonCreator.createJsonCreator().addServletCategory(servletRequest,servletResponse)
                .addCommonCategory()
                .addExceptionCategory(e)
                .addTraceeCategory(traceeBackend)
                .createJson();

        // write log message
        traceeLogger.error("TraceeErrorLoggingFilter - FAULT :\n "
                + errorJson);

    }


    @Override
    public final void destroy() {

    }
}
