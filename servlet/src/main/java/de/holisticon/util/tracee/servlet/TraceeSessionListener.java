package de.holisticon.util.tracee.servlet;

import de.holisticon.util.tracee.Tracee;
import de.holisticon.util.tracee.TraceeConstants;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Warning: This filter assumes that the HttpSessionListener is executed on the same thread as the request handling.
 * This might not work for every servlet container.
 * It should at least work for the following containers:
 * <ul>
 *     <li>Jetty</li>
 *     <li>Tomcat</li>
 * </ul>
 * @author Daniel
 */
public class TraceeSessionListener implements HttpSessionListener {


    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        Tracee.getBackend().put(TraceeConstants.SESSION_ID_KEY, httpSessionEvent.getSession().getId());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        Tracee.getBackend().remove(TraceeConstants.SESSION_ID_KEY);
    }
}
