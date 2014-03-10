package de.holisticon.util.tracee.servlet;

import de.holisticon.util.tracee.Tracee;
import de.holisticon.util.tracee.TraceeBackend;
import de.holisticon.util.tracee.TraceeConstants;
import de.holisticon.util.tracee.Utilities;
import de.holisticon.util.tracee.configuration.TraceeFilterConfiguration;
import de.holisticon.util.tracee.transport.HttpJsonHeaderTransport;
import de.holisticon.util.tracee.transport.TransportSerialization;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import static de.holisticon.util.tracee.configuration.TraceeFilterConfiguration.Channel.IncomingRequest;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
public class TraceeServletRequestListener implements ServletRequestListener {

	private static final String HTTP_HEADER_NAME = TraceeConstants.HTTP_HEADER_NAME;

	private final TraceeBackend backend;

	private final TransportSerialization<String> transportSerialization;

	protected TraceeServletRequestListener(TraceeBackend backend, TransportSerialization<String> transportSerialization) {
		this.backend = backend;
		this.transportSerialization = transportSerialization;
	}

	public TraceeServletRequestListener() {
		this(Tracee.getBackend(), new HttpJsonHeaderTransport(Tracee.getBackend().getLoggerFactory()));
	}

	@Override
	public void requestDestroyed(ServletRequestEvent sre) {
		final ServletRequest servletRequest = sre.getServletRequest();
		if (servletRequest instanceof HttpServletRequest) {
			httpRequestDestroyed();
		}
	}

	@Override
	public void requestInitialized(ServletRequestEvent sre) {
		final ServletRequest servletRequest = sre.getServletRequest();
		if (servletRequest instanceof HttpServletRequest) {
			httpRequestInitialized((HttpServletRequest) servletRequest);
		}
	}

	void httpRequestInitialized(HttpServletRequest request) {
		final TraceeFilterConfiguration configuration = backend.getConfiguration();

		if (configuration.shouldProcessContext(IncomingRequest)) {
			mergeIncomingContextToBackend(request, backend);
		}

		if (configuration.shouldGenerateRequestId() && !backend.containsKey(TraceeConstants.REQUEST_ID_KEY)) {
			backend.put(TraceeConstants.REQUEST_ID_KEY, Utilities.createRandomAlphanumeric(configuration.generatedRequestIdLength()));
		}

		if (configuration.shouldGenerateSessionId() && !backend.containsKey(TraceeConstants.SESSION_ID_KEY)) {
			final HttpSession session = request.getSession(false);
			if (session != null) {
				backend.put(TraceeConstants.SESSION_ID_KEY, anonymizedSessionKey(session.getId(), configuration.generatedSessionIdLength()));
			}
		}
	}

	void httpRequestDestroyed() {
		backend.clear();
	}


	private String anonymizedSessionKey(String sessionKey, int length) {
		return Utilities.createAlphanumericHash(sessionKey, length);
	}

	private void mergeIncomingContextToBackend(HttpServletRequest request, TraceeBackend backend) {
		final Enumeration headers = request.getHeaders(HTTP_HEADER_NAME);
		if (headers == null) {
			throw new IllegalStateException("Could not read headers with name '"
					+ HTTP_HEADER_NAME + "'. The access seem to be forbidden by the container.");
		}

		final Map<String,String> parsed = new HashMap<String, String>();
		while (headers.hasMoreElements()) {
			parsed.putAll(transportSerialization.parse((String) headers.nextElement()));
		}

		final Map<String, String> filtered = backend.getConfiguration().filterDeniedParams(parsed, IncomingRequest);
		backend.putAll(filtered);
	}


}
