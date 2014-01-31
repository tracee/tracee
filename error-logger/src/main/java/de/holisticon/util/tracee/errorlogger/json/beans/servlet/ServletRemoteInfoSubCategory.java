package de.holisticon.util.tracee.errorlogger.json.beans.servlet;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * Created by Tobias Gindler, holisticon AG on 24.01.14.
 */
@JsonAutoDetect(
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE,
        fieldVisibility = JsonAutoDetect.Visibility.ANY
)
@JsonPropertyOrder(
        value = {
                ServletRemoteInfoSubCategory.ATTR_HTTP_REMOTE_ADDRESS,
                ServletRemoteInfoSubCategory.ATTR_HTTP_REMOTE_HOST,
                ServletRemoteInfoSubCategory.ATTR_HTTP_REMOTE_PORT
        }
)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public final class ServletRemoteInfoSubCategory {

    public static final String ATTR_HTTP_REMOTE_ADDRESS = "http-remote-address";
    public static final String ATTR_HTTP_REMOTE_HOST = "http-remote-host";
    public static final String ATTR_HTTP_REMOTE_PORT = "http-remote-port";

    @JsonProperty(value = ATTR_HTTP_REMOTE_ADDRESS)
    private final String httpRemoteAddress;
    @JsonProperty(value = ATTR_HTTP_REMOTE_HOST)
    private final String httpRemoteHost;
    @JsonProperty(value = ATTR_HTTP_REMOTE_PORT)
    private final Integer httpRemotePort;


    @SuppressWarnings("unused")
    private ServletRemoteInfoSubCategory() {
        this(null, null, null);
    }

    public ServletRemoteInfoSubCategory (String httpRemoteAddress,
                                         String httpRemoteHost,
                                         Integer httpRemotePort) {
        this.httpRemoteAddress = httpRemoteAddress;
        this.httpRemoteHost = httpRemoteHost;
        this.httpRemotePort = httpRemotePort;
    }

    @SuppressWarnings("unused")
    public String getHttpRemoteAddress() {
        return httpRemoteAddress;
    }

    @SuppressWarnings("unused")
    public String getHttpRemoteHost() {
        return httpRemoteHost;
    }

    @SuppressWarnings("unused")
    public Integer getHttpRemotePort() {
        return httpRemotePort;
    }

}
