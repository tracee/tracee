package de.holisticon.util.tracee.contextlogger.data.subdata.jaxws;

import de.holisticon.util.tracee.contextlogger.api.TraceeContextLogProvider;
import de.holisticon.util.tracee.contextlogger.api.TraceeContextLogProviderMethod;
import de.holisticon.util.tracee.contextlogger.api.WrappedContextData;
import de.holisticon.util.tracee.contextlogger.data.Order;
import de.holisticon.util.tracee.contextlogger.data.wrapper.JaxWsWrapper;
import de.holisticon.util.tracee.contextlogger.profile.ProfilePropertyNames;

import javax.servlet.http.Cookie;

/**
 * JaxWsContextProvider context provider
 * Created by Tobias Gindler, holisticon AG on 28.03.14.
 */
@TraceeContextLogProvider(displayName = "jaxWs", order = Order.JAXWS)
public class JaxWsContextProvider implements WrappedContextData<JaxWsWrapper> {

    private JaxWsWrapper jaxWsWrapper;


    @Override
    public void setContextData(Object instance) throws ClassCastException {
        this.jaxWsWrapper = (JaxWsWrapper)instance;
    }

    @Override
    public Class<JaxWsWrapper> getWrappedType() {
        return JaxWsWrapper.class;
    }


    @SuppressWarnings("unused")
    @TraceeContextLogProviderMethod(
            displayName = "soapRequest",
            propertyName = ProfilePropertyNames.JAXWS_SOAP_REQUEST,
            order = 40)
    public String getSoapRequest() {
        if (jaxWsWrapper != null) {
            return jaxWsWrapper.getSoapRequest();
        }
        return null;
    }

    @SuppressWarnings("unused")
    @TraceeContextLogProviderMethod(
            displayName = "soapResponse",
            propertyName = ProfilePropertyNames.JAXWS_SOAP_RESPONSE,
            order = 50)
    public String getSoapResponse() {
        if (jaxWsWrapper != null) {
            return jaxWsWrapper.getSoapResponse();
        }
        return null;
    }
}
