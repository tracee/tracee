package de.holisticon.util.tracee.errorlogger.json.beans.servlet;

import de.holisticon.util.tracee.errorlogger.json.beans.values.ServletHttpHeader;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

import java.util.List;

/**
 * HttpServletResponse context specific class for  JSON generation.
 * Created by Tobias Gindler, holisticon AG on 24.01.14.
 */

@JsonAutoDetect(
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE,
        fieldVisibility = JsonAutoDetect.Visibility.ANY
)
@JsonPropertyOrder(
        value = {
                ServletResponseSubCategory.ATTR_HTTP_STATUS_CODE,
                ServletResponseSubCategory.ATTR_HTTP_RESPONSE_HEADERS
        }
)
public class ServletResponseSubCategory {

    public static final String ATTR_HTTP_STATUS_CODE = "http-status-code";
    public static final String ATTR_HTTP_RESPONSE_HEADERS = "http-response-headers";


    @JsonProperty(value = ServletResponseSubCategory.ATTR_HTTP_STATUS_CODE)
    private final Integer httpStatusCode;
    @JsonProperty(value = ServletResponseSubCategory.ATTR_HTTP_RESPONSE_HEADERS)
    private final List<ServletHttpHeader> httpResponseHeaders;


    @SuppressWarnings("unused")
    private ServletResponseSubCategory() {
        this(null, null);
    }


    public ServletResponseSubCategory (Integer httpStatusCode, List<ServletHttpHeader> httpResponseHeaders) {

        this.httpStatusCode = httpStatusCode;
        this.httpResponseHeaders = httpResponseHeaders;

    }


    @SuppressWarnings("unused")
    public List<ServletHttpHeader> getHttpResponseHeaders() {
        return httpResponseHeaders;
    }

    @SuppressWarnings("unused")
    public Integer getHttpStatusCode() {
        return httpStatusCode;
    }
}
