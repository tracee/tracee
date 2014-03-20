package de.holisticon.util.tracee.contextlogger.builder.gson;

import de.holisticon.util.tracee.contextlogger.testdata.AnnotationTestClass;
import de.holisticon.util.tracee.contextlogger.utility.TraceeContextLogAnnotationUtilities;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;


import java.util.Collections;
import java.util.List;

import static java.util.Collections.sort;

/**
 * Created by Tobias Gindler, holistcon AG on 14.03.14.
 */
public class MethodPairComparatorTest {

    public static final AnnotationTestClass TEST_INSTANCE_WITH_ANNOTATION = new AnnotationTestClass();

    @Test
    public void should_sort_method_annotation_pairs_correctly() {

        // first get method pairs fromAnnotationTestClass
        List<MethodAnnotationPair> list = TraceeContextLogAnnotationUtilities.getAnnotatedMethodsOfInstance( TEST_INSTANCE_WITH_ANNOTATION);


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
