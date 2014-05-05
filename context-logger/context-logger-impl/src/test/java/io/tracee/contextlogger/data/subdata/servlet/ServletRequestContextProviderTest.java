package io.tracee.contextlogger.data.subdata.servlet;

import io.tracee.contextlogger.data.subdata.NameObjectValuePair;
import io.tracee.contextlogger.data.subdata.NameStringValuePair;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

/**
 * Test class for {@link io.tracee.contextlogger.data.subdata.servlet.ServletRequestContextProvider}
 * Created by Tobias Gindler, holisticon AG on 01.04.14.
 */
public class ServletRequestContextProviderTest {

    private final static String KEY = "key";
    private final static String VALUE = "value";

    private Enumeration<String> NAMES;

    @Before
    public void init() {
        NAMES = new Enumeration<String>() {
            boolean first = true;

            @Override
            public boolean hasMoreElements() {
                if (first) {
                    first = false;
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public String nextElement() {
                return KEY;
            }
        };
    }


    @Test
    public void should_return_wrapped_type() {

        ServletRequestContextProvider givenServletRequestContextProvider = new ServletRequestContextProvider();

        Class result = givenServletRequestContextProvider.getWrappedType();

        MatcherAssert.assertThat(result.equals(HttpServletRequest.class), Matchers.equalTo(true));

    }

    @Test
    public void should_return_null_for_url() {
        ServletRequestContextProvider givenServletRequestContextProvider = new ServletRequestContextProvider();

        String result = givenServletRequestContextProvider.getUrl();

        MatcherAssert.assertThat(result, Matchers.nullValue());
    }

    @Test
    public void should_return_null_for_http_method() {
        ServletRequestContextProvider givenServletRequestContextProvider = new ServletRequestContextProvider();

        String result = givenServletRequestContextProvider.getHttpMethod();

        MatcherAssert.assertThat(result, Matchers.nullValue());
    }

    @Test
    public void should_return_null_for_request_parameters() {
        ServletRequestContextProvider givenServletRequestContextProvider = new ServletRequestContextProvider();

        List<NameStringValuePair> result = givenServletRequestContextProvider.getHttpParameters();

        MatcherAssert.assertThat(result, Matchers.nullValue());
    }

    @Test
    public void should_return_null_for_http_headers() {
        ServletRequestContextProvider givenServletRequestContextProvider = new ServletRequestContextProvider();

        List<NameStringValuePair> result = givenServletRequestContextProvider.getHttpRequestHeaders();

        MatcherAssert.assertThat(result, Matchers.nullValue());
    }

    @Test
    public void should_return_null_for_request_attributes() {
        ServletRequestContextProvider givenServletRequestContextProvider = new ServletRequestContextProvider();

        List<NameObjectValuePair> result = givenServletRequestContextProvider.getHttpRequestAttributes();

        MatcherAssert.assertThat(result, Matchers.nullValue());
    }

    @Test
    public void should_return_null_for_cookies() {
        ServletRequestContextProvider givenServletRequestContextProvider = new ServletRequestContextProvider();

        List<ServletCookieContextProvider> result = givenServletRequestContextProvider.getCookies();

        MatcherAssert.assertThat(result, Matchers.nullValue());
    }


    @Test
    public void should_return_null_for_remote_address() {
        ServletRequestContextProvider givenServletRequestContextProvider = new ServletRequestContextProvider();

        String result = givenServletRequestContextProvider.getHttpRemoteAddress();

        MatcherAssert.assertThat(result, Matchers.nullValue());
    }

    @Test
    public void should_return_null_for_remote_host() {
        ServletRequestContextProvider givenServletRequestContextProvider = new ServletRequestContextProvider();

        String result = givenServletRequestContextProvider.getHttpRemoteHost();

        MatcherAssert.assertThat(result, Matchers.nullValue());
    }

    @Test
    public void should_return_null_for_remote_port() {
        ServletRequestContextProvider givenServletRequestContextProvider = new ServletRequestContextProvider();

        Integer result = givenServletRequestContextProvider.getHttpRemotePort();

        MatcherAssert.assertThat(result, Matchers.nullValue());
    }

    @Test
    public void should_return_null_for_scheme() {
        ServletRequestContextProvider givenServletRequestContextProvider = new ServletRequestContextProvider();

        String result = givenServletRequestContextProvider.getScheme();

        MatcherAssert.assertThat(result, Matchers.nullValue());
    }

    @Test
    public void should_return_null_for_is_secure() {
        ServletRequestContextProvider givenServletRequestContextProvider = new ServletRequestContextProvider();

        Boolean result = givenServletRequestContextProvider.getSecure();

        MatcherAssert.assertThat(result, Matchers.nullValue());
    }

    @Test
    public void should_return_null_for_content_type() {
        ServletRequestContextProvider givenServletRequestContextProvider = new ServletRequestContextProvider();

        String result = givenServletRequestContextProvider.getContentType();

        MatcherAssert.assertThat(result, Matchers.nullValue());
    }

    @Test
    public void should_return_null_for_content_length() {
        ServletRequestContextProvider givenServletRequestContextProvider = new ServletRequestContextProvider();

        Integer result = givenServletRequestContextProvider.getContentLength();

        MatcherAssert.assertThat(result, Matchers.nullValue());
    }

    @Test
    public void should_return_null_for_locale() {
        ServletRequestContextProvider givenServletRequestContextProvider = new ServletRequestContextProvider();

        String result = givenServletRequestContextProvider.getLocale();

        MatcherAssert.assertThat(result, Matchers.nullValue());
    }


    @Test
    public void should_return_url() {

        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getRequestURL()).thenReturn(new StringBuffer("url"));

        ServletRequestContextProvider givenServletRequestContextProvider = new ServletRequestContextProvider();
        givenServletRequestContextProvider.setContextData(request);

        String result = givenServletRequestContextProvider.getUrl();

        MatcherAssert.assertThat(result, Matchers.equalTo("url"));
    }

    @Test
    public void should_return_http_method() {

        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getMethod()).thenReturn("POST");

        ServletRequestContextProvider givenServletRequestContextProvider = new ServletRequestContextProvider();
        givenServletRequestContextProvider.setContextData(request);

        String result = givenServletRequestContextProvider.getHttpMethod();

        MatcherAssert.assertThat(result, Matchers.equalTo("POST"));
    }

