package de.holisticon.util.tracee.springmvc;

import de.holisticon.util.tracee.Tracee;
import de.holisticon.util.tracee.TraceeBackend;
import de.holisticon.util.tracee.TraceeConstants;
import de.holisticon.util.tracee.Utilities;
import de.holisticon.util.tracee.configuration.TraceeFilterConfiguration;
import de.holisticon.util.tracee.transport.HttpJsonHeaderTransport;
import de.holisticon.util.tracee.transport.TransportSerialization;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.Map;

import static de.holisticon.util.tracee.configuration.TraceeFilterConfiguration.Channel.IncomingRequest;
import static de.holisticon.util.tracee.configuration.TraceeFilterConfiguration.Channel.OutgoingResponse;

/**
 * @author Sven Bunge, Holisticon AG
 */
public class TraceeInterceptor implements HandlerInterceptor {

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

		if (configuration.shouldProcessContext(IncomingRequest))
			mergeIncomingContextToBackend(request, configuration);
		
		// create random RequestId if not already set
		if (!backend.containsKey(TraceeConstants.REQUEST_ID_KEY) && configuration.shouldGenerateRequestId()) {
			backend.put(TraceeConstants.REQUEST_ID_KEY, Utilities.createRandomAlphanumeric(configuration.generatedRequestIdLength()));
		}

		// create another random id to identify the http session 
		if (!backend.containsKey(TraceeConstants.SESSION_ID_KEY) && configuration.shouldGenerateSessionId()) {
			final HttpSession session = request.getSession(false);
			if (session != null) {
				backend.put(TraceeConstants.SESSION_ID_KEY, Utilities.createAlphanumericHash(session.getId(), configuration.generatedSessionIdLength()));
			}
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
