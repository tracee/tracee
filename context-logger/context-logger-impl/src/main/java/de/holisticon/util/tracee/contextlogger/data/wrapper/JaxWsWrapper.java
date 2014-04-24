package de.holisticon.util.tracee.contextlogger.data.wrapper;


/**
 * Wrapper class for JaxWs soap request and response.
 * @author Tobias Gindler, holisticon AG
 */
public final class JaxWsWrapper {

    private final String soapRequest;
    private final String soapResponse;

    private JaxWsWrapper(final String soapRequest, final String soapResponse) {
        this.soapRequest = soapRequest;
        this.soapResponse = soapResponse;

    }

    public static JaxWsWrapper wrap(final String soapRequest, final String soapResponse) {
        return new JaxWsWrapper(soapRequest, soapResponse);
    }

    public String getSoapRequest() {
        return soapRequest;
    }

    public String getSoapResponse() {
        return soapResponse;
    }

}
