package io.tracee.contextlogger.builder.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.tracee.contextlogger.RegexMatcher;
import io.tracee.contextlogger.TraceeContextLoggerConstants;
import io.tracee.contextlogger.contextprovider.tracee.CommonDataContextProvider;
import io.tracee.contextlogger.profile.Profile;
import io.tracee.contextlogger.profile.ProfileSettings;
import io.tracee.contextlogger.testdata.AnnotationTestClass;
import org.hamcrest.Matchers;
import org.joda.time.DateTimeUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import org.hamcrest.MatcherAssert;

/**
 * Test class for {@link io.tracee.contextlogger.builder.gson.TraceeGenericGsonSerializer}.
 * Created by Tobias Gindler, holisticon AG on 14.03.14.
 */
public class TraceeGenericGsonSerializerTest {


    @Before
    public void init() {
        // Be sure not to use fixed date
        DateTimeUtils.setCurrentMillisSystem();

        System.getProperties().remove(TraceeContextLoggerConstants.SYSTEM_PROPERTY_NAME_STAGE);
        System.getProperties().remove(TraceeContextLoggerConstants.SYSTEM_PROPERTY_NAME_SYSTEM);
    }

    @Test
    @Ignore
    public void should_return_json_representation () {

        Gson gson = new GsonBuilder().registerTypeAdapter(AnnotationTestClass.class, new TraceeGenericGsonSerializer(new ProfileSettings(Profile.getCurrentProfile(),null))).create();
        String json = gson.toJson(new AnnotationTestClass());

        MatcherAssert.assertThat(json, Matchers.notNullValue());
        MatcherAssert.assertThat(json, Matchers.equalTo("{\"B\":\"test\",\"C\":[5,8],\"E\":[3,2],\"A\":0}"));

    }

    @Test
    public void should_return_json_representation_of_common_data_provider () {

        System.setProperty(TraceeContextLoggerConstants.SYSTEM_PROPERTY_NAME_STAGE, "DEBUG");
        System.setProperty(TraceeContextLoggerConstants.SYSTEM_PROPERTY_NAME_SYSTEM, "SYSTEM_1");

        Gson gson = new GsonBuilder().registerTypeAdapter(CommonDataContextProvider.class, new TraceeGenericGsonSerializer(new ProfileSettings(Profile.getCurrentProfile(),null))).create();
        String json = gson.toJson(new CommonDataContextProvider());

        MatcherAssert.assertThat(json, Matchers.notNullValue());
        MatcherAssert.assertThat(json, RegexMatcher.matches("\\{\\\"timestamp\\\":\\\".*?\\\",\\\"stage\\\":\\\"DEBUG\\\",\\\"system-name\\\":\\\"SYSTEM_1\\\",\\\"thread-name\\\":\\\"main\\\",\\\"thread-id\\\":1\\}"));

    }




}
