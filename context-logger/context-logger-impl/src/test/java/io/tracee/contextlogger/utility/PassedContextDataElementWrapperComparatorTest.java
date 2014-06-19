package io.tracee.contextlogger.utility;

import io.tracee.contextlogger.data.subdata.NameObjectValuePair;
import org.junit.Test;
import org.mockito.Mock;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.powermock.api.mockito.PowerMockito.mock;

/**
 * @author Simon Sprünker (Holisticon AG)
 */
public class PassedContextDataElementWrapperComparatorTest {

    private static final PassedContextDataElementWrapperComparator comparator = new PassedContextDataElementWrapperComparator();

    private static final PassedContextDataElementWrapper passedContextDataElementWrapper1 = new PassedContextDataElementWrapper(new NameObjectValuePair("name1", "value1"));

    @Test
    public void compare_equals_0_if_both_arguments_are_null() {
        assertThat(comparator.compare(null, null), equalTo(0));
    }

    @Test
    public void compare_equals_minus_1_if_first_argument_is_null() {
        assertThat(comparator.compare(null, passedContextDataElementWrapper1), equalTo(-1));
    }

    @Test
    public void compare_equals_1_if_second_argument_is_null() {
        assertThat(comparator.compare(passedContextDataElementWrapper1, null), equalTo(1));
    }


}
