package io.tracee.contextlogger.data.subdata.tracee;

import io.tracee.contextlogger.data.subdata.NameObjectValuePair;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.List;

/**
 * Test class for {@link PassedDataContextProvider}.
 * Created by Tobias Gindler, holisticon AG on 31.03.14.
 */
public class PassedContextDataProviderTest {

    @Test
    public void should_return_wrapped_type() {

        PassedDataContextProvider passedContextProvider = new PassedDataContextProvider(null);

        Class result = passedContextProvider.getWrappedType();

        MatcherAssert.assertThat(result.equals(Object[].class), Matchers.equalTo(true));

    }


    @Test
    public void should_return_null_for_null_valued_wrapped_object_array() {

        PassedDataContextProvider passedContextProvider = new PassedDataContextProvider(null);

        List<NameObjectValuePair> result = passedContextProvider.getContextData();

        MatcherAssert.assertThat(result, Matchers.nullValue());

    }

    @Test
    public void should_return_null_for_zero_length_wrapped_object_array() {

        PassedDataContextProvider passedContextProvider = new PassedDataContextProvider(new Object[0]);

        List<NameObjectValuePair> result = passedContextProvider.getContextData();

        MatcherAssert.assertThat(result, Matchers.nullValue());

    }

    @Test
    public void should_return_list_of_name_object_value_pairs() {

        Object[] OBJECTS = {"ABC"};

        PassedDataContextProvider passedContextProvider = new PassedDataContextProvider(OBJECTS);

        List<NameObjectValuePair> result = passedContextProvider.getContextData();

        MatcherAssert.assertThat(result, Matchers.notNullValue());
        MatcherAssert.assertThat(result.size(), Matchers.equalTo(1));
        MatcherAssert.assertThat(result.get(0).getName(), Matchers.equalTo(String.class.getCanonicalName()));
        MatcherAssert.assertThat(result.get(0).getValue(), Matchers.equalTo(OBJECTS[0]));
    }

}
