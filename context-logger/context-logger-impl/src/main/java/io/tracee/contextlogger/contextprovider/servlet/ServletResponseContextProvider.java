package io.tracee.contextlogger.contextprovider.servlet;

import io.tracee.contextlogger.api.TraceeContextProvider;
import io.tracee.contextlogger.api.TraceeContextProviderMethod;
import io.tracee.contextlogger.api.WrappedContextData;
import io.tracee.contextlogger.contextprovider.Order;
import io.tracee.contextlogger.contextprovider.utility.NameStringValuePair;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Context provider for HttpServletResponse.
 * Created by Tobias Gindler, holisticon AG on 20.03.14.
 */
@TraceeContextProvider(displayName = "servletResponse", order = Order.SERVLET)
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
    @TraceeContextProviderMethod(
            displayName = "http-status-code",
            order = 10
    )
    public Integer getHttpStatusCode() {
        if (this.response != null) {
            return this.response.getStatus();
        }
        return null;
    }

    @SuppressWarnings("unused")
    @TraceeContextProviderMethod(
            displayName = "http-header",
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
