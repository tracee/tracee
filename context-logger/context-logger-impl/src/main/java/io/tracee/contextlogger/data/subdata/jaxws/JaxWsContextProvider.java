package io.tracee.contextlogger.data.subdata.jaxws;

import io.tracee.contextlogger.api.TraceeContextLogProvider;
import io.tracee.contextlogger.api.TraceeContextLogProviderMethod;
import io.tracee.contextlogger.api.WrappedContextData;
import io.tracee.contextlogger.data.Order;
import io.tracee.contextlogger.data.wrapper.JaxWsWrapper;
import io.tracee.contextlogger.profile.ProfilePropertyNames;

/**
 * JaxWsContextProvider context provider.
 * @author Tobias Gindler, holisticon AG on 28.03.14.
 */
@TraceeContextLogProvider(displayName = "jaxWs", order = Order.JAXWS)
public class JaxWsContextProvider implements WrappedContextData<JaxWsWrapper> {

    private JaxWsWrapper jaxWsWrapper;


    @Override
    public final void setContextData(Object instance) throws ClassCastException {
        this.jaxWsWrapper = (JaxWsWrapper) instance;
    }

    @Override
    public final Class<JaxWsWrapper> getWrappedType() {
        return JaxWsWrapper.class;
    }


    @SuppressWarnings("unused")
    @TraceeContextLogProviderMethod(
            displayName = "soapRequest",
            propertyName = ProfilePropertyNames.JAXWS_SOAP_REQUEST,
            order = 40)
    public final String getSoapRequest() {
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
    public final String getSoapResponse() {
        if (jaxWsWrapper != null) {
            return jaxWsWrapper.getSoapResponse();
        }
        return null;
    }
}
