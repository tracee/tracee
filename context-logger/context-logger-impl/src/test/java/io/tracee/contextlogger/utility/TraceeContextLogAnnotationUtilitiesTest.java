package io.tracee.contextlogger.utility;

import io.tracee.contextlogger.api.TraceeContextLogProvider;
import io.tracee.contextlogger.builder.gson.MethodAnnotationPair;
import io.tracee.contextlogger.testdata.AnnotationTestClass;
import io.tracee.contextlogger.testdata.TestClassWithMethods;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Test class for {@link io.tracee.contextlogger.utility.TraceeContextLogAnnotationUtilities}.
 * Created by Tobias Gindler, holisticon AG on 14.03.14.
 */
public class TraceeContextLogAnnotationUtilitiesTest {

    public static final AnnotationTestClass TEST_INSTANCE_WITH_ANNOTATION = new AnnotationTestClass();
    public static final TraceeContextLogAnnotationUtilitiesTest TEST_INSTANCE_WITHOUT_ANNOTATION = new TraceeContextLogAnnotationUtilitiesTest();

    public static final String TEST_METHOD_WITH_NO_PARAMETERS = "methodWithNoParameters";
    public static final String TEST_METHOD_WITH_PARAMETERS = "methodWithParameters";
    public static final String TEST_METHOD_WITH_VOID_RETURN_TYPE = "methodWithVoidReturnType";
    public static final String TEST_METHOD_WITH_NON_VOID_RETURN_TYPE = "methodWithNonVoidReturnType";
    public static final String TEST_METHOD_MARKED_WITH_FLATTEN = "flattenTest";

    @Test
    public void getAnnotationFromType_should_return_annotation() {

        TraceeContextLogProvider annotation = TraceeContextLogAnnotationUtilities.getAnnotationFromType(TEST_INSTANCE_WITH_ANNOTATION);
        MatcherAssert.assertThat(annotation, Matchers.notNullValue());

    }

    @Test
    public void getAnnotationFromType_should_return_null_for_passed_null_value() {

        TraceeContextLogProvider annotation = TraceeContextLogAnnotationUtilities.getAnnotationFromType(null);
        MatcherAssert.assertThat(annotation, Matchers.nullValue());

    }

    @Test
    public void getAnnotationFromType_should_return_null_for_not_annotated_class() {

        TraceeContextLogProvider annotation = TraceeContextLogAnnotationUtilities.getAnnotationFromType(TEST_INSTANCE_WITHOUT_ANNOTATION);
        MatcherAssert.assertThat(annotation, Matchers.nullValue());

    }

    @Test
    public void checkMethodHasNoParameters_should_return_true_for_null_value() throws NoSuchMethodException {


        boolean result = TraceeContextLogAnnotationUtilities.checkMethodHasNoParameters(null);

        MatcherAssert.assertThat(result, Matchers.equalTo(true));

    }

    @Test
    public void checkMethodHasNoParameters_should_return_true_for_method_with_no_parameters() throws NoSuchMethodException {

        Method methodWithNoParameters = TestClassWithMethods.class.getMethod(TEST_METHOD_WITH_NO_PARAMETERS, null);

        boolean result = TraceeContextLogAnnotationUtilities.checkMethodHasNoParameters(methodWithNoParameters);

        MatcherAssert.assertThat(result, Matchers.equalTo(true));

    }

    @Test
    public void checkMethodHasNoParameters_should_return_false_for_method_with_one_or_more_parameters() throws NoSuchMethodException {

        Method methodWithParameters = TestClassWithMethods.class.getMethod(TEST_METHOD_WITH_PARAMETERS, int.class, int.class);

        boolean result = TraceeContextLogAnnotationUtilities.checkMethodHasNoParameters(methodWithParameters);

        MatcherAssert.assertThat(result, Matchers.equalTo(false));

    }

    @Test
    public void getAnnotatedMethodsOfInstance_should_return_all_annotated_methods() {

        List<MethodAnnotationPair> methods = TraceeContextLogAnnotationUtilities.getAnnotatedMethodsOfInstance(TEST_INSTANCE_WITH_ANNOTATION);

        MatcherAssert.assertThat(methods, Matchers.notNullValue());
        MatcherAssert.assertThat(methods.size(), Matchers.equalTo(4));

    }

