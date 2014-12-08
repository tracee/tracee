package io.tracee.contextlogger.impl.gson;

import io.tracee.contextlogger.api.TraceeContextProviderMethod;
import io.tracee.contextlogger.testdata.AnnotationTestClass;
import io.tracee.contextlogger.utility.TraceeContextLogAnnotationUtilities;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

/**
 * Test class for {@link MethodAnnotationPair}.
 * Created by Tobias Gindler, holistcon AG on 14.03.14.
 */
public class MethodPairComparatorTest {

    public static final AnnotationTestClass TEST_INSTANCE_WITH_ANNOTATION = new AnnotationTestClass();

    @Test
    public void should_compare_two_null_valued_instances_correctly() {
        int result = new MethodAnnotationPairComparator().compare(null, null);
        MatcherAssert.assertThat(result, Matchers.equalTo(0));
    }

    @Test
    public void should_compare_null_valued_with_not_null_valued_instance_correctly() {
        int result = new MethodAnnotationPairComparator().compare(null, new MethodAnnotationPair(null, null, null));
        MatcherAssert.assertThat(result, Matchers.equalTo(-1));
    }

    @Test
    public void should_compare_not_null_valued_with_null_valued_instance_correctly() {
        int result = new MethodAnnotationPairComparator().compare(new MethodAnnotationPair(null, null, null), null);
        MatcherAssert.assertThat(result, Matchers.equalTo(1));
    }

    @Test
    public void should_compare_two_not_null_valued_instances_with_first_order_is_greater_correctly() {
        MethodAnnotationPair methodAnnotationPair1 = Mockito.mock(MethodAnnotationPair.class);
        MethodAnnotationPair methodAnnotationPair2 = Mockito.mock(MethodAnnotationPair.class);
        TraceeContextProviderMethod traceeContextProviderMethod1 = Mockito.mock(TraceeContextProviderMethod.class);
        TraceeContextProviderMethod traceeContextProviderMethod2 = Mockito.mock(TraceeContextProviderMethod.class);
        Mockito.when(methodAnnotationPair1.getAnnotation()).thenReturn(traceeContextProviderMethod1);
        Mockito.when(methodAnnotationPair2.getAnnotation()).thenReturn(traceeContextProviderMethod2);
        Mockito.when(traceeContextProviderMethod1.order()).thenReturn(2);
        Mockito.when(traceeContextProviderMethod2.order()).thenReturn(1);
        Mockito.when(traceeContextProviderMethod1.displayName()).thenReturn("A");
        Mockito.when(traceeContextProviderMethod2.displayName()).thenReturn("B");

        int result = new MethodAnnotationPairComparator().compare(methodAnnotationPair1, methodAnnotationPair2);
        MatcherAssert.assertThat(result, Matchers.equalTo(1));
    }

    @Test
    public void should_compare_two_not_null_valued_instances_with_first_order_is_less_correctly() {
        MethodAnnotationPair methodAnnotationPair1 = Mockito.mock(MethodAnnotationPair.class);
        MethodAnnotationPair methodAnnotationPair2 = Mockito.mock(MethodAnnotationPair.class);
        TraceeContextProviderMethod traceeContextProviderMethod1 = Mockito.mock(TraceeContextProviderMethod.class);
        TraceeContextProviderMethod traceeContextProviderMethod2 = Mockito.mock(TraceeContextProviderMethod.class);
        Mockito.when(methodAnnotationPair1.getAnnotation()).thenReturn(traceeContextProviderMethod1);
        Mockito.when(methodAnnotationPair2.getAnnotation()).thenReturn(traceeContextProviderMethod2);
        Mockito.when(traceeContextProviderMethod1.order()).thenReturn(1);
        Mockito.when(traceeContextProviderMethod2.order()).thenReturn(2);
        Mockito.when(traceeContextProviderMethod1.displayName()).thenReturn("A");
        Mockito.when(traceeContextProviderMethod2.displayName()).thenReturn("B");

        int result = new MethodAnnotationPairComparator().compare(methodAnnotationPair1, methodAnnotationPair2);
        MatcherAssert.assertThat(result, Matchers.equalTo(-1));
    }

