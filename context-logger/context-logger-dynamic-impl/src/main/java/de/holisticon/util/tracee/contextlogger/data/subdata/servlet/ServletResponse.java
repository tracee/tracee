package de.holisticon.util.tracee.contextlogger.data.subdata.servlet;

import de.holisticon.util.tracee.contextlogger.api.TraceeContextLogProvider;
import de.holisticon.util.tracee.contextlogger.api.TraceeContextLogProviderMethod;
import de.holisticon.util.tracee.contextlogger.data.subdata.NameValuePair;
import de.holisticon.util.tracee.contextlogger.profile.ProfilePropertyNames;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Context provider for HttpServletResponse.
 * Created by Tobias Gindler, holisticon AG on 20.03.14.
 */
@TraceeContextLogProvider(displayName = "servletResponse")
public class ServletResponse {

    final HttpServletResponse response;

    public ServletResponse (final HttpServletResponse response) {
        this.response = response;
    }

    @SuppressWarnings("unused")
    @TraceeContextLogProviderMethod(
            displayName = "http-status-code",
            propertyName = ProfilePropertyNames.SERVLET_RESPONSE_HTTP_STATUS_CODE,
            order = 10
    )
    public Integer getHttpStatusCode() {
        return this.response.getStatus();
    }

    @SuppressWarnings("unused")
    @TraceeContextLogProviderMethod(
            displayName = "http-header",
            propertyName = ProfilePropertyNames.SERVLET_RESPONSE_HTTP_HEADER,
            order = 20
    )
    public List<NameValuePair> getHttpResponseHeaders() {
        final List<NameValuePair> list = new ArrayList<NameValuePair>();

        if (this.response != null) {
            final Collection<String> httpHeaderNames = this.response.getHeaderNames();
            for (final String httpHeaderName : httpHeaderNames) {

                final String value = this.response.getHeader(httpHeaderName);
                list.add(new NameValuePair(httpHeaderName, value));

            }
        }

        return list.size() > 0 ? list : null;
    }


}
