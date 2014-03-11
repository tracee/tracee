package de.holisticon.util.tracee.contextlogger.json.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Category for json output for jaxws context specific data.
 * Created by Tobias Gindler, holisticon AG on 24.01.14.
 */
public final class JaxWsCategory {

    public static final String ATTR_REQUEST_SOAP_MESSAGE = "request-soap-message";
    public static final String ATTR_RESPONSE_SOAP_MESSAGE = "response-soap-message";

    @SerializedName(JaxWsCategory.ATTR_REQUEST_SOAP_MESSAGE)
    private final String requestSoapMessage;
    @SerializedName(JaxWsCategory.ATTR_RESPONSE_SOAP_MESSAGE)
    private final String responseSoapMessage;

    @SuppressWarnings("unused")
    private JaxWsCategory() {
        this(null, null);
    }

    public JaxWsCategory(String requestSoapMessage, String responseSoapMessage) {
        this.requestSoapMessage = requestSoapMessage;
        this.responseSoapMessage = responseSoapMessage;
    }

    @SuppressWarnings("unused")
    public String getRequestSoapMessage() {
        return requestSoapMessage;
    }

    @SuppressWarnings("unused")
    public String getResponseSoapMessage() {
        return responseSoapMessage;
    }
}
