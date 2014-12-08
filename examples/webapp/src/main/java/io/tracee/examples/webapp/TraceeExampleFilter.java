package io.tracee.examples.webapp;

import io.tracee.Tracee;
import io.tracee.TraceeConstants;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Catches uncaught exceptions and does some redirecting.
 * Created by TGI on 20.11.2014.
 */
@WebFilter(urlPatterns = "*")
public class TraceeExampleFilter implements Filter {

	@Override
	public void init(final FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {

		try {
			filterChain.doFilter(servletRequest, servletResponse);
		} catch (Exception e) {

			Tracee.getBackend().get(TraceeConstants.REQUEST_ID_KEY);

			// try to send redirect
			HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
			httpServletResponse.sendRedirect("index.jsf?showError=true&rid=" + Tracee.getBackend().get(TraceeConstants.REQUEST_ID_KEY));

		}

	}

	@Override
	public void destroy() {

	}
}
