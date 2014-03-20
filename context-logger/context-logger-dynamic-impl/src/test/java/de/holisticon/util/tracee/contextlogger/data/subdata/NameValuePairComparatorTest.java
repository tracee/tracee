package de.holisticon.util.tracee.contextlogger.data.subdata;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Tobias Gindler, holisticon AG on 16.03.14.
 */
public class NameValuePairComparatorTest {

    public static final NameValuePair NAME_VALUE_PAIR_1 = new NameValuePair("C", "C");
    public static final NameValuePair NAME_VALUE_PAIR_2 = new NameValuePair("A", "A");
    public static final NameValuePair NAME_VALUE_PAIR_3 = new NameValuePair("B", "B");

    public static final NameValuePair BROKEN_NAME_VALUE_PAIR = new NameValuePair(null, "B");


    @Test
    public void should_sort_name_value_pairs_correctly () {

        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(NAME_VALUE_PAIR_1);
        list.add(NAME_VALUE_PAIR_2);
        list.add(NAME_VALUE_PAIR_3);

        Collections.sort(list,new NameValuePairComparator());

        MatcherAssert.assertThat(list.get(0), Matchers.equalTo(NAME_VALUE_PAIR_2));
        MatcherAssert.assertThat(list.get(1), Matchers.equalTo(NAME_VALUE_PAIR_3));
        MatcherAssert.assertThat(list.get(2), Matchers.equalTo(NAME_VALUE_PAIR_1));

    }

    @Test
    public void should_sort_name_value_pairs_with_one_null_valued_name_correctly () {

        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(NAME_VALUE_PAIR_1);
        list.add(NAME_VALUE_PAIR_2);
        list.add(BROKEN_NAME_VALUE_PAIR);
        list.add(NAME_VALUE_PAIR_3);

        Collections.sort(list,new NameValuePairComparator());

        MatcherAssert.assertThat(list.get(0), Matchers.equalTo(NAME_VALUE_PAIR_2));
        MatcherAssert.assertThat(list.get(1), Matchers.equalTo(NAME_VALUE_PAIR_3));
        MatcherAssert.assertThat(list.get(2), Matchers.equalTo(NAME_VALUE_PAIR_1));
        MatcherAssert.assertThat(list.get(3), Matchers.equalTo(BROKEN_NAME_VALUE_PAIR));

    }



}
