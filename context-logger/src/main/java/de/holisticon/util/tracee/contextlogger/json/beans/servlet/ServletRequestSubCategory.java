package de.holisticon.util.tracee.contextlogger.json.beans.servlet;

import com.google.gson.annotations.SerializedName;
import de.holisticon.util.tracee.contextlogger.json.beans.values.ServletHttpHeader;
import de.holisticon.util.tracee.contextlogger.json.beans.values.ServletHttpParameter;

import java.util.List;

/**
 * HttpServletRequest context specific class for  JSON generation.
 * Created by Tobias Gindler, holisticon AG on 24.01.14.
 */

public final class ServletRequestSubCategory {

    public static final String ATTR_URL = "url";
    public static final String ATTR_HTTP_METHOD = "http-method";
    public static final String ATTR_HTTP_PARAMETERS = "http-parameters";
    public static final String ATTR_HTTP_REQUEST_HEADERS = "http-request-headers";
    public static final String ATTR_REMOTE_INFO = "remote-info";
    public static final String ATTR_ENHANCED_REQUEST_INFO = "enhanced-request-info";
    public static final String ATTR_COOKIES = "cookies";


    @SerializedName(ServletRequestSubCategory.ATTR_URL)
    private final String url;
    @SerializedName(ServletRequestSubCategory.ATTR_HTTP_METHOD)
    private final String httpMethod;
    @SerializedName(ServletRequestSubCategory.ATTR_HTTP_PARAMETERS)
    private final List<ServletHttpParameter> httpParameters;
    @SerializedName(ServletRequestSubCategory.ATTR_HTTP_REQUEST_HEADERS)
    private final List<ServletHttpHeader> httpRequestHeaders;
    @SerializedName(ServletRequestSubCategory.ATTR_REMOTE_INFO)
    private final ServletRemoteInfoSubCategory remoteInfo;
    @SerializedName(ServletRequestSubCategory.ATTR_ENHANCED_REQUEST_INFO)
    private final ServletRequestEnhancedInfoSubCategory enhancedRequestInfo;
    @SerializedName(ServletRequestSubCategory.ATTR_COOKIES)
    private final List<ServletCookie> cookies;


    @SuppressWarnings("unused")
    private ServletRequestSubCategory() {
        this(null, null, null, null, null, null, null);
    }

    public ServletRequestSubCategory(String url,
                                     String httpMethod,
                                     List<ServletHttpParameter> httpParameters,
                                     List<ServletHttpHeader> httpRequestHeaders,
                                     ServletRemoteInfoSubCategory remoteInfo,
                                     ServletRequestEnhancedInfoSubCategory enhancedRequestInfo,
                                     List<ServletCookie> cookies
                                    ) {
        this.url = url;
        this.httpMethod = httpMethod;
        this.httpParameters = httpParameters;
        this.httpRequestHeaders = httpRequestHeaders;
        this.remoteInfo = remoteInfo;
        this.enhancedRequestInfo = enhancedRequestInfo;
        this.cookies = cookies;
    }

    @SuppressWarnings("unused")
    public String getUrl() {
        return url;
    }

    @SuppressWarnings("unused")
    public String getHttpMethod() {
        return httpMethod;
    }

    @SuppressWarnings("unused")
    public List<ServletHttpHeader> getHttpRequestHeaders() {
        return httpRequestHeaders;
    }

    @SuppressWarnings("unused")
    public List<ServletHttpParameter> getHttpParameters() {
        return httpParameters;
    }

    @SuppressWarnings("unused")
    public ServletRemoteInfoSubCategory getRemoteInfo() {
        return remoteInfo;
    }

    @SuppressWarnings("unused")
    public ServletRequestEnhancedInfoSubCategory getEnhancedRequestInfo() {
        return enhancedRequestInfo;
    }

    @SuppressWarnings("unused")
    public List<ServletCookie> getCookies() {
        return cookies;
    }
}
