package io.tracee.contextlogger.contextprovider.servlet;

import io.tracee.contextlogger.contextprovider.utility.NameStringValuePair;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link ServletResponseContextProvider}.
 * Created by Tobias Gindler, holisticon ag on 01.04.14.
 */
public class ServletResponseContextProviderTest {

	private final ServletResponseContextProvider unit = new ServletResponseContextProvider();

	@Test
	public void should_return_wrapped_type() {
		assertThat(unit.getWrappedType(), equalTo(HttpServletResponse.class));
	}

	@Test
	public void should_return_null_for_status_code() {
		assertThat(unit.getHttpStatusCode(), Matchers.nullValue());
	}

	@Test
	public void should_return_null_for_response_headers() {
		assertThat(unit.getHttpResponseHeaders(), Matchers.nullValue());
	}


	@Test
	public void should_return_status_code() {
		final HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
		when(response.getStatus()).thenReturn(200);

		unit.setContextData(response);

		assertThat(unit.getHttpStatusCode(), equalTo(200));
	}

	@Test
	public void should_return_response_headers() {

		final List<String> list = Arrays.asList("key");

		final HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
		when(response.getHeaderNames()).thenReturn(list);
		when(response.getHeader("key")).thenReturn("value");

		unit.setContextData(response);

		final List<NameStringValuePair> result = unit.getHttpResponseHeaders();

		assertThat(result, notNullValue());
		assertThat(result.size(), equalTo(1));
		assertThat(result.get(0).getName(), equalTo("key"));
		assertThat(result.get(0).getValue(), equalTo("value"));
	}
}
