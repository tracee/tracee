package de.holisticon.util.tracee.contextlogger.data.wrapper;

import de.holisticon.util.tracee.contextlogger.api.TraceeContextLogProvider;
import de.holisticon.util.tracee.contextlogger.data.Order;

/**
 * Wrapper class for JaxWs soap request and response.
 * Created by Tobias Gindler, holisticon AG on 28.03.14.
 */
public class JaxWsWrapper {

    private final String soapRequest;
    private final String soapResponse;

    private JaxWsWrapper (final String soapRequest, final String soapResponse) {
        this.soapRequest = soapRequest;
        this.soapResponse = soapResponse;

    }

    public static JaxWsWrapper create(final String soapRequest, final String soapResponse) {
        return new JaxWsWrapper(soapRequest,soapResponse);
    }

    public String getSoapRequest() {
        return soapRequest;
    }

    public String getSoapResponse() {
        return soapResponse;
    }

}
