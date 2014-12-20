package io.tracee.springmvc;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import io.tracee.Utilities;
import io.tracee.configuration.TraceeFilterConfiguration;
import io.tracee.transport.HttpHeaderTransport;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import static io.tracee.configuration.TraceeFilterConfiguration.Channel.IncomingRequest;
import static io.tracee.configuration.TraceeFilterConfiguration.Channel.IncomingResponse;
import static io.tracee.configuration.TraceeFilterConfiguration.Channel.OutgoingResponse;

public final class TraceeInterceptor implements HandlerInterceptor {

	private final TraceeBackend backend;
	private final HttpHeaderTransport httpHeaderSerialization;
	private String outgoingHeaderName = TraceeConstants.HTTP_HEADER_NAME;
	private String incomingHeaderName = TraceeConstants.HTTP_HEADER_NAME;
	private String profileName;

	public TraceeInterceptor() {
		this(Tracee.getBackend());
	}

	protected TraceeInterceptor(TraceeBackend backend) {
		this.backend = backend;
		httpHeaderSerialization = new HttpHeaderTransport(backend.getLoggerFactory());
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {

		final TraceeFilterConfiguration configuration = backend.getConfiguration(profileName);

		if (configuration.shouldProcessContext(IncomingRequest)) {
			final Enumeration headers = request.getHeaders(incomingHeaderName);
			if (headers != null && headers.hasMoreElements() && configuration.shouldProcessContext(IncomingRequest)) {
				final List<String> stringTraceeHeaders = new ArrayList<String>();
				while (headers.hasMoreElements()) {
					stringTraceeHeaders.add((String) headers.nextElement());
				}
				final Map<String, String> parsedContext = httpHeaderSerialization.parse(stringTraceeHeaders);
				backend.putAll(configuration.filterDeniedParams(parsedContext, IncomingResponse));
			}
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
			final Map<String, String> filteredContext = configuration.filterDeniedParams(backend.copyToMap(), OutgoingResponse);
			response.setHeader(outgoingHeaderName, httpHeaderSerialization.render(filteredContext));
		}
		backend.clear();
	}

	@Override
	public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

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
