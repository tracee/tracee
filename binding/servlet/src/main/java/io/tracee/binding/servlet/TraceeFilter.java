package io.tracee.binding.servlet;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import io.tracee.configuration.TraceeFilterConfiguration;
import io.tracee.transport.HttpHeaderTransport;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import static io.tracee.configuration.TraceeFilterConfiguration.Channel.OutgoingResponse;

public class TraceeFilter implements Filter {

	public static final String PROFILE_INIT_PARAM = "profile";

    private static final String HTTP_HEADER_NAME = TraceeConstants.TPIC_HEADER;

	private String profile = TraceeFilterConfiguration.Profile.DEFAULT;

	private final TraceeBackend backend;

    private final HttpHeaderTransport transportSerialization;

	public TraceeFilter() {
		this(Tracee.getBackend(), new HttpHeaderTransport());
	}

	// VisibleForTesting
	TraceeFilter(TraceeBackend backend, HttpHeaderTransport transportSerialization) {
		this.backend = backend;
		this.transportSerialization = transportSerialization;
	}

    @Override
    public final void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain)
            throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest && servletResponse instanceof HttpServletResponse) {
            doFilterHttp((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, filterChain);
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    final void doFilterHttp(final HttpServletRequest request, final HttpServletResponse response,
							final FilterChain filterChain) throws IOException, ServletException {

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

	private void writeContextToResponse(final HttpServletResponse response, final TraceeFilterConfiguration configuration) {
		if (!backend.isEmpty() && configuration.shouldProcessContext(OutgoingResponse)) {
			final Map<String, String> filteredContext = backend.getConfiguration(profile).filterDeniedParams(backend.copyToMap(), OutgoingResponse);
			response.setHeader(HTTP_HEADER_NAME, transportSerialization.render(filteredContext));
		}
	}

    @Override
    public final void init(final FilterConfig filterConfig) throws ServletException {
		final String profileInitParameter = filterConfig.getInitParameter(PROFILE_INIT_PARAM);
		if (profileInitParameter != null) {
			profile = profileInitParameter;
		}
	}

    @Override
    public final void destroy() { }
}
