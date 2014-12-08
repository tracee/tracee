package io.tracee.contextlogger.utility;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.lang.reflect.Method;

/**
 * Unit test class for {@link io.tracee.contextlogger.utility.GetterUtilities}.
 */
public class GetterUtilitiesTest {

    public static class TestClass {

        private int field1 = 1;
        private int field2 = 2;
        private int field3 = 3;

        private int field4 = 4;

        public int getField1() {
            return field1;
        }

        public int isField2() {
            return field2;
        }

        public int hasField3() {
            return field3;
        }

        private int nonGetterField() {
            return 4;
        }

    }

    @Test
    public void isGetterMethod_should_return_true_for_get_getter_method_with_passed_method() throws NoSuchMethodException {

        Method method = TestClass.class.getDeclaredMethod("getField1", null);

        boolean result = GetterUtilities.isGetterMethod(method);
        MatcherAssert.assertThat(result, Matchers.is(true));


    }

    @Test
    public void isGetterMethod_should_call_isgetter_with_method_name_for_non_null_valued_method() throws NoSuchMethodException {

        final Method method = TestClass.class.getDeclaredMethod("hasField3", null);

        final boolean result = GetterUtilities.isGetterMethod(method);
        MatcherAssert.assertThat(result, Matchers.is(true));


    }

    @Test
    public void isGetterMethod_should_return_true_for_is_getter_method_with_passed_method() throws NoSuchMethodException {

        final Method method = TestClass.class.getDeclaredMethod("isField2", null);

        final boolean result = GetterUtilities.isGetterMethod(method);
        MatcherAssert.assertThat(result, Matchers.is(true));


    }


    @Test
    public void isGetterMethod_should_return_true_for_non_getter_method_with_passed_method() throws NoSuchMethodException {

        final Method method = TestClass.class.getDeclaredMethod("nonGetterField", null);

        final boolean result = GetterUtilities.isGetterMethod(method);
        MatcherAssert.assertThat(result, Matchers.is(false));


    }

    @Test
    public void isGetterMethod_should_return_true_for_null_valued_passed_method() throws NoSuchMethodException {

        final Method method = null;

        final boolean result = GetterUtilities.isGetterMethod(method);
        MatcherAssert.assertThat(result, Matchers.is(false));


    }

    @Test
    public void isGetterMethod_should_return_true_for_get_getter_methodname() throws NoSuchMethodException {


        final boolean result = GetterUtilities.isGetterMethod("getField1");
        MatcherAssert.assertThat(result, Matchers.is(true));


    }

    @Test
    public void isGetterMethod_should_return_true_for_has_getter_methodname() {

        final boolean result = GetterUtilities.isGetterMethod("hasField3");
        MatcherAssert.assertThat(result, Matchers.is(true));


    }

    @Test
    public void isGetterMethod_should_return_true_for_is_getter_methodname() {


        final boolean result = GetterUtilities.isGetterMethod("isField2");
        MatcherAssert.assertThat(result, Matchers.is(true));


    }


    @Test
    public void isGetterMethod_should_return_true_for_non_getter_methodname() throws NoSuchMethodException {

        boolean result = GetterUtilities.isGetterMethod("nonGetterField");
        MatcherAssert.assertThat(result, Matchers.is(false));


    }

    @Test
    public void isGetterMethod_should_return_true_for_null_valued_methodname() throws NoSuchMethodException {

        final Method method = null;

        final boolean result = GetterUtilities.isGetterMethod(method);
        MatcherAssert.assertThat(result, Matchers.is(false));


    }


    @Test
    public void getFieldName_should_return_true_for_get_getter_method_with_passed_method() throws NoSuchMethodException {

        final Method method = TestClass.class.getDeclaredMethod("getField1", null);

        final String result = GetterUtilities.getFieldName(method);
        MatcherAssert.assertThat(result, Matchers.is("field1"));


    }

    @Test
    public void getFieldName_should_call_isgetter_with_method_name_for_non_null_valued_method() throws NoSuchMethodException {

        final Method method = TestClass.class.getDeclaredMethod("hasField3", null);

        final String result = GetterUtilities.getFieldName(method);
        MatcherAssert.assertThat(result, Matchers.is("field3"));


    }

    @Test
    public void getFieldName_should_return_true_for_is_getter_method_with_passed_method() throws NoSuchMethodException {

        final Method method = TestClass.class.getDeclaredMethod("isField2", null);

        final String result = GetterUtilities.getFieldName(method);
        MatcherAssert.assertThat(result, Matchers.is("field2"));


    }


    @Test
    public void getFieldName_should_return_true_for_non_getter_method_with_passed_method() throws NoSuchMethodException {

        final Method method = TestClass.class.getDeclaredMethod("nonGetterField", null);

        final String result = GetterUtilities.getFieldName(method);
        MatcherAssert.assertThat(result, Matchers.nullValue());


    }

