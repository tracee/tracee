package io.tracee.contextlogger.contextprovider.servlet;

import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.Cookie;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

/**
 * Test class for {@link ServletCookieContextProvider}.
 * Created by Tobias Gindler, holisticon ag on 01.04.14.
 */
public class ServletCookieContextProviderTest {

	private final ServletCookieContextProvider unit = new ServletCookieContextProvider();

    private Cookie cookie;

    @Before
    public void init() {
        cookie = new Cookie("name", "value");
        cookie.setDomain("domain");
        cookie.setSecure(true);
        cookie.setPath("path");
        cookie.setMaxAge(100);
    }

    @Test
    public void should_return_wrapped_type() {
		assertThat(unit.getWrappedType(), equalTo(Cookie.class));
    }


    @Test
    public void should_return_null_for_name_if_wrapped_cookie_is_null() {
		assertThat(unit.getName(), nullValue());
    }

    @Test
    public void should_return_null_for_value_if_wrapped_cookie_is_null() {
		assertThat(unit.getValue(), nullValue());
    }

    @Test
    public void should_return_null_for_domain_if_wrapped_cookie_is_null() {
		assertThat(unit.getDomain(), nullValue());
    }

    @Test
    public void should_return_null_for_path_if_wrapped_cookie_is_null() {
		assertThat(unit.getPath(), nullValue());
    }

    @Test
    public void should_return_null_for_secure_if_wrapped_cookie_is_null() {
		assertThat(unit.getSecure(), nullValue());
    }

    @Test
    public void should_return_null_for_maxage_if_wrapped_cookie_is_null() {
		assertThat(unit.getMaxAge(), nullValue());
    }

    @Test
    public void should_return_name() {
		unit.setContextData(cookie);
		assertThat(unit.getName(), equalTo(cookie.getName()));
    }

    @Test
    public void should_return_value() {
		unit.setContextData(cookie);
		assertThat(unit.getValue(), equalTo(cookie.getValue()));
    }

    @Test
    public void should_return_domain() {
		unit.setContextData(cookie);
		assertThat(unit.getDomain(), equalTo(cookie.getDomain()));
    }

    @Test
    public void should_return_path() {
		unit.setContextData(cookie);
		assertThat(unit.getPath(), equalTo(cookie.getPath()));
    }

    @Test
    public void should_return_secure() {
		unit.setContextData(cookie);
		assertThat(unit.getSecure(), equalTo(cookie.getSecure()));
    }

    @Test
    public void should_return_maxage() {
		unit.setContextData(cookie);
		assertThat(unit.getMaxAge(), equalTo(cookie.getMaxAge()));
    }
}
