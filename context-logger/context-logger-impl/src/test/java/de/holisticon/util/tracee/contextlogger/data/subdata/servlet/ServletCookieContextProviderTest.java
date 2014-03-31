package de.holisticon.util.tracee.contextlogger.data.subdata.servlet;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.Cookie;

/**
 * Test class for {@link de.holisticon.util.tracee.contextlogger.data.subdata.servlet.ServletCookieContextProvider}.
 * Created by Tobias Gindler, holisticon ag on 01.04.14.
 */
public class ServletCookieContextProviderTest {

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

        ServletCookieContextProvider givenServletCookieContextProvider = new ServletCookieContextProvider();

        Class result = givenServletCookieContextProvider.getWrappedType();

        MatcherAssert.assertThat(result.equals(Cookie.class), Matchers.equalTo(true));

    }


    @Test
    public void should_return_null_for_name_if_wrapped_cookie_is_null() {

        ServletCookieContextProvider givenJaxWsContextProvider = new ServletCookieContextProvider();

        String result = givenJaxWsContextProvider.getName();

        MatcherAssert.assertThat(result, Matchers.nullValue());

    }

    @Test
    public void should_return_null_for_value_if_wrapped_cookie_is_null() {

        ServletCookieContextProvider givenJaxWsContextProvider = new ServletCookieContextProvider();

        String result = givenJaxWsContextProvider.getValue();

        MatcherAssert.assertThat(result, Matchers.nullValue());

    }

    @Test
    public void should_return_null_for_domain_if_wrapped_cookie_is_null() {

        ServletCookieContextProvider givenJaxWsContextProvider = new ServletCookieContextProvider();

        String result = givenJaxWsContextProvider.getDomain();

        MatcherAssert.assertThat(result, Matchers.nullValue());

    }

    @Test
    public void should_return_null_for_path_if_wrapped_cookie_is_null() {

        ServletCookieContextProvider givenJaxWsContextProvider = new ServletCookieContextProvider();

        String result = givenJaxWsContextProvider.getPath();

        MatcherAssert.assertThat(result, Matchers.nullValue());

    }

    @Test
    public void should_return_null_for_secure_if_wrapped_cookie_is_null() {

        ServletCookieContextProvider givenJaxWsContextProvider = new ServletCookieContextProvider();

        Boolean result = givenJaxWsContextProvider.getSecure();

        MatcherAssert.assertThat(result, Matchers.nullValue());

    }

    @Test
    public void should_return_null_for_maxage_if_wrapped_cookie_is_null() {

        ServletCookieContextProvider givenJaxWsContextProvider = new ServletCookieContextProvider();

        Integer result = givenJaxWsContextProvider.getMaxAge();

        MatcherAssert.assertThat(result, Matchers.nullValue());

    }

    @Test
    public void should_return_name() {

        ServletCookieContextProvider givenJaxWsContextProvider = new ServletCookieContextProvider();
        givenJaxWsContextProvider.setContextData(cookie);

        String result = givenJaxWsContextProvider.getName();

        MatcherAssert.assertThat(result, Matchers.equalTo(cookie.getName()));

    }

    @Test
    public void should_return_value() {

        ServletCookieContextProvider givenJaxWsContextProvider = new ServletCookieContextProvider();
        givenJaxWsContextProvider.setContextData(cookie);

        String result = givenJaxWsContextProvider.getValue();

        MatcherAssert.assertThat(result, Matchers.equalTo(cookie.getValue()));

    }

    @Test
    public void should_return_domain() {

        ServletCookieContextProvider givenJaxWsContextProvider = new ServletCookieContextProvider();
        givenJaxWsContextProvider.setContextData(cookie);

        String result = givenJaxWsContextProvider.getDomain();

        MatcherAssert.assertThat(result, Matchers.equalTo(cookie.getDomain()));

    }

    @Test
    public void should_return_path() {

        ServletCookieContextProvider givenJaxWsContextProvider = new ServletCookieContextProvider();
        givenJaxWsContextProvider.setContextData(cookie);

        String result = givenJaxWsContextProvider.getPath();

        MatcherAssert.assertThat(result, Matchers.equalTo(cookie.getPath()));

    }

    @Test
    public void should_return_secure() {

        ServletCookieContextProvider givenJaxWsContextProvider = new ServletCookieContextProvider();
        givenJaxWsContextProvider.setContextData(cookie);

        Boolean result = givenJaxWsContextProvider.getSecure();

        MatcherAssert.assertThat(result, Matchers.equalTo(cookie.getSecure()));

    }

    @Test
    public void should_return_maxage() {

        ServletCookieContextProvider givenJaxWsContextProvider = new ServletCookieContextProvider();
        givenJaxWsContextProvider.setContextData(cookie);

        Integer result = givenJaxWsContextProvider.getMaxAge();

        MatcherAssert.assertThat(result, Matchers.equalTo(cookie.getMaxAge()));

    }
}
