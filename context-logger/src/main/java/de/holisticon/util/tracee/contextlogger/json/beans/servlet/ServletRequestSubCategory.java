package de.holisticon.util.tracee.contextlogger.json.beans.servlet;

import de.holisticon.util.tracee.contextlogger.json.beans.values.ServletHttpHeader;
import de.holisticon.util.tracee.contextlogger.json.beans.values.ServletHttpParameter;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.List;

/**
 * HttpServletRequest context specific class for  JSON generation.
 * Created by Tobias Gindler, holisticon AG on 24.01.14.
 */

@JsonAutoDetect(
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE,
        fieldVisibility = JsonAutoDetect.Visibility.ANY
)
@JsonPropertyOrder(
        value = {
                ServletRequestSubCategory.ATTR_URL,
                ServletRequestSubCategory.ATTR_HTTP_METHOD,
                ServletRequestSubCategory.ATTR_HTTP_PARAMETERS,
                ServletRequestSubCategory.ATTR_HTTP_REQUEST_HEADERS,
                ServletRequestSubCategory.ATTR_COOKIES,
                ServletRequestSubCategory.ATTR_REMOTE_INFO,
                ServletRequestSubCategory.ATTR_ENHANCED_REQUEST_INFO
        }
)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public final class ServletRequestSubCategory {

    public static final String ATTR_URL = "url";
    public static final String ATTR_HTTP_METHOD = "http-method";
    public static final String ATTR_HTTP_PARAMETERS = "http-parameters";
    public static final String ATTR_HTTP_REQUEST_HEADERS = "http-request-headers";
    public static final String ATTR_REMOTE_INFO = "remote-info";
    public static final String ATTR_ENHANCED_REQUEST_INFO = "enhanced-request-info";
    public static final String ATTR_COOKIES = "cookies";


    @JsonProperty(value = ServletRequestSubCategory.ATTR_URL)
    private final String url;
    @JsonProperty(value = ServletRequestSubCategory.ATTR_HTTP_METHOD)
    private final String httpMethod;
    @JsonProperty(value = ServletRequestSubCategory.ATTR_HTTP_PARAMETERS)
    private final List<ServletHttpParameter> httpParameters;
    @JsonProperty(value = ServletRequestSubCategory.ATTR_HTTP_REQUEST_HEADERS)
    private final List<ServletHttpHeader> httpRequestHeaders;
    @JsonProperty(value = ServletRequestSubCategory.ATTR_REMOTE_INFO)
    private final ServletRemoteInfoSubCategory remoteInfo;
    @JsonProperty(value = ServletRequestSubCategory.ATTR_ENHANCED_REQUEST_INFO)
    private final ServletRequestEnhancedInfoSubCategory enhancedRequestInfo;
    @JsonProperty(value = ServletRequestSubCategory.ATTR_COOKIES)
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
