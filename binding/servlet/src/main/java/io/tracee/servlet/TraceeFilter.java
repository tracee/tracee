package io.tracee.servlet;

import io.tracee.*;
import io.tracee.configuration.TraceeFilterConfiguration;
import io.tracee.transport.HttpJsonHeaderTransport;
import io.tracee.transport.TransportSerialization;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import static io.tracee.configuration.TraceeFilterConfiguration.Channel.OutgoingResponse;

public class TraceeFilter implements Filter {

	public static final String PROFILE_INIT_PARAM = "profile";

    private static final String HTTP_HEADER_NAME = TraceeConstants.HTTP_HEADER_NAME;

	private String profile = TraceeFilterConfiguration.DEFAULT_PROFILE;

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

    final void doFilterHttp(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

		final TraceeFilterConfiguration configuration = backend.getConfiguration(profile);

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
			final Map<String, String> filteredContext = backend.getConfiguration(profile).filterDeniedParams(backend, OutgoingResponse);
			response.setHeader(HTTP_HEADER_NAME, transportSerialization.render(filteredContext));
		}
	}

    @Override
    public final void init(FilterConfig filterConfig) throws ServletException {
		final String profileInitParameter = filterConfig.getInitParameter(PROFILE_INIT_PARAM);
		if (profileInitParameter != null) {
			profile = profileInitParameter;
		}

	}

    @Override
    public final void destroy() { }
}
