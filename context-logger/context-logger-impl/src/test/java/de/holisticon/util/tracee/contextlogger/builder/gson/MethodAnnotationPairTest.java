package de.holisticon.util.tracee.contextlogger.builder.gson;

import de.holisticon.util.tracee.contextlogger.api.TraceeContextLogProviderMethod;
import de.holisticon.util.tracee.contextlogger.profile.Profile;
import de.holisticon.util.tracee.contextlogger.profile.ProfileSettings;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Test class for {@link de.holisticon.util.tracee.contextlogger.builder.gson.MethodAnnotationPair}.
 * Created by Tobias Gindler, holisticon AG on 01.04.14.
 */
public class MethodAnnotationPairTest {

    private final static String PROPERTY_NAME = "KEY";
    private static Method METHOD_WITH_EMPTY_PROPERTY_NAME;
    private static Method METHOD_WITH_PROPERTY_NAME;


    @TraceeContextLogProviderMethod(propertyName = "", displayName = "")
    public void testWithEmptyPropertyName() {

    }

    @TraceeContextLogProviderMethod(propertyName = PROPERTY_NAME, displayName = "")
    public void testWithPropertyName() {

    }

    {
        try {
            METHOD_WITH_EMPTY_PROPERTY_NAME = MethodAnnotationPairTest.class.getMethod("testWithEmptyPropertyName", new Class[0]);
            METHOD_WITH_PROPERTY_NAME = MethodAnnotationPairTest.class.getMethod("testWithPropertyName", new Class[0]);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void should_return_true_for_non_annotated_method() {

        MethodAnnotationPair methodAnnotationPair = new MethodAnnotationPair(METHOD_WITH_EMPTY_PROPERTY_NAME, null);
        ProfileSettings profileSettings = new ProfileSettings(Profile.NONE, new HashMap<String, Boolean>());

        boolean result = methodAnnotationPair.shouldBeProcessed(profileSettings);

        MatcherAssert.assertThat(result, Matchers.equalTo(true));


    }

    @Test
    public void should_return_true_for_annotated_method_with_empty_property_name() {

        MethodAnnotationPair methodAnnotationPair = new MethodAnnotationPair(METHOD_WITH_EMPTY_PROPERTY_NAME, getAnnotation(METHOD_WITH_EMPTY_PROPERTY_NAME));
        ProfileSettings profileSettings = new ProfileSettings(Profile.NONE, new HashMap<String, Boolean>());

        boolean result = methodAnnotationPair.shouldBeProcessed(profileSettings);

        MatcherAssert.assertThat(result, Matchers.equalTo(true));

    }

    @Test
    public void should_return_true_for_annotated_method_with_property_name_and_set_profiler_settings() {

        Map<String, Boolean> map = new HashMap<String, Boolean>();
        map.put(PROPERTY_NAME, true);

        MethodAnnotationPair methodAnnotationPair = new MethodAnnotationPair(METHOD_WITH_EMPTY_PROPERTY_NAME, getAnnotation(METHOD_WITH_PROPERTY_NAME));
        ProfileSettings profileSettings = new ProfileSettings(Profile.NONE, map);

        boolean result = methodAnnotationPair.shouldBeProcessed(profileSettings);

        MatcherAssert.assertThat(result, Matchers.equalTo(true));

    }

    @Test
    public void should_return_false_for_annotated_method_with_property_name_and_not_set_profiler_settings() {

        Map<String, Boolean> map = new HashMap<String, Boolean>();
        map.put(PROPERTY_NAME, false);

        MethodAnnotationPair methodAnnotationPair = new MethodAnnotationPair(METHOD_WITH_EMPTY_PROPERTY_NAME, getAnnotation(METHOD_WITH_PROPERTY_NAME));
        ProfileSettings profileSettings = new ProfileSettings(Profile.NONE, map);

        boolean result = methodAnnotationPair.shouldBeProcessed(profileSettings);

        MatcherAssert.assertThat(result, Matchers.equalTo(false));

    }

    @Test
    public void should_return_false_for_annotated_method_with_property_name_and_null_valued_profiler_settings() {

        MethodAnnotationPair methodAnnotationPair = new MethodAnnotationPair(METHOD_WITH_EMPTY_PROPERTY_NAME, getAnnotation(METHOD_WITH_PROPERTY_NAME));

        boolean result = methodAnnotationPair.shouldBeProcessed(null);

        MatcherAssert.assertThat(result, Matchers.equalTo(true));

    }

    private TraceeContextLogProviderMethod getAnnotation(final Method method) {
        return method.getAnnotation(TraceeContextLogProviderMethod.class);
    }

}
