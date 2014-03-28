package de.holisticon.util.tracee.contextlogger.utility;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for {@link de.holisticon.util.tracee.contextlogger.utility.ListUtilities}.
 * Created by Tobias Gindler, holisticon AG on 21.03.14.
 */
public class ListUtilitiesTest {

    @Test
    public void isListOfType_should_return_true_for_list_of_a_matching_type() throws NoSuchMethodException {

        List<Integer> list = new ArrayList<Integer>();
        list.add(5);
        list.add(6);
        list.add(9);

        boolean isListOfTypeInteger = ListUtilities.isListOfType(list, Integer.class);

        MatcherAssert.assertThat(isListOfTypeInteger, Matchers.equalTo(true));
    }

    @Test
    public void isListOfType_should_return_false_for_list_with_not_matching_generic_type() throws NoSuchMethodException {

        List<Double> list = new ArrayList<Double>();
        list.add(5.0);
        list.add(6.0);
        list.add(9.0);

        boolean isListOfTypeInteger = ListUtilities.isListOfType(list, Integer.class);

        MatcherAssert.assertThat(isListOfTypeInteger, Matchers.equalTo(false));
    }

    @Test
    public void isListOfType_should_return_false_for_no_list() throws NoSuchMethodException {


        boolean isListOfTypeInteger = ListUtilities.isListOfType("abc", Integer.class);

        MatcherAssert.assertThat(isListOfTypeInteger, Matchers.equalTo(false));
    }

    @Test
    public void isListOfType_should_return_true_for_non_generic_list_with_matching_element_types() throws NoSuchMethodException {

        List list = new ArrayList();
        list.add(5);
        list.add(6);
        list.add(9);

        boolean isListOfTypeInteger = ListUtilities.isListOfType(list, Integer.class);

        MatcherAssert.assertThat(isListOfTypeInteger, Matchers.equalTo(true));
    }

    @Test
    public void isListOfType_should_return_true_for_non_generic_list_with_at_least_one_none_matching_element_types() throws NoSuchMethodException {

        List list = new ArrayList();
        list.add(5);
        list.add("sadas");
        list.add(9);

        boolean isListOfTypeInteger = ListUtilities.isListOfType(list, Integer.class);

        MatcherAssert.assertThat(isListOfTypeInteger, Matchers.equalTo(false));
    }

    @Test
    public void getListOfNameValuePair_should_return_false_for_list_with_not_matching_generic_type() throws NoSuchMethodException {

        List<Double> list = new ArrayList<Double>();
        list.add(5.0);
        list.add(6.0);
        list.add(9.0);

        List<Integer> isListOfTypeInteger = ListUtilities.getListOfType(list, Integer.class);

        MatcherAssert.assertThat(isListOfTypeInteger, Matchers.nullValue());
    }

    @Test
    public void getListOfNameValuePair_should_return_false_for_no_list() throws NoSuchMethodException {

        List<Integer> isListOfTypeInteger = ListUtilities.getListOfType("abc", Integer.class);

        MatcherAssert.assertThat(isListOfTypeInteger, Matchers.nullValue());
    }

    @Test
    public void getListOfNameValuePair_should_return_true_for_non_generic_list_with_matching_element_types() throws NoSuchMethodException {

        List list = new ArrayList();
        list.add(5);
        list.add(6);
        list.add(9);

        List<Integer> isListOfTypeInteger = ListUtilities.getListOfType(list, Integer.class);

        MatcherAssert.assertThat(isListOfTypeInteger, Matchers.notNullValue());
    }

    @Test
    public void getListOfNameValuePair_should_return_true_for_non_generic_list_with_at_least_one_none_matching_element_types() throws NoSuchMethodException {

        List list = new ArrayList();
        list.add(5);
        list.add("sadas");
        list.add(9);

        List<Integer> isListOfTypeInteger = ListUtilities.getListOfType(list, Integer.class);

        MatcherAssert.assertThat(isListOfTypeInteger, Matchers.nullValue());
    }

}
