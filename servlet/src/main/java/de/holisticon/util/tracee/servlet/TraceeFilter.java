package de.holisticon.util.tracee.servlet;

import de.holisticon.util.tracee.*;
import de.holisticon.util.tracee.configuration.TraceeFilterConfiguration;
import de.holisticon.util.tracee.transport.HttpJsonHeaderTransport;
import de.holisticon.util.tracee.transport.TransportSerialization;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import static de.holisticon.util.tracee.configuration.TraceeFilterConfiguration.Channel.OutgoingResponse;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
public class TraceeFilter implements Filter {

    private static final String HTTP_HEADER_NAME = TraceeConstants.HTTP_HEADER_NAME;

	public TraceeFilter() {
		this(Tracee.getBackend(), new HttpJsonHeaderTransport(Tracee.getBackend().getLoggerFactory()));
	}

	// VisibleForTesting
	TraceeFilter(TraceeBackend backend, TransportSerialization<String> transportSerialization) {
		this.backend = backend;
		this.transportSerialization = transportSerialization;
	}

	private final TraceeBackend backend;

    private final TransportSerialization<String> transportSerialization;

    @Override
    public final void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest && servletResponse instanceof HttpServletResponse) {
            doFilterHttp((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, filterChain);
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    void doFilterHttp(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

		final TraceeFilterConfiguration configuration = backend.getConfiguration();

        try {
			// we need to eagerly write ResponseHeaders since the inner servlets may flush the output stream
			// and writing of response headers become impossible afterwards. This is a best effort trade-off.
			writeContextToResponse(response, configuration);
			filterChain.doFilter(request, response);
        } finally {
			if (!response.isCommitted()) {
				writeContextToResponse(response, configuration);
			}
        }
    }

	private void writeContextToResponse(HttpServletResponse response, TraceeFilterConfiguration configuration) {
		if (configuration.shouldProcessContext(OutgoingResponse) && !backend.isEmpty()) {
			final Map<String, String> filteredContext = backend.getConfiguration().filterDeniedParams(backend, OutgoingResponse);
			response.setHeader(HTTP_HEADER_NAME, transportSerialization.render(filteredContext));
		}
	}

    @Override
    public final void init(FilterConfig filterConfig) throws ServletException { }

    @Override
    public final void destroy() { }
}
