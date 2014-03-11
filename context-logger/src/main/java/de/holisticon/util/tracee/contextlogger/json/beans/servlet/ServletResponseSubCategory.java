package de.holisticon.util.tracee.contextlogger.json.beans.servlet;

import com.google.gson.annotations.SerializedName;
import de.holisticon.util.tracee.contextlogger.json.beans.values.ServletHttpHeader;

import java.util.List;

/**
 * HttpServletResponse context specific class for  JSON generation.
 * Created by Tobias Gindler, holisticon AG on 24.01.14.
 */

public class ServletResponseSubCategory {

    public static final String ATTR_HTTP_STATUS_CODE = "http-status-code";
    public static final String ATTR_HTTP_RESPONSE_HEADERS = "http-response-headers";

    @SerializedName(ServletResponseSubCategory.ATTR_HTTP_STATUS_CODE)
    private final Integer httpStatusCode;
    @SerializedName(ServletResponseSubCategory.ATTR_HTTP_RESPONSE_HEADERS)
    private final List<ServletHttpHeader> httpResponseHeaders;


    @SuppressWarnings("unused")
    private ServletResponseSubCategory() {
        this(null, null);
    }


    public ServletResponseSubCategory(Integer httpStatusCode, List<ServletHttpHeader> httpResponseHeaders) {

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
