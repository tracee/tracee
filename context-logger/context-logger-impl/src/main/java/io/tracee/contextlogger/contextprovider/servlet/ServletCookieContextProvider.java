package io.tracee.contextlogger.contextprovider.servlet;

import io.tracee.contextlogger.api.TraceeContextProvider;
import io.tracee.contextlogger.api.TraceeContextProviderMethod;
import io.tracee.contextlogger.api.WrappedContextData;
import io.tracee.contextlogger.contextprovider.Order;

import javax.servlet.http.Cookie;

/**
 * Context provider for ServletCookieContextProvider.
 * Created by Tobias Gindler, holisticon AG on 24.01.14.
 */
@TraceeContextProvider(displayName = "servletCookies", order = Order.SERVLET)
public final class ServletCookieContextProvider implements WrappedContextData<Cookie> {

    private Cookie cookie;

    public ServletCookieContextProvider() {
    }

    public ServletCookieContextProvider(Cookie cookie) {
        this.cookie = cookie;
    }

    @Override
    public void setContextData(Object instance) throws ClassCastException {
        this.cookie = (Cookie) instance;
    }

    @Override
    public Class<Cookie> getWrappedType() {
        return Cookie.class;
    }

    @SuppressWarnings("unused")
    @TraceeContextProviderMethod(
            displayName = "name",
            order = 10)
    public String getName() {
        if (cookie != null) {
            return cookie.getName();
        }
        return null;
    }

    @SuppressWarnings("unused")
    @TraceeContextProviderMethod(
            displayName = "value",
            order = 20)
    public String getValue() {
        if (cookie != null) {
            return cookie.getValue();
        }
        return null;
    }

    @SuppressWarnings("unused")
    @TraceeContextProviderMethod(
            displayName = "domain",
            order = 30)
    public String getDomain() {
        if (cookie != null) {
            return cookie.getDomain();
        }
        return null;
    }

    @SuppressWarnings("unused")
    @TraceeContextProviderMethod(
            displayName = "path",
            order = 40
    )
    public String getPath() {
        if (cookie != null) {
            return cookie.getPath();
        }
        return null;
    }

    @SuppressWarnings("unused")
    @TraceeContextProviderMethod(
            displayName = "secure",
            order = 50)
    public Boolean getSecure() {
        if (cookie != null) {
            return cookie.getSecure();
        }
        return null;
    }

    @SuppressWarnings("unused")
    @TraceeContextProviderMethod(
            displayName = "max-age",
            order = 60)
    public Integer getMaxAge() {
        if (cookie != null) {
            return cookie.getMaxAge();
        }
        return null;
    }

}
