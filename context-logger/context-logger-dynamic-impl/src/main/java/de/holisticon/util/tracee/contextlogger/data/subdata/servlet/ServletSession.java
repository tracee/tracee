package de.holisticon.util.tracee.contextlogger.data.subdata.servlet;

import de.holisticon.util.tracee.contextlogger.api.Flatten;
import de.holisticon.util.tracee.contextlogger.api.TraceeContextLogProvider;
import de.holisticon.util.tracee.contextlogger.api.TraceeContextLogProviderMethod;
import de.holisticon.util.tracee.contextlogger.data.Order;
import de.holisticon.util.tracee.contextlogger.data.subdata.NameValuePair;
import de.holisticon.util.tracee.contextlogger.profile.ProfilePropertyNames;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Context provider for HttpSession.
 * Created by Tobias Gindler, holisticon AG on 19.03.14.
 */
@TraceeContextLogProvider(displayName = "servletSession")
public class ServletSession {

    final HttpSession session;

    public ServletSession (HttpSession session) {
        this.session = session;
    }


    @SuppressWarnings("unused")
    @Flatten
    @TraceeContextLogProviderMethod(displayName = "DYNAMIC", propertyName = "DYNAMIC")
    public List<NameValuePair> getSessionAttributes() {

        if (session == null) {
            return null;
        }

        final List<NameValuePair> sessionAttributes = new ArrayList<NameValuePair>();

        if (session != null) {
            final Enumeration<String> attributeNames = session.getAttributeNames();
            while (attributeNames.hasMoreElements()) {

                final String key = attributeNames.nextElement();
                final Object value = session.getAttribute(key);

                sessionAttributes.add(new NameValuePair(key, value != null ? value.toString() : null));

            }
        }

        return (sessionAttributes.size() > 0 ? sessionAttributes : null);

    }

}
