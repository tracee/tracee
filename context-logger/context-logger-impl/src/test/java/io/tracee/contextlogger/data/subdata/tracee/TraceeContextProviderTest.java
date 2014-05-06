package io.tracee.contextlogger.data.subdata.tracee;

import io.tracee.Tracee;
import io.tracee.contextlogger.ImplicitContext;
import io.tracee.contextlogger.data.subdata.NameStringValuePair;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.List;

/**
 * Test class for  {@link io.tracee.contextlogger.data.subdata.tracee.TraceeContextProvider}.
 * Created by Tobias Gindler, holisticon AG on 31.03.14.
 */
public class TraceeContextProviderTest {

    @Test
    public void should_return_implicit_context() {
        ImplicitContext implicitContext = new TraceeContextProvider().getImplicitContext();

        MatcherAssert.assertThat(implicitContext, Matchers.notNullValue());
        MatcherAssert.assertThat(implicitContext, Matchers.equalTo(ImplicitContext.TRACEE));
    }

    @Test
    public void should_return_null_for_empty_mdc() {

        TraceeContextProvider traceeContextProvider = new TraceeContextProvider();

        List<NameStringValuePair> result = traceeContextProvider.getNameValuePairs();

        MatcherAssert.assertThat(result, Matchers.nullValue());

    }

    @Test
    public void should_return_tracee_properties() {

        Tracee.getBackend().put("ID", "ID_value");

        TraceeContextProvider traceeContextProvider = new TraceeContextProvider();

        List<NameStringValuePair> result = traceeContextProvider.getNameValuePairs();

        MatcherAssert.assertThat(result, Matchers.notNullValue());
        MatcherAssert.assertThat(result.size(), Matchers.equalTo(1));
        MatcherAssert.assertThat(result.get(0).getName(), Matchers.equalTo("ID"));
        MatcherAssert.assertThat(result.get(0).getValue(), Matchers.equalTo("ID_value"));
    }

}
