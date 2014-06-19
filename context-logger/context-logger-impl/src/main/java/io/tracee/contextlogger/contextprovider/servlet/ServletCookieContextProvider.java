package io.tracee.contextlogger.contextprovider.servlet;

import io.tracee.contextlogger.api.TraceeContextProvider;
import io.tracee.contextlogger.api.TraceeContextProviderMethod;
import io.tracee.contextlogger.api.WrappedContextData;
import io.tracee.contextlogger.contextprovider.Order;
import io.tracee.contextlogger.profile.ProfilePropertyNames;

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
			propertyName = ProfilePropertyNames.COOKIE_NAME,
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
			propertyName = ProfilePropertyNames.COOKIE_VALUE,
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
			propertyName = ProfilePropertyNames.COOKIE_DOMAIN,
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
			propertyName = ProfilePropertyNames.COOKIE_PATH,
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
			propertyName = ProfilePropertyNames.COOKIE_SECURE,
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
			propertyName = ProfilePropertyNames.COOKIE_MAXAGE,
			order = 60)
	public Integer getMaxAge() {
		if (cookie != null) {
			return cookie.getMaxAge();
		}
		return null;
	}

}
