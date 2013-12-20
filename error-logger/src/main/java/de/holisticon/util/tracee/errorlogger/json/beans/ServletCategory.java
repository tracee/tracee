package de.holisticon.util.tracee.errorlogger.json.beans;

/**
 * Created by Tobias Gindler, holisticon AG on 19.12.13.
 */

import de.holisticon.util.tracee.errorlogger.json.beans.values.ServletHttpHeader;
import de.holisticon.util.tracee.errorlogger.json.beans.values.ServletHttpParameter;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.List;

@JsonAutoDetect(
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE,
        fieldVisibility = JsonAutoDetect.Visibility.ANY
)
@JsonPropertyOrder(
        value = {
                ServletCategory.ATTR_URL,
                ServletCategory.ATTR_HTTP_METHOD,
                ServletCategory.ATTR_HTTP_PARAMETERS,
                ServletCategory.ATTR_HTTP_HEADERS,
                ServletCategory.ATTR_HTTP_REMOTE_ADDRESS,
                ServletCategory.ATTR_HTTP_REMOTE_HOST,
                ServletCategory.ATTR_HTTP_REMOTE_PORT,
                ServletCategory.ATTR_USER
        }
)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public final class ServletCategory {

    public static final String ATTR_URL = "url";
    public static final String ATTR_HTTP_METHOD = "http-method";
    public static final String ATTR_HTTP_PARAMETERS = "http-parameters";
    public static final String ATTR_HTTP_HEADERS = "http-headers";
    public static final String ATTR_USER = "user";
    public static final String ATTR_HTTP_REMOTE_ADDRESS = "http-remote-address";
    public static final String ATTR_HTTP_REMOTE_HOST = "http-remote-host";
    public static final String ATTR_HTTP_REMOTE_PORT = "http-remote-port";

    @JsonProperty(value = ServletCategory.ATTR_URL)
    private final String url;
    @JsonProperty(value = ServletCategory.ATTR_HTTP_METHOD)
    private final String httpMethod;
    @JsonProperty(value = ServletCategory.ATTR_USER)
    private final String user;
    @JsonProperty(value = ServletCategory.ATTR_HTTP_PARAMETERS)
    private final List<ServletHttpParameter> httpParameters;
    @JsonProperty(value = ServletCategory.ATTR_HTTP_HEADERS)
    private final List<ServletHttpHeader> httpHeaders;
    @JsonProperty(value = ServletCategory.ATTR_HTTP_REMOTE_ADDRESS)
    private final String httpRemoteAddress;
    @JsonProperty(value = ServletCategory.ATTR_HTTP_REMOTE_HOST)
    private final String httpRemoteHost;
    @JsonProperty(value = ServletCategory.ATTR_HTTP_REMOTE_PORT)
    private final Integer httpRemotePort;


    @SuppressWarnings("unused")
    private ServletCategory() {
        this(null, null, null, null, null, null, null, null);
    }

    public ServletCategory(String url,
                           String httpMethod,
                           List<ServletHttpParameter> httpParameters,
                           List<ServletHttpHeader> httpHeaders,
                           String httpRemoteAddress,
                           String httpRemoteHost,
                           Integer httpRemotePort,
                           String user) {
        this.url = url;
        this.httpMethod = httpMethod;
        this.httpParameters = httpParameters;
        this.httpHeaders = httpHeaders;
        this.httpRemoteAddress = httpRemoteAddress;
        this.httpRemoteHost = httpRemoteHost;
        this.httpRemotePort = httpRemotePort;
        this.user = user;
    }

    public String getUrl() {
        return url;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public List<ServletHttpHeader> getHttpHeaders() {
        return httpHeaders;
    }

    public List<ServletHttpParameter> getHttpParameters() {
        return httpParameters;
    }

    public String getHttpRemoteAddress() {
        return httpRemoteAddress;
    }

    public String getHttpRemoteHost() {
        return httpRemoteHost;
    }

    public Integer getHttpRemotePort() {
        return httpRemotePort;
    }

    public String getUser() {
        return user;
    }
}


