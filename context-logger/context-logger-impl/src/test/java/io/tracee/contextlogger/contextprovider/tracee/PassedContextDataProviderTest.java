package io.tracee.contextlogger.contextprovider.tracee;

import io.tracee.contextlogger.contextprovider.utility.NameObjectValuePair;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

/**
 * Test class for {@link PassedDataContextProvider}.
 * Created by Tobias Gindler, holisticon AG on 31.03.14.
 */
public class PassedContextDataProviderTest {

    @Test
    public void should_return_wrapped_type() {
        final PassedDataContextProvider passedContextProvider = new PassedDataContextProvider(null);

		assertThat(passedContextProvider.getWrappedType(), equalTo(Object[].class));
    }


    @Test
    public void should_return_null_for_null_valued_wrapped_object_array() {

        final PassedDataContextProvider passedContextProvider = new PassedDataContextProvider(null);

		assertThat(passedContextProvider.getContextData(), nullValue());
    }

    @Test
    public void should_return_null_for_zero_length_wrapped_object_array() {

        final PassedDataContextProvider passedContextProvider = new PassedDataContextProvider(new Object[0]);

		assertThat(passedContextProvider.getContextData(), nullValue());
    }

    @Test
    public void should_return_list_of_name_object_value_pairs() {

        final Object[] OBJECTS = {"ABC"};

        final PassedDataContextProvider passedContextProvider = new PassedDataContextProvider(OBJECTS);

        final List<NameObjectValuePair> result = passedContextProvider.getContextData();

        assertThat(result, Matchers.notNullValue());
        assertThat(result.size(), equalTo(1));
        assertThat(result.get(0).getName(), equalTo(String.class.getCanonicalName()));
        assertThat(result.get(0).getValue(), equalTo(OBJECTS[0]));
    }
}
