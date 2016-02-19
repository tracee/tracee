package io.tracee.binding.servlet;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import io.tracee.Utilities;
import io.tracee.configuration.TraceeFilterConfiguration;
import io.tracee.transport.HttpHeaderTransport;

import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

import static io.tracee.configuration.TraceeFilterConfiguration.Channel.IncomingRequest;

/**
 * Manages the TracEE lifecycle.
 */
@WebListener("TraceeServletRequestListener to read incoming TPICs into Tracee backend")
public final class TraceeServletRequestListener implements ServletRequestListener {

	private static final String HTTP_HEADER_NAME = TraceeConstants.TPIC_HEADER;

	private final TraceeBackend backend;

	private final HttpHeaderTransport transportSerialization;

	protected TraceeServletRequestListener(TraceeBackend backend, HttpHeaderTransport transportSerialization) {
		this.backend = backend;
		this.transportSerialization = transportSerialization;
	}

	public TraceeServletRequestListener() {
		this(Tracee.getBackend(), new HttpHeaderTransport());
	}

	@Override
	public void requestDestroyed(final ServletRequestEvent sre) {
		backend.clear();
	}

	@Override
	public void requestInitialized(final ServletRequestEvent sre) {
		final ServletRequest servletRequest = sre.getServletRequest();
		if (servletRequest instanceof HttpServletRequest) {
			httpRequestInitialized((HttpServletRequest) servletRequest);
		}
	}

	private void httpRequestInitialized(final HttpServletRequest request) {
		final TraceeFilterConfiguration configuration = backend.getConfiguration();

		if (configuration.shouldProcessContext(IncomingRequest)) {
			final Enumeration<String> headers = request.getHeaders(HTTP_HEADER_NAME);

			if (headers != null && headers.hasMoreElements()) {
				final Map<String, String> contextMap = transportSerialization.parse(Collections.list(headers));
				backend.putAll(backend.getConfiguration().filterDeniedParams(contextMap, IncomingRequest));
			}
		}

		Utilities.generateInvocationIdIfNecessary(backend);

		final HttpSession session = request.getSession(false);
		if (session != null) {
			Utilities.generateSessionIdIfNecessary(backend, session.getId());
		}
	}
}
