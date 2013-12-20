package de.holisticon.util.tracee.errorlogger.json.beans;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

/**
 * Created by Tobias Gindler on 19.12.13.
 */
@JsonAutoDetect(
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE,
        fieldVisibility = JsonAutoDetect.Visibility.ANY
)
@JsonPropertyOrder(
        value = {
                JaxWsCategory.ATTR_REQUEST_SOAP_MESSAGE,
                JaxWsCategory.ATTR_RESPONSE_SOAP_MESSAGE
        }
)
public final class JaxWsCategory {

    public static final String ATTR_REQUEST_SOAP_MESSAGE = "request-soap-message";
    public static final String ATTR_RESPONSE_SOAP_MESSAGE = "response-soap-message";

    @JsonProperty(value = JaxWsCategory.ATTR_REQUEST_SOAP_MESSAGE)
    private final String requestSoapMessage;
    @JsonProperty(value = JaxWsCategory.ATTR_RESPONSE_SOAP_MESSAGE)
    private final String responseSoapMessage;

    @SuppressWarnings("unused")
    private JaxWsCategory() {
        this(null, null);
    }

    public JaxWsCategory(String requestSoapMessage, String responseSoapMessage) {
        this.requestSoapMessage = requestSoapMessage;
        this.responseSoapMessage = responseSoapMessage;
    }

    public String getRequestSoapMessage() {
        return requestSoapMessage;
    }

    public String getResponseSoapMessage() {
        return responseSoapMessage;
    }
}