    @Test
    public void checkMethodHasNonVoidReturnType_should_return_false_for_null_value() {

        // Method methodWithNoParameters = TestClassWithMethods.class.getMethod(TEST_METHOD_WITH_NO_PARAMETERS, null);

        boolean result = TraceeContextLogAnnotationUtilities.checkMethodHasNonVoidReturnType(null);
        MatcherAssert.assertThat(result, Matchers.equalTo(false));
    }

    @Test
    public void checkMethodHasNonVoidReturnType_should_return_false_for_method_with_void_return_type() throws NoSuchMethodException {

        Method methodWithVoidReturnType = TestClassWithMethods.class.getMethod(TEST_METHOD_WITH_VOID_RETURN_TYPE, null);

        boolean result = TraceeContextLogAnnotationUtilities.checkMethodHasNonVoidReturnType(methodWithVoidReturnType);
        MatcherAssert.assertThat(result, Matchers.equalTo(false));
    }

    @Test
    public void checkMethodHasNonVoidReturnType_should_return_true_for_method_with_non_void_return_type() throws NoSuchMethodException {

        Method methodWithNoParameters = TestClassWithMethods.class.getMethod(TEST_METHOD_WITH_NON_VOID_RETURN_TYPE, null);

        boolean result = TraceeContextLogAnnotationUtilities.checkMethodHasNonVoidReturnType(methodWithNoParameters);
        MatcherAssert.assertThat(result, Matchers.equalTo(true));
    }

    @Test
    public void isFlatable_should_return_true_for_as_flatable_marked_method() throws NoSuchMethodException {

        Method methodMarkedAsFlatable = TestClassWithMethods.class.getMethod(TEST_METHOD_MARKED_WITH_FLATTEN, null);

        boolean result = TraceeContextLogAnnotationUtilities.isFlatable(methodMarkedAsFlatable);
        MatcherAssert.assertThat(result, Matchers.equalTo(true));

    }

    @Test
    public void isFlatable_should_return_false_for_not_flatable_marked_method() throws NoSuchMethodException {

        Method methodWithNoParameters = TestClassWithMethods.class.getMethod(TEST_METHOD_WITH_NON_VOID_RETURN_TYPE, null);

        boolean result = TraceeContextLogAnnotationUtilities.isFlatable(methodWithNoParameters);
        MatcherAssert.assertThat(result, Matchers.equalTo(false));

    }

    @Test
    public void checkIsPublic_should_return_false_for_protected_method() throws NoSuchMethodException {

        Method method = TestClassWithMethods.class.getDeclaredMethod("protectedMethod", null);
        boolean result = TraceeContextLogAnnotationUtilities.checkIsPublic(method);
        MatcherAssert.assertThat(result, Matchers.equalTo(false));

    }

    @Test
    public void checkIsPublic_should_return_false_for_private_method() throws NoSuchMethodException {
        Method method = TestClassWithMethods.class.getDeclaredMethod("privateMethod", null);
        boolean result = TraceeContextLogAnnotationUtilities.checkIsPublic(method);
        MatcherAssert.assertThat(result, Matchers.equalTo(false));

    }

    @Test
    public void checkIsPublic_should_return_false_for_passed_null_value() throws NoSuchMethodException {
        boolean result = TraceeContextLogAnnotationUtilities.checkIsPublic(null);
        MatcherAssert.assertThat(result, Matchers.equalTo(false));
    }

    @Test
    public void checkIsPublic_should_return_false_for_package_private_method() throws NoSuchMethodException {
        Method method = TestClassWithMethods.class.getDeclaredMethod("packagePrivateMethod", null);
        boolean result = TraceeContextLogAnnotationUtilities.checkIsPublic(method);
        MatcherAssert.assertThat(result, Matchers.equalTo(false));
    }

    @Test
    public void checkIsPublic_should_return_true_for_public_method() throws NoSuchMethodException {
        Method method = TestClassWithMethods.class.getDeclaredMethod("flattenTest", null);
        boolean result = TraceeContextLogAnnotationUtilities.checkIsPublic(method);
        MatcherAssert.assertThat(result, Matchers.equalTo(true));
    }

}
