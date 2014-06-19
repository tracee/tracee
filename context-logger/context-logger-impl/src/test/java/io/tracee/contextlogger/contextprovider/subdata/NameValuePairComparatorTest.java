package io.tracee.contextlogger.contextprovider.subdata;

import io.tracee.contextlogger.contextprovider.utility.NameStringValuePair;
import io.tracee.contextlogger.contextprovider.utility.NameValuePairComparator;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;

/**
 * Test class for {@link io.tracee.contextlogger.contextprovider.utility.NameValuePair}
 * Created by Tobias Gindler, holisticon AG on 16.03.14.
 */
public class NameValuePairComparatorTest {

    public static final NameStringValuePair NAME_VALUE_PAIR_1 = new NameStringValuePair("C", "C");
    public static final NameStringValuePair NAME_VALUE_PAIR_2 = new NameStringValuePair("A", "A");
    public static final NameStringValuePair NAME_VALUE_PAIR_3 = new NameStringValuePair("B", "B");

    public static final NameStringValuePair BROKEN_NAME_VALUE_PAIR = new NameStringValuePair(null, "B");
    public static final NameStringValuePair ANOTHER_BROKEN_NAME_VALUE_PAIR = new NameStringValuePair(null, "C");

    @Test
    public void should_compare_null_valued_with_null_valued_instance_correctly() {

        int result = new NameValuePairComparator().compare(null, null);
        assertThat(result, equalTo(0));

    }

    @Test
    public void should_compare_not_null_valued_with_null_valued_instance_correctly() {

        int result = new NameValuePairComparator().compare(NAME_VALUE_PAIR_1, null);
        assertThat(result, equalTo(-1));

    }

    @Test
    public void should_compare_null_valued_with_not_null_valued_instance_correctly() {

        int result = new NameValuePairComparator().compare(null, NAME_VALUE_PAIR_1);
        assertThat(result, equalTo(1));

    }

    @Test
    public void should_compare_two_not_null_valued_instance_with_null_valued_names_correctly() {

        int result = new NameValuePairComparator().compare(BROKEN_NAME_VALUE_PAIR, ANOTHER_BROKEN_NAME_VALUE_PAIR);
        assertThat(result, equalTo(0));
    }

    @Test
    public void should_compare_two_not_null_valued_instance_with_null_and_not_null_valued_names_correctly() {

        int result = new NameValuePairComparator().compare(BROKEN_NAME_VALUE_PAIR, NAME_VALUE_PAIR_1);
        assertThat(result, lessThan(0));
    }

    @Test
    public void should_compare_two_not_null_valued_instance_with_not_null_and_null_valued_names_correctly() {

        int result = new NameValuePairComparator().compare(NAME_VALUE_PAIR_1, BROKEN_NAME_VALUE_PAIR);
        assertThat(result, greaterThan(0));
    }

    @Test
    public void should_compare_two_not_null_valued_instance_with_same_not_null_valued_names_correctly() {

        int result = new NameValuePairComparator().compare(NAME_VALUE_PAIR_1, NAME_VALUE_PAIR_1);
        assertThat(result, equalTo(0));
    }


    @Test
    public void should_sort_name_value_pairs_correctly() {

        final List<NameStringValuePair> list = new ArrayList<NameStringValuePair>();
        list.add(NAME_VALUE_PAIR_1);
        list.add(NAME_VALUE_PAIR_2);
        list.add(NAME_VALUE_PAIR_3);

        Collections.sort(list, new NameValuePairComparator());

        assertThat(list.get(0), equalTo(NAME_VALUE_PAIR_2));
        assertThat(list.get(1), equalTo(NAME_VALUE_PAIR_3));
        assertThat(list.get(2), equalTo(NAME_VALUE_PAIR_1));
    }

    @Test
    public void should_sort_name_value_pairs_with_one_null_valued_name_correctly() {

        final List<NameStringValuePair> list = new ArrayList<NameStringValuePair>();
        list.add(NAME_VALUE_PAIR_1);
        list.add(NAME_VALUE_PAIR_2);
        list.add(BROKEN_NAME_VALUE_PAIR);
        list.add(NAME_VALUE_PAIR_3);

        Collections.sort(list, new NameValuePairComparator());

        assertThat(list.get(1), equalTo(NAME_VALUE_PAIR_2));
        assertThat(list.get(2), equalTo(NAME_VALUE_PAIR_3));
        assertThat(list.get(3), equalTo(NAME_VALUE_PAIR_1));
        assertThat(list.get(0), equalTo(BROKEN_NAME_VALUE_PAIR));
    }
}