    @Test
    public void should_return_request_parameters() {

        String[] values = {VALUE};

        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getParameterNames()).thenReturn(NAMES);
        Mockito.when(request.getParameterValues(KEY)).thenReturn(values);

        ServletRequestContextProvider givenServletRequestContextProvider = new ServletRequestContextProvider();
        givenServletRequestContextProvider.setContextData(request);

        List<NameStringValuePair> result = givenServletRequestContextProvider.getHttpParameters();

        MatcherAssert.assertThat(result, Matchers.notNullValue());
        MatcherAssert.assertThat(result.size(), Matchers.equalTo(1));
        MatcherAssert.assertThat(result.get(0).getName(), Matchers.equalTo(KEY));
        MatcherAssert.assertThat(result.get(0).getValue(), Matchers.equalTo(VALUE));
    }

    @Test
    public void should_return_http_headers() {

        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeaderNames()).thenReturn(NAMES);
        Mockito.when(request.getHeader(KEY)).thenReturn(VALUE);

        ServletRequestContextProvider givenServletRequestContextProvider = new ServletRequestContextProvider();
        givenServletRequestContextProvider.setContextData(request);

        List<NameStringValuePair> result = givenServletRequestContextProvider.getHttpRequestHeaders();

        MatcherAssert.assertThat(result, Matchers.notNullValue());
        MatcherAssert.assertThat(result.size(), Matchers.equalTo(1));
        MatcherAssert.assertThat(result.get(0).getName(), Matchers.equalTo(KEY));
        MatcherAssert.assertThat(result.get(0).getValue(), Matchers.equalTo(VALUE));
    }

    @Test
    public void should_return_request_attributes() {


        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getAttributeNames()).thenReturn(NAMES);
        Mockito.when(request.getAttribute(KEY)).thenReturn(VALUE);

        ServletRequestContextProvider givenServletRequestContextProvider = new ServletRequestContextProvider();
        givenServletRequestContextProvider.setContextData(request);

        List<NameObjectValuePair> result = givenServletRequestContextProvider.getHttpRequestAttributes();

        MatcherAssert.assertThat(result, Matchers.notNullValue());
        MatcherAssert.assertThat(result.size(), Matchers.equalTo(1));
        MatcherAssert.assertThat(result.get(0).getName(), Matchers.equalTo(KEY));
        MatcherAssert.assertThat(result.get(0).getValue(), Matchers.equalTo((Object) VALUE));
    }

    @Test
    public void should_return_cookies() {

        Cookie[] cookies = {new Cookie("name", "value")};

        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getCookies()).thenReturn(cookies);

        ServletRequestContextProvider givenServletRequestContextProvider = new ServletRequestContextProvider();
        givenServletRequestContextProvider.setContextData(request);

        List<ServletCookieContextProvider> result = givenServletRequestContextProvider.getCookies();

        MatcherAssert.assertThat(result, Matchers.notNullValue());
        MatcherAssert.assertThat(result.size(), Matchers.equalTo(1));
        MatcherAssert.assertThat(result.get(0).getName(), Matchers.equalTo("name"));
        MatcherAssert.assertThat(result.get(0).getValue(), Matchers.equalTo("value"));
    }


    @Test
    public void should_return_remote_address() {

        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getRemoteAddr()).thenReturn("123.123.123.123");

        ServletRequestContextProvider givenServletRequestContextProvider = new ServletRequestContextProvider();
        givenServletRequestContextProvider.setContextData(request);

        String result = givenServletRequestContextProvider.getHttpRemoteAddress();

        MatcherAssert.assertThat(result, Matchers.equalTo("123.123.123.123"));
    }

    @Test
    public void should_return_remote_host() {

        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getRemoteHost()).thenReturn("HOST");

        ServletRequestContextProvider givenServletRequestContextProvider = new ServletRequestContextProvider();
        givenServletRequestContextProvider.setContextData(request);

        String result = givenServletRequestContextProvider.getHttpRemoteHost();

        MatcherAssert.assertThat(result, Matchers.equalTo("HOST"));
    }

    @Test
    public void should_return_remote_port() {

        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getRemotePort()).thenReturn(8080);

        ServletRequestContextProvider givenServletRequestContextProvider = new ServletRequestContextProvider();
        givenServletRequestContextProvider.setContextData(request);

        Integer result = givenServletRequestContextProvider.getHttpRemotePort();

        MatcherAssert.assertThat(result, Matchers.equalTo(8080));
    }

    @Test
    public void should_return_scheme() {

        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getScheme()).thenReturn("HTTPS");

        ServletRequestContextProvider givenServletRequestContextProvider = new ServletRequestContextProvider();
        givenServletRequestContextProvider.setContextData(request);

        String result = givenServletRequestContextProvider.getScheme();

        MatcherAssert.assertThat(result, Matchers.equalTo("HTTPS"));
    }

    @Test
    public void should_return_is_secure() {

        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.isSecure()).thenReturn(true);

        ServletRequestContextProvider givenServletRequestContextProvider = new ServletRequestContextProvider();
        givenServletRequestContextProvider.setContextData(request);

        Boolean result = givenServletRequestContextProvider.getSecure();

        MatcherAssert.assertThat(result, Matchers.equalTo(true));
    }

    @Test
    public void should_return_content_type() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getContentType()).thenReturn("txt");

        ServletRequestContextProvider givenServletRequestContextProvider = new ServletRequestContextProvider();
        givenServletRequestContextProvider.setContextData(request);

        String result = givenServletRequestContextProvider.getContentType();

        MatcherAssert.assertThat(result, Matchers.equalTo("txt"));
    }

    @Test
    public void should_return_content_length() {

        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getContentLength()).thenReturn(10);

        ServletRequestContextProvider givenServletRequestContextProvider = new ServletRequestContextProvider();
        givenServletRequestContextProvider.setContextData(request);

        Integer result = givenServletRequestContextProvider.getContentLength();

        MatcherAssert.assertThat(result, Matchers.equalTo(10));
    }

    @Test
    public void should_return_locale() {

        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getLocale()).thenReturn(Locale.GERMANY);

        ServletRequestContextProvider givenServletRequestContextProvider = new ServletRequestContextProvider();
        givenServletRequestContextProvider.setContextData(request);

        String result = givenServletRequestContextProvider.getLocale();

        MatcherAssert.assertThat(result, Matchers.equalTo("de_DE"));
    }


}
