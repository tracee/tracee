package de.holisticon.util.tracee.contextlogger.data.subdata.servlet;

import de.holisticon.util.tracee.contextlogger.api.TraceeContextLogProvider;
import de.holisticon.util.tracee.contextlogger.api.TraceeContextLogProviderMethod;
import de.holisticon.util.tracee.contextlogger.api.WrappedContextData;
import de.holisticon.util.tracee.contextlogger.profile.ProfilePropertyNames;

import javax.servlet.http.Cookie;

/**
 * Context provider for ServletCookie.
 * Created by Tobias Gindler, holisticon AG on 24.01.14.
 */
@TraceeContextLogProvider(displayName = "cookies")
public final class ServletCookie implements WrappedContextData<Cookie> {

    private Cookie cookie;

    public ServletCookie() {
    }

    public ServletCookie(Cookie cookie) {
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
    @TraceeContextLogProviderMethod(
            displayName = "name",
            propertyName = ProfilePropertyNames.COOKIE_NAME,
            order = 10)
    public String getName() {
        return cookie.getName();
    }

    @SuppressWarnings("unused")
    @TraceeContextLogProviderMethod(
            displayName = "value",
            propertyName = ProfilePropertyNames.COOKIE_VALUE,
            order = 20)
    public String getValue() {
        return cookie.getValue();
    }

    @SuppressWarnings("unused")
    @TraceeContextLogProviderMethod(
            displayName = "domain",
            propertyName = ProfilePropertyNames.COOKIE_DOMAIN,
            order = 30)
    public String getDomain() {
        return cookie.getDomain();
    }

    @SuppressWarnings("unused")
    @TraceeContextLogProviderMethod(
            displayName = "path",
            propertyName = ProfilePropertyNames.COOKIE_PATH,
            order = 40
    )
    public String getPath() {
        return cookie.getPath();
    }

    @SuppressWarnings("unused")
    @TraceeContextLogProviderMethod(
            displayName = "secure",
            propertyName = ProfilePropertyNames.COOKIE_SECURE,
            order = 50)
    public Boolean getSecure() {
        return cookie.getSecure();
    }

    @SuppressWarnings("unused")
    @TraceeContextLogProviderMethod(
            displayName = "max-age",
            propertyName = ProfilePropertyNames.COOKIE_MAXAGE,
            order = 60)
    public Integer getMaxAge() {
        return cookie.getMaxAge();
    }

}
