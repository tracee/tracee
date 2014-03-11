package de.holisticon.util.tracee.contextlogger.servlet;

import de.holisticon.util.tracee.Tracee;
import de.holisticon.util.tracee.TraceeBackend;
import de.holisticon.util.tracee.contextlogger.connector.ConnectorFactory;
import de.holisticon.util.tracee.contextlogger.json.generator.TraceeContextLoggerJsonBuilder;
import de.holisticon.util.tracee.contextlogger.json.generator.datawrapper.ServletDataWrapper;

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
				throw (ServletException)e;
			} else if (e instanceof IOException) {
				throw (IOException)e;
			}
        }
    }

    private void handleHttpServletRequest(
            HttpServletRequest servletRequest,
            HttpServletResponse servletResponse,
            Exception e) {

        TraceeContextLoggerJsonBuilder errorJsonCreator = TraceeContextLoggerJsonBuilder.createJsonCreator()
                .addServletCategory(ServletDataWrapper.wrap(servletRequest, servletResponse))
                .addPrefixedMessage("TraceeErrorLoggingFilter - FAULT :")
                .addCommonCategory()
                .addExceptionCategory(e)
                .addTraceeCategory(traceeBackend);

        // write message to connectors
        ConnectorFactory.sendErrorReportToConnectors(errorJsonCreator);

    }


    @Override
    public final void destroy() {

    }
}
