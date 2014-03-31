package de.holisticon.util.tracee.contextlogger.data.subdata.servlet;

import de.holisticon.util.tracee.contextlogger.api.TraceeContextLogProviderMethod;
import de.holisticon.util.tracee.contextlogger.data.subdata.NameStringValuePair;
import de.holisticon.util.tracee.contextlogger.profile.ProfilePropertyNames;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Test class for {@link de.holisticon.util.tracee.contextlogger.data.subdata.servlet.ServletResponseContextProvider}.
 * Created by Tobias Gindler, holisticon ag on 01.04.14.
 */
public class ServletResponseContextProviderTest {


    @Test
    public void should_return_wrapped_type() {

        ServletResponseContextProvider givenServletResponseContextProvider = new ServletResponseContextProvider();

        Class result = givenServletResponseContextProvider.getWrappedType();

        MatcherAssert.assertThat(result.equals(HttpServletResponse.class), Matchers.equalTo(true));

    }

    @Test
    public void should_return_null_for_status_code () {

        ServletResponseContextProvider givenServletResponseContextProvider = new ServletResponseContextProvider();

        Integer result = givenServletResponseContextProvider.getHttpStatusCode();

        MatcherAssert.assertThat(result, Matchers.nullValue());

    }

    @Test
    public void should_return_null_for_response_headers () {

        ServletResponseContextProvider givenServletResponseContextProvider = new ServletResponseContextProvider();

        List<NameStringValuePair> result = givenServletResponseContextProvider.getHttpResponseHeaders();

        MatcherAssert.assertThat(result, Matchers.nullValue());

    }


    @Test
    public void should_return_status_code () {

        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        Mockito.when(response.getStatus()).thenReturn(200);

        ServletResponseContextProvider givenServletResponseContextProvider = new ServletResponseContextProvider();
        givenServletResponseContextProvider.setContextData(response);

        Integer result = givenServletResponseContextProvider.getHttpStatusCode();

        MatcherAssert.assertThat(result, Matchers.equalTo(200));

    }

    @Test
    public void should_return_response_headers () {

        List<String> list = new ArrayList<String>();
        list.add("key");

        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        Mockito.when(response.getHeaderNames()).thenReturn(list);
        Mockito.when(response.getHeader("key")).thenReturn("value");

        ServletResponseContextProvider givenServletResponseContextProvider = new ServletResponseContextProvider();
        givenServletResponseContextProvider.setContextData(response);

        List<NameStringValuePair> result = givenServletResponseContextProvider.getHttpResponseHeaders();

        MatcherAssert.assertThat(result, Matchers.notNullValue());
        MatcherAssert.assertThat(result.size(), Matchers.equalTo(1));
        MatcherAssert.assertThat(result.get(0).getName(), Matchers.equalTo("key"));
        MatcherAssert.assertThat(result.get(0).getValue(), Matchers.equalTo("value"));


    }

}
