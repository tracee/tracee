package de.holisticon.util.tracee.servlet;

import de.holisticon.util.tracee.Tracee;
import de.holisticon.util.tracee.TraceeBackend;
import de.holisticon.util.tracee.TraceeConstants;

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
 *
 * @author Daniel Wegener (Holisticon AG)
 */
public class TraceeSessionListener implements HttpSessionListener {

	private final TraceeBackend backend;

	@SuppressWarnings("unused")
	public TraceeSessionListener() {
		this(Tracee.getBackend());
	}

	protected TraceeSessionListener(TraceeBackend backend) {
		this.backend = backend;
	}

    @Override
    public final void sessionCreated(HttpSessionEvent httpSessionEvent) {
		backend.put(TraceeConstants.SESSION_ID_KEY, httpSessionEvent.getSession().getId());
    }

    @Override
    public final void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
		backend.remove(TraceeConstants.SESSION_ID_KEY);
    }
}
