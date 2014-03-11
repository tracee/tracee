package de.holisticon.util.tracee.contextlogger.json.beans.servlet;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Tobias Gindler, holisticon AG on 24.01.14.
 */
public final class ServletRemoteInfoSubCategory {

    public static final String ATTR_HTTP_REMOTE_ADDRESS = "http-remote-address";
    public static final String ATTR_HTTP_REMOTE_HOST = "http-remote-host";
    public static final String ATTR_HTTP_REMOTE_PORT = "http-remote-port";

    @SerializedName(ATTR_HTTP_REMOTE_ADDRESS)
    private final String httpRemoteAddress;
    @SerializedName(ATTR_HTTP_REMOTE_HOST)
    private final String httpRemoteHost;
    @SerializedName(ATTR_HTTP_REMOTE_PORT)
    private final Integer httpRemotePort;


    @SuppressWarnings("unused")
    private ServletRemoteInfoSubCategory() {
        this(null, null, null);
    }

    public ServletRemoteInfoSubCategory(String httpRemoteAddress,
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
