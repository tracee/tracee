package io.tracee.contextlogger.data.subdata.servlet;

import io.tracee.contextlogger.data.subdata.NameStringValuePair;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link io.tracee.contextlogger.data.subdata.servlet.ServletSessionContextProvider}.
 * Created by Tobias Gindler, holisticon AG on 01.04.14.
 */
public class ServletSessionContextProviderTest {

	private final ServletSessionContextProvider unit = new ServletSessionContextProvider();

    @Test
    public void should_return_wrapped_type() {
		assertThat(unit.getWrappedType(), equalTo(HttpSession.class));
    }

    @Test
    public void should_return_null_if_wrapped_session_is_null() {
		assertThat(unit.getSessionAttributes(), nullValue());
    }

    @Test
    public void should_return_session_attributes() {

        final Enumeration<String> names = new Enumeration<String>() {
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

        HttpSession session = mock(HttpSession.class);
        when(session.getAttributeNames()).thenReturn(names);
        when(session.getAttribute("key")).thenReturn("value");

        unit.setContextData(session);

        final List<NameStringValuePair> result = unit.getSessionAttributes();

        assertThat(result, Matchers.notNullValue());
        assertThat(result.size(), equalTo(1));
        assertThat(result.get(0).getName(), equalTo("key"));
        assertThat(result.get(0).getValue(), equalTo("value"));
    }
}
