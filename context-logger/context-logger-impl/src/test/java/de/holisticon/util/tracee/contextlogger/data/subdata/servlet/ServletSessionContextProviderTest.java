package de.holisticon.util.tracee.contextlogger.data.subdata.servlet;

import de.holisticon.util.tracee.contextlogger.data.subdata.NameStringValuePair;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.List;

/**
 * Test class for {@link de.holisticon.util.tracee.contextlogger.data.subdata.servlet.ServletSessionContextProvider}.
 * Created by Tobias Gindler, holisticon AG on 01.04.14.
 */
public class ServletSessionContextProviderTest {

    @Test
    public void should_return_wrapped_type() {

        ServletSessionContextProvider givenServletSessionContextProvider = new ServletSessionContextProvider();

        Class result = givenServletSessionContextProvider.getWrappedType();

        MatcherAssert.assertThat(result.equals(HttpSession.class), Matchers.equalTo(true));

    }


    @Test
    public void should_return_null_if_wrapped_session_is_null() {

        ServletSessionContextProvider servletSessionContextProvider = new ServletSessionContextProvider();

        List<NameStringValuePair> result = servletSessionContextProvider.getSessionAttributes();

        MatcherAssert.assertThat(result, Matchers.nullValue());
    }

    @Test
    public void should_return_session_attributes() {

        Enumeration<String> names = new Enumeration<String>() {
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
                return "key";
            }
        };

        HttpSession session = Mockito.mock(HttpSession.class);
        Mockito.when(session.getAttributeNames()).thenReturn(names);
        Mockito.when(session.getAttribute("key")).thenReturn("value");

        ServletSessionContextProvider servletSessionContextProvider = new ServletSessionContextProvider();
        servletSessionContextProvider.setContextData(session);

        List<NameStringValuePair> result = servletSessionContextProvider.getSessionAttributes();

        MatcherAssert.assertThat(result, Matchers.notNullValue());
        MatcherAssert.assertThat(result.size(), Matchers.equalTo(1));
        MatcherAssert.assertThat(result.get(0).getName(), Matchers.equalTo("key"));
        MatcherAssert.assertThat(result.get(0).getValue(), Matchers.equalTo("value"));

    }


}
