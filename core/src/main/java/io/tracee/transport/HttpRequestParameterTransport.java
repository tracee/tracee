package io.tracee.transport;

import io.tracee.Tracee;
import io.tracee.TraceeConstants;
import io.tracee.TraceeLogger;
import io.tracee.TraceeLoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class HttpRequestParameterTransport {

	private final TraceeLogger logger;

	public HttpRequestParameterTransport() {
		this(Tracee.getBackend().getLoggerFactory());
	}

	public HttpRequestParameterTransport(TraceeLoggerFactory loggerFactory) {
		this.logger = loggerFactory.getLogger(HttpRequestParameterTransport.class);
	}

	public Map<String, String> parse(Map<String, String[]> serializedElements) {
		final Map<String, String> contextMap = new HashMap<String, String>();

		final String[] requestIdValue = serializedElements.get(TraceeConstants.REQUEST_ID_KEY);
		if (requestIdValue != null && requestIdValue.length > 0) {
			contextMap.put(TraceeConstants.REQUEST_ID_KEY, requestIdValue[0]);
		}

		final String[] sessionIdValue = serializedElements.get(TraceeConstants.SESSION_ID_KEY);
		if (sessionIdValue != null && sessionIdValue.length > 0) {
			contextMap.put(TraceeConstants.SESSION_ID_KEY, sessionIdValue[0]);
		}

		return contextMap;
	}

}
