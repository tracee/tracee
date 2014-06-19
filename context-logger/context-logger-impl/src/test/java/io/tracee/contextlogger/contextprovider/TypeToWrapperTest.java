package io.tracee.contextlogger.contextprovider;

import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Test class for {@link TypeToWrapper}.
 * Created by Tobias Gindler, holisticon AG on 20.03.14.
 */
public class TypeToWrapperTest {

    @Test
    public void getAvailableWrappers_should_load_all_available_wrappers () {

        List<TypeToWrapper> result = TypeToWrapper.getAvailableWrappers();

        assertThat(result, notNullValue());
        assertThat(result.size(), greaterThan(0));
    }
}
