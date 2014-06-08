package io.tracee.contextlogger.data.subdata.tracee;

import io.tracee.Tracee;
import io.tracee.contextlogger.ImplicitContext;
import io.tracee.contextlogger.data.subdata.NameStringValuePair;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

/**
 * Test class for  {@link io.tracee.contextlogger.data.subdata.tracee.TraceeContextProvider}.
 * Created by Tobias Gindler, holisticon AG on 31.03.14.
 */
public class TraceeContextProviderTest {

    @Test
    public void should_return_implicit_context() {
        final ImplicitContext implicitContext = new TraceeContextProvider().getImplicitContext();

        assertThat(implicitContext, notNullValue());
        assertThat(implicitContext, equalTo(ImplicitContext.TRACEE));
    }

    @Test
    public void should_return_null_for_empty_mdc() {

        final TraceeContextProvider traceeContextProvider = new TraceeContextProvider();

		assertThat(traceeContextProvider.getNameValuePairs(), nullValue());
    }

    @Test
    public void should_return_tracee_properties() {

        Tracee.getBackend().put("ID", "ID_value");

        final TraceeContextProvider traceeContextProvider = new TraceeContextProvider();

        final List<NameStringValuePair> result = traceeContextProvider.getNameValuePairs();

        assertThat(result, notNullValue());
        assertThat(result.size(), equalTo(1));
        assertThat(result.get(0).getName(), equalTo("ID"));
        assertThat(result.get(0).getValue(), equalTo("ID_value"));
    }
}