    @Test
    public void getFieldName_should_return_true_for_null_valued_passed_method() throws NoSuchMethodException {

        final Method method = null;

        final String result = GetterUtilities.getFieldName(method);
        MatcherAssert.assertThat(result, Matchers.nullValue());


    }

    @Test
    public void getFieldName_should_return_true_for_get_getter_methodname() throws NoSuchMethodException {


        final String result = GetterUtilities.getFieldName("getField1");
        MatcherAssert.assertThat(result, Matchers.is("field1"));


    }

    @Test
    public void getFieldName_should_return_true_for_has_getter_methodname() {

        final String result = GetterUtilities.getFieldName("hasField3");
        MatcherAssert.assertThat(result, Matchers.is("field3"));


    }

    @Test
    public void getFieldName_should_return_true_for_is_getter_methodname() {


        final String result = GetterUtilities.getFieldName("isField2");
        MatcherAssert.assertThat(result, Matchers.is("field2"));


    }


    @Test
    public void getFieldName_should_return_true_for_non_getter_methodname() throws NoSuchMethodException {

        final String result = GetterUtilities.getFieldName("nonGetterField");
        MatcherAssert.assertThat(result, Matchers.nullValue());


    }

    @Test
    public void getFieldName_should_return_true_for_null_valued_methodname() throws NoSuchMethodException {

        final Method method = null;

        final String result = GetterUtilities.getFieldName(method);
        MatcherAssert.assertThat(result, Matchers.nullValue());


    }

    @Test
    public void getFullQualifiedFieldName_for_getter_field() throws NoSuchMethodException {

        final String expectedResult = TestClass.class.getCanonicalName() + "." + "nonGetterField";

        final Method method = TestClass.class.getDeclaredMethod("nonGetterField", null);
        final Class type = TestClass.class;

        String result = GetterUtilities.getFullQualifiedFieldName(type, method);

        MatcherAssert.assertThat(result, Matchers.is(expectedResult));

    }

    @Test
    public void getFullQualifiedFieldName_for_getter_field_with_null_valued_type() throws NoSuchMethodException {

        final String expectedResult = TestClass.class.getCanonicalName() + "." + "field1";

        final Method method = TestClass.class.getDeclaredMethod("getField1", null);

        String result = GetterUtilities.getFullQualifiedFieldName(null, method);

        MatcherAssert.assertThat(result, Matchers.is(expectedResult));

    }

    @Test
    public void getFullQualifiedFieldName_for_non_getter_field() throws NoSuchMethodException {

        final String expectedResult = TestClass.class.getCanonicalName() + "." + "field1";

        final Method method = TestClass.class.getDeclaredMethod("getField1", null);
        final Class type = TestClass.class;

        String result = GetterUtilities.getFullQualifiedFieldName(type, method);

        MatcherAssert.assertThat(result, Matchers.is(expectedResult));


    }


    @Test
    public void capitalizeFirstChar_with_null_valued_input() {
        final String result = GetterUtilities.capitalizeFirstCharOfString(null);
        MatcherAssert.assertThat(result, Matchers.is(""));
    }

    @Test
    public void capitalizeFirstChar_with_empty_input() {
        final String result = GetterUtilities.capitalizeFirstCharOfString("");
        MatcherAssert.assertThat(result, Matchers.is(""));
    }

    @Test
    public void capitalizeFirstChar_with_input_of_length_one() {
        final String result = GetterUtilities.capitalizeFirstCharOfString("a");
        MatcherAssert.assertThat(result, Matchers.is("A"));
    }

    @Test
    public void capitalizeFirstChar_with_input() {
        final String result = GetterUtilities.capitalizeFirstCharOfString("abc");
        MatcherAssert.assertThat(result, Matchers.is("Abc"));
    }

    @Test
    public void decapitalizeFirstChar_with_null_valued_input() {
        final String result = GetterUtilities.decapitalizeFirstCharOfString(null);
        MatcherAssert.assertThat(result, Matchers.is(""));
    }

    @Test
    public void decapitalizeFirstChar_with_empty_input() {
        final String result = GetterUtilities.decapitalizeFirstCharOfString("");
        MatcherAssert.assertThat(result, Matchers.is(""));
    }

    @Test
    public void decapitalizeFirstChar_with_input_of_length_one() {
        final String result = GetterUtilities.decapitalizeFirstCharOfString("A");
        MatcherAssert.assertThat(result, Matchers.is("a"));
    }

    @Test
    public void decapitalizeFirstChar_with_input() {
        final String result = GetterUtilities.decapitalizeFirstCharOfString("Abc");
        MatcherAssert.assertThat(result, Matchers.is("abc"));
    }


}
