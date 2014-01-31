package de.holisticon.util.tracee.errorlogger.json.beans;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

/**
 * Category for json output for jaxws context specific data.
 * Created by Tobias Gindler, holisticon AG on 24.01.14.
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

    @SuppressWarnings("unused")
    public String getRequestSoapMessage() {
        return requestSoapMessage;
    }

    @SuppressWarnings("unused")
    public String getResponseSoapMessage() {
        return responseSoapMessage;
    }
}
