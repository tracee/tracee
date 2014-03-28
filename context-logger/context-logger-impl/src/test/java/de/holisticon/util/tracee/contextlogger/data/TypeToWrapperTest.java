package de.holisticon.util.tracee.contextlogger.data;

import de.holisticon.util.tracee.contextlogger.data.TypeToWrapper;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Test class for {@link de.holisticon.util.tracee.contextlogger.data.TypeToWrapper}.
 * Created by Tobias Gindler, holisticon AG on 20.03.14.
 */
public class TypeToWrapperTest {

    @Test
    public void getAvailableWrappers_should_load_all_available_wrappers () {

        List<TypeToWrapper> result = TypeToWrapper.getAvailableWrappers();

        Assert.assertThat(result, Matchers.notNullValue());
        Assert.assertThat(result.size() > 0 , Matchers.equalTo(true));

    }

}
