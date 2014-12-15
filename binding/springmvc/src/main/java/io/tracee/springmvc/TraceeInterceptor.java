package io.tracee.springmvc;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import io.tracee.Utilities;
import io.tracee.configuration.TraceeFilterConfiguration;
import io.tracee.transport.HttpJsonHeaderTransport;
import io.tracee.transport.TransportSerialization;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.Map;

import static io.tracee.configuration.TraceeFilterConfiguration.Channel.IncomingRequest;
import static io.tracee.configuration.TraceeFilterConfiguration.Channel.OutgoingResponse;

public final class TraceeInterceptor implements HandlerInterceptor {

	private final TraceeBackend backend;
	private final TransportSerialization<String> httpJsonHeaderSerialization;
	private String outgoingHeaderName = TraceeConstants.HTTP_HEADER_NAME;
	private String incomingHeaderName = TraceeConstants.HTTP_HEADER_NAME;
	private String profileName;

	public TraceeInterceptor() {
		this(Tracee.getBackend());
	}

	protected TraceeInterceptor(TraceeBackend backend) {
		this.backend = backend;
		httpJsonHeaderSerialization = new HttpJsonHeaderTransport(backend.getLoggerFactory());
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {

		final TraceeFilterConfiguration configuration = backend.getConfiguration(profileName);

		if (configuration.shouldProcessContext(IncomingRequest)) {
			mergeIncomingContextToBackend(request, configuration);
		}

		Utilities.generateRequestIdIfNecessary(backend);

		final HttpSession session = request.getSession(false);
		if (session != null) {
			Utilities.generateSessionIdIfNecessary(backend, session.getId());
		}

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {
		final TraceeFilterConfiguration configuration = backend.getConfiguration(profileName);

		if (configuration.shouldProcessContext(OutgoingResponse) && !backend.isEmpty()) {
			final Map<String, String> filteredContext = configuration.filterDeniedParams(backend, OutgoingResponse);
			response.setHeader(outgoingHeaderName, httpJsonHeaderSerialization.render(filteredContext));
		}
		backend.clear();
	}

	@Override
	public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

	}

	private void mergeIncomingContextToBackend(HttpServletRequest request, TraceeFilterConfiguration configuration) {
		final Enumeration headers = request.getHeaders(incomingHeaderName);
		if (headers == null) {
			throw new IllegalStateException("Could not read headers with name '"
					+ incomingHeaderName + "'. The access seem to be forbidden by the container");
		}
		while (headers.hasMoreElements()) {
			backend.putAll(httpJsonHeaderSerialization.parse((String) headers.nextElement()));

		}
	}

	public void setOutgoingHeaderName(String outgoingHeaderName) {
		this.outgoingHeaderName = outgoingHeaderName;
	}

	public void setIncomingHeaderName(String incomingHeaderName) {
		this.incomingHeaderName = incomingHeaderName;
	}

	public String getProfileName() {
		return profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}
}
