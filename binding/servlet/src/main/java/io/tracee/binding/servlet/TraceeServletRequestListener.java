package io.tracee.binding.servlet;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import io.tracee.Utilities;
import io.tracee.configuration.TraceeFilterConfiguration;
import io.tracee.transport.HttpHeaderTransport;
import io.tracee.transport.HttpRequestParameterTransport;

import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import static io.tracee.configuration.TraceeFilterConfiguration.Channel.IncomingRequest;
import static io.tracee.configuration.TraceeFilterConfiguration.Channel.IncomingResponse;

/**
 * Manages the TracEE lifecycle.
 */
public final class TraceeServletRequestListener implements ServletRequestListener {

	private static final String HTTP_HEADER_NAME = TraceeConstants.HTTP_HEADER_NAME;

	private final TraceeBackend backend;

	private final HttpHeaderTransport transportSerialization;
	private final HttpRequestParameterTransport httpRequestParameterTransport;

	protected TraceeServletRequestListener(TraceeBackend backend, HttpHeaderTransport transportSerialization, HttpRequestParameterTransport httpRequestParameterTransport) {
		this.backend = backend;
		this.transportSerialization = transportSerialization;
		this.httpRequestParameterTransport = httpRequestParameterTransport;
	}

	public TraceeServletRequestListener() {
		this(Tracee.getBackend(), new HttpHeaderTransport(Tracee.getBackend().getLoggerFactory()), new HttpRequestParameterTransport(Tracee.getBackend().getLoggerFactory()));
	}

	@Override
	public void requestDestroyed(ServletRequestEvent sre) {
		backend.clear();
	}

	@Override
	public void requestInitialized(ServletRequestEvent sre) {
		final ServletRequest servletRequest = sre.getServletRequest();
		if (servletRequest instanceof HttpServletRequest) {
			httpRequestInitialized((HttpServletRequest) servletRequest);
		}
	}

	private void httpRequestInitialized(HttpServletRequest request) {
		final TraceeFilterConfiguration configuration = backend.getConfiguration();

		if (configuration.shouldProcessContext(IncomingRequest)) {
			// process request parameter first
			final Map<String, String> parameterParsedContext = httpRequestParameterTransport.parse((Map<String, String[]>)request.getParameterMap());
			backend.putAll(configuration.filterDeniedParams(parameterParsedContext, IncomingResponse));

			// overwrite values defined by request parameter with values from header
			final Enumeration headers = request.getHeaders(HTTP_HEADER_NAME);

			if (headers != null && headers.hasMoreElements() && configuration.shouldProcessContext(IncomingRequest)) {
				final List<String> stringTraceeHeaders = new ArrayList<String>();
				while (headers.hasMoreElements()) {
					stringTraceeHeaders.add((String) headers.nextElement());
				}
				final Map<String, String> contextMap = transportSerialization.parse(stringTraceeHeaders);
				backend.putAll(backend.getConfiguration().filterDeniedParams(contextMap, IncomingRequest));
			}
		}

		Utilities.generateRequestIdIfNecessary(backend);
		Utilities.generateConversationIdIfNecessary(backend);

		final HttpSession session = request.getSession(false);
		if (session != null) {
			Utilities.generateSessionIdIfNecessary(backend, session.getId());
		}
	}

}
