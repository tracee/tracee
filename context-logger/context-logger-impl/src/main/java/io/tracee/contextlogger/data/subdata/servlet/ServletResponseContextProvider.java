package io.tracee.contextlogger.data.subdata.servlet;

import io.tracee.contextlogger.api.TraceeContextLogProvider;
import io.tracee.contextlogger.api.TraceeContextLogProviderMethod;
import io.tracee.contextlogger.api.WrappedContextData;
import io.tracee.contextlogger.data.Order;
import io.tracee.contextlogger.data.subdata.NameStringValuePair;
import io.tracee.contextlogger.profile.ProfilePropertyNames;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Context provider for HttpServletResponse.
 * Created by Tobias Gindler, holisticon AG on 20.03.14.
 */
@TraceeContextLogProvider(displayName = "servletResponse", order = Order.SERVLET)
public final class ServletResponseContextProvider implements WrappedContextData<HttpServletResponse> {

    private HttpServletResponse response;

    public ServletResponseContextProvider() {
    }

    public ServletResponseContextProvider(final HttpServletResponse response) {
        this.response = response;
    }

    @Override
    public void setContextData(Object instance) throws ClassCastException {
        this.response = (HttpServletResponse) instance;
    }

    @Override
    public Class<HttpServletResponse> getWrappedType() {
        return HttpServletResponse.class;
    }

    @SuppressWarnings("unused")
    @TraceeContextLogProviderMethod(
            displayName = "http-status-code",
            propertyName = ProfilePropertyNames.SERVLET_RESPONSE_HTTP_STATUS_CODE,
            order = 10
    )
    public Integer getHttpStatusCode() {
        if (this.response != null) {
            return this.response.getStatus();
        }
        return null;
    }

    @SuppressWarnings("unused")
    @TraceeContextLogProviderMethod(
            displayName = "http-header",
            propertyName = ProfilePropertyNames.SERVLET_RESPONSE_HTTP_HEADER,
            order = 20
    )
    public List<NameStringValuePair> getHttpResponseHeaders() {
        final List<NameStringValuePair> list = new ArrayList<NameStringValuePair>();


        if (this.response != null && this.response.getHeaderNames() != null) {
            final Collection<String> httpHeaderNames = this.response.getHeaderNames();
            for (final String httpHeaderName : httpHeaderNames) {

                final String value = this.response.getHeader(httpHeaderName);
                list.add(new NameStringValuePair(httpHeaderName, value));

            }
        }

        return list.size() > 0 ? list : null;
    }


}
