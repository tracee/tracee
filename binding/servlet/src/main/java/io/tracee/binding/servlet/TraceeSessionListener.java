package io.tracee.binding.servlet;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import io.tracee.Utilities;
import io.tracee.configuration.PropertiesBasedTraceeFilterConfiguration;
import io.tracee.configuration.TraceeFilterConfiguration;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Warning: This filter assumes that the HttpSessionListener is executed on the same thread as the request handling.
 * This might not work for every servlet container.
 * It should at least work for the following containers:
 * <ul>
 * <li>Jetty</li>
 * <li>Tomcat</li>
 * </ul>
 */
@WebListener("TraceeSessionListener to create sessionIds on session creation and remove it instead from the TracEE backend on session termination.")
public class TraceeSessionListener implements HttpSessionListener {

	private final TraceeBackend backend;
	private final TraceeFilterConfiguration filterConfiguration;

	/**
	 * @deprecated Use full constructor
	 */
	@Deprecated
	public TraceeSessionListener() {
		this(Tracee.getBackend(), PropertiesBasedTraceeFilterConfiguration.instance().DEFAULT);
	}

	protected TraceeSessionListener(TraceeBackend backend, TraceeFilterConfiguration filterConfiguration) {
		this.backend = backend;
		this.filterConfiguration = filterConfiguration;
	}

	@Override
	public final void sessionCreated(HttpSessionEvent httpSessionEvent) {
		Utilities.generateSessionIdIfNecessary(backend, filterConfiguration, httpSessionEvent.getSession().getId());
	}

	@Override
	public final void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
		backend.remove(TraceeConstants.SESSION_ID_KEY);
	}
}
