package io.tracee.contextlogger.contextprovider.servlet;

import io.tracee.contextlogger.contextprovider.utility.NameObjectValuePair;
import io.tracee.contextlogger.contextprovider.utility.NameStringValuePair;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link ServletRequestContextProvider}
 * Created by Tobias Gindler, holisticon AG on 01.04.14.
 */
public class ServletRequestContextProviderTest {

	private final ServletRequestContextProvider unit = new ServletRequestContextProvider();

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
		assertThat(unit.getWrappedType(), equalTo(HttpServletRequest.class));
	}

	@Test
	public void should_return_null_for_url() {
		assertThat(unit.getUrl(), nullValue());
	}

	@Test
	public void should_return_null_for_http_method() {
		assertThat(unit.getHttpMethod(), nullValue());
	}

	@Test
	public void should_return_null_for_request_parameters() {
		assertThat(unit.getHttpParameters(), nullValue());
	}

	@Test
	public void should_return_null_for_http_headers() {
		assertThat(unit.getHttpRequestHeaders(), nullValue());
	}

	@Test
	public void should_return_null_for_request_attributes() {
		assertThat(unit.getHttpRequestAttributes(), nullValue());
	}

	@Test
	public void should_return_null_for_cookies() {
		assertThat(unit.getCookies(), nullValue());
	}


	@Test
	public void should_return_null_for_remote_address() {
		assertThat(unit.getHttpRemoteAddress(), nullValue());
	}

	@Test
	public void should_return_null_for_remote_host() {
		assertThat(unit.getHttpRemoteHost(), nullValue());
	}

	@Test
	public void should_return_null_for_remote_port() {
		assertThat(unit.getHttpRemotePort(), nullValue());
	}

	@Test
	public void should_return_null_for_scheme() {
		assertThat(unit.getScheme(), nullValue());
	}

	@Test
	public void should_return_null_for_is_secure() {
		assertThat(unit.getSecure(), nullValue());
	}

	@Test
	public void should_return_null_for_content_type() {
		assertThat(unit.getContentType(), nullValue());
	}

	@Test
	public void should_return_null_for_content_length() {
		assertThat(unit.getContentLength(), nullValue());
	}

	@Test
	public void should_return_null_for_locale() {
		assertThat(unit.getLocale(), nullValue());
	}


	@Test
	public void should_return_url() {
		final HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		when(request.getRequestURL()).thenReturn(new StringBuffer("url"));

		unit.setContextData(request);

		assertThat(unit.getUrl(), equalTo("url"));
	}

	@Test
	public void should_return_http_method() {

		final HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		when(request.getMethod()).thenReturn("POST");

		unit.setContextData(request);

		assertThat(unit.getHttpMethod(), equalTo("POST"));
	}

	@Test
	public void should_return_request_parameters() {

		final String[] values = {VALUE};

		final HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		when(request.getParameterNames()).thenReturn(NAMES);
		when(request.getParameterValues(KEY)).thenReturn(values);

		unit.setContextData(request);

		final List<NameStringValuePair> result = unit.getHttpParameters();

		assertThat(result, Matchers.notNullValue());
		assertThat(result.size(), equalTo(1));
		assertThat(result.get(0).getName(), equalTo(KEY));
		assertThat(result.get(0).getValue(), equalTo(VALUE));
	}

	@Test
	public void should_return_http_headers() {

		final HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		when(request.getHeaderNames()).thenReturn(NAMES);
		when(request.getHeader(KEY)).thenReturn(VALUE);

		unit.setContextData(request);

		final List<NameStringValuePair> result = unit.getHttpRequestHeaders();

		assertThat(result, Matchers.notNullValue());
		assertThat(result.size(), equalTo(1));
		assertThat(result.get(0).getName(), equalTo(KEY));
		assertThat(result.get(0).getValue(), equalTo(VALUE));
	}

	@Test
	public void should_return_request_attributes() {
		final HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		when(request.getAttributeNames()).thenReturn(NAMES);
		when(request.getAttribute(KEY)).thenReturn(VALUE);

		unit.setContextData(request);

		final List<NameObjectValuePair> result = unit.getHttpRequestAttributes();

		assertThat(result, Matchers.notNullValue());
		assertThat(result.size(), equalTo(1));
		assertThat(result.get(0).getName(), equalTo(KEY));
		assertThat(result.get(0).getValue(), equalTo((Object) VALUE));
	}

	@Test
	public void should_return_cookies() {

		final Cookie[] cookies = {new Cookie("name", "value")};

		final HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		when(request.getCookies()).thenReturn(cookies);

		unit.setContextData(request);

		final List<ServletCookieContextProvider> result = unit.getCookies();

		assertThat(result, Matchers.notNullValue());
		assertThat(result.size(), equalTo(1));
		assertThat(result.get(0).getName(), equalTo("name"));
		assertThat(result.get(0).getValue(), equalTo("value"));
	}

	@Test
	public void should_return_remote_address() {
		final HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		when(request.getRemoteAddr()).thenReturn("123.123.123.123");

		unit.setContextData(request);

		assertThat(unit.getHttpRemoteAddress(), equalTo("123.123.123.123"));
	}

	@Test
	public void should_return_remote_host() {
		final HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		when(request.getRemoteHost()).thenReturn("HOST");

		unit.setContextData(request);

		assertThat(unit.getHttpRemoteHost(), equalTo("HOST"));
	}

	@Test
	public void should_return_remote_port() {
		final HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		when(request.getRemotePort()).thenReturn(8080);

		unit.setContextData(request);

		assertThat(unit.getHttpRemotePort(), equalTo(8080));
	}

	@Test
	public void should_return_scheme() {
		final HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		when(request.getScheme()).thenReturn("HTTPS");

		unit.setContextData(request);

		assertThat(unit.getScheme(), equalTo("HTTPS"));
	}

	@Test
	public void should_return_is_secure() {
		final HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		when(request.isSecure()).thenReturn(true);

		unit.setContextData(request);

		assertThat(unit.getSecure(), equalTo(true));
	}

	@Test
	public void should_return_content_type() {
		final HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		when(request.getContentType()).thenReturn("txt");

		unit.setContextData(request);

		assertThat(unit.getContentType(), equalTo("txt"));
	}

	@Test
	public void should_return_content_length() {
		final HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		when(request.getContentLength()).thenReturn(10);

		unit.setContextData(request);

		assertThat(unit.getContentLength(), equalTo(10));
	}

	@Test
	public void should_return_locale() {
		final HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		when(request.getLocale()).thenReturn(Locale.GERMANY);

		unit.setContextData(request);

		assertThat(unit.getLocale(), equalTo("de_DE"));
	}
}