    @Test
    public void should_compare_two_not_null_valued_instances_with_equal_order_and_first_displayname_is_less_correctly() {
        MethodAnnotationPair methodAnnotationPair1 = Mockito.mock(MethodAnnotationPair.class);
        MethodAnnotationPair methodAnnotationPair2 = Mockito.mock(MethodAnnotationPair.class);
        TraceeContextProviderMethod traceeContextProviderMethod1 = Mockito.mock(TraceeContextProviderMethod.class);
        TraceeContextProviderMethod traceeContextProviderMethod2 = Mockito.mock(TraceeContextProviderMethod.class);
        Mockito.when(methodAnnotationPair1.getAnnotation()).thenReturn(traceeContextProviderMethod1);
        Mockito.when(methodAnnotationPair2.getAnnotation()).thenReturn(traceeContextProviderMethod2);
        Mockito.when(traceeContextProviderMethod1.order()).thenReturn(2);
        Mockito.when(traceeContextProviderMethod2.order()).thenReturn(2);
        Mockito.when(traceeContextProviderMethod1.displayName()).thenReturn("A");
        Mockito.when(traceeContextProviderMethod2.displayName()).thenReturn("B");

        int result = new MethodAnnotationPairComparator().compare(methodAnnotationPair1, methodAnnotationPair2);
        MatcherAssert.assertThat(result, Matchers.equalTo(-1));
    }

    @Test
    public void should_compare_two_not_null_valued_instances_with_equal_order_and_first_displayname_is_greater_correctly() {
        MethodAnnotationPair methodAnnotationPair1 = Mockito.mock(MethodAnnotationPair.class);
        MethodAnnotationPair methodAnnotationPair2 = Mockito.mock(MethodAnnotationPair.class);
        TraceeContextProviderMethod traceeContextProviderMethod1 = Mockito.mock(TraceeContextProviderMethod.class);
        TraceeContextProviderMethod traceeContextProviderMethod2 = Mockito.mock(TraceeContextProviderMethod.class);
        Mockito.when(methodAnnotationPair1.getAnnotation()).thenReturn(traceeContextProviderMethod1);
        Mockito.when(methodAnnotationPair2.getAnnotation()).thenReturn(traceeContextProviderMethod2);
        Mockito.when(traceeContextProviderMethod1.order()).thenReturn(2);
        Mockito.when(traceeContextProviderMethod2.order()).thenReturn(2);
        Mockito.when(traceeContextProviderMethod1.displayName()).thenReturn("C");
        Mockito.when(traceeContextProviderMethod2.displayName()).thenReturn("B");

        int result = new MethodAnnotationPairComparator().compare(methodAnnotationPair1, methodAnnotationPair2);
        MatcherAssert.assertThat(result, Matchers.equalTo(1));
    }

    @Test
    public void should_compare_two_not_null_valued_instances_with_equal_order_and_displayname_correctly() {
        MethodAnnotationPair methodAnnotationPair1 = Mockito.mock(MethodAnnotationPair.class);
        MethodAnnotationPair methodAnnotationPair2 = Mockito.mock(MethodAnnotationPair.class);
        TraceeContextProviderMethod traceeContextProviderMethod1 = Mockito.mock(TraceeContextProviderMethod.class);
        TraceeContextProviderMethod traceeContextProviderMethod2 = Mockito.mock(TraceeContextProviderMethod.class);
        Mockito.when(methodAnnotationPair1.getAnnotation()).thenReturn(traceeContextProviderMethod1);
        Mockito.when(methodAnnotationPair2.getAnnotation()).thenReturn(traceeContextProviderMethod2);
        Mockito.when(traceeContextProviderMethod1.order()).thenReturn(2);
        Mockito.when(traceeContextProviderMethod2.order()).thenReturn(2);
        Mockito.when(traceeContextProviderMethod1.displayName()).thenReturn("C");
        Mockito.when(traceeContextProviderMethod2.displayName()).thenReturn("C");

        int result = new MethodAnnotationPairComparator().compare(methodAnnotationPair1, methodAnnotationPair2);
        MatcherAssert.assertThat(result, Matchers.equalTo(0));
    }


    @Test
    public void should_sort_method_annotation_pairs_correctly() {

        // first get method pairs fromAnnotationTestClass
        List<MethodAnnotationPair> list = TraceeContextLogAnnotationUtilities.getAnnotatedMethodsOfInstance(TEST_INSTANCE_WITH_ANNOTATION);


        MatcherAssert.assertThat(list, Matchers.notNullValue());
        MatcherAssert.assertThat(list.size(), Matchers.equalTo(4));

        Collections.sort(list, new MethodAnnotationPairComparator());

        // now check if order is correct
        MatcherAssert.assertThat(list.get(0).getAnnotation().displayName(), Matchers.equalTo("B"));
        MatcherAssert.assertThat(list.get(1).getAnnotation().displayName(), Matchers.equalTo("C"));
        MatcherAssert.assertThat(list.get(2).getAnnotation().displayName(), Matchers.equalTo("E"));
        MatcherAssert.assertThat(list.get(3).getAnnotation().displayName(), Matchers.equalTo("A"));

    }

}
