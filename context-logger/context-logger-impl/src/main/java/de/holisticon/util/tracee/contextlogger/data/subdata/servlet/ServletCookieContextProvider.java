package de.holisticon.util.tracee.contextlogger.data.subdata.servlet;

import de.holisticon.util.tracee.contextlogger.api.TraceeContextLogProvider;
import de.holisticon.util.tracee.contextlogger.api.TraceeContextLogProviderMethod;
import de.holisticon.util.tracee.contextlogger.api.WrappedContextData;
import de.holisticon.util.tracee.contextlogger.data.Order;
import de.holisticon.util.tracee.contextlogger.profile.ProfilePropertyNames;

import javax.servlet.http.Cookie;

/**
 * Context provider for ServletCookieContextProvider.
 * Created by Tobias Gindler, holisticon AG on 24.01.14.
 */
@TraceeContextLogProvider(displayName = "servletCookies", order = Order.SERVLET)
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
	@TraceeContextLogProviderMethod(
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
	@TraceeContextLogProviderMethod(
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
	@TraceeContextLogProviderMethod(
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
	@TraceeContextLogProviderMethod(
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
	@TraceeContextLogProviderMethod(
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
	@TraceeContextLogProviderMethod(
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
