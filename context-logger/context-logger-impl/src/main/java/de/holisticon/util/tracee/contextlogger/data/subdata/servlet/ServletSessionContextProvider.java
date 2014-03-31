package de.holisticon.util.tracee.contextlogger.data.subdata.servlet;

import de.holisticon.util.tracee.contextlogger.api.Flatten;
import de.holisticon.util.tracee.contextlogger.api.TraceeContextLogProvider;
import de.holisticon.util.tracee.contextlogger.api.TraceeContextLogProviderMethod;
import de.holisticon.util.tracee.contextlogger.api.WrappedContextData;
import de.holisticon.util.tracee.contextlogger.data.Order;
import de.holisticon.util.tracee.contextlogger.data.subdata.NameStringValuePair;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Context provider for HttpSession.
 * Created by Tobias Gindler, holisticon AG on 19.03.14.
 */
@TraceeContextLogProvider(displayName = "servletSession", order = Order.SERVLET)
public class ServletSessionContextProvider implements WrappedContextData<HttpSession> {

    HttpSession session;

    public ServletSessionContextProvider() {
    }

    public ServletSessionContextProvider(HttpSession session) {
        this.session = session;
    }

    @Override
    public void setContextData(Object instance) throws ClassCastException {
        this.session = (HttpSession) instance;
    }

    @Override
    public Class<HttpSession> getWrappedType() {
        return HttpSession.class;
    }


    @SuppressWarnings("unused")
    @Flatten
    @TraceeContextLogProviderMethod(displayName = "DYNAMIC", propertyName = "DYNAMIC")
    public List<NameStringValuePair> getSessionAttributes() {

        if (session == null) {
            return null;
        }

        final List<NameStringValuePair> sessionAttributes = new ArrayList<NameStringValuePair>();

        if (session != null) {
            final Enumeration<String> attributeNames = session.getAttributeNames();
            while (attributeNames.hasMoreElements()) {

                final String key = attributeNames.nextElement();
                final Object value = session.getAttribute(key);

                sessionAttributes.add(new NameStringValuePair(key, value != null ? value.toString() : null));

            }
        }

        return (sessionAttributes.size() > 0 ? sessionAttributes : null);

    }


}
