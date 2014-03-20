package de.holisticon.util.tracee.contextlogger.builder.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.holisticon.util.tracee.contextlogger.TraceeContextLoggerConstants;
import de.holisticon.util.tracee.contextlogger.data.subdata.tracee.CommonDataProvider;
import de.holisticon.util.tracee.contextlogger.testdata.AnnotationTestClass;
import org.hamcrest.Matchers;
import org.joda.time.DateTimeUtils;
import org.junit.Before;
import org.junit.Test;

import org.hamcrest.MatcherAssert;

/**
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
    public void should_return_json_representation () {

        Gson gson = new GsonBuilder().registerTypeAdapter(AnnotationTestClass.class, new TraceeGenericGsonSerializer()).create();
        String json = gson.toJson(new AnnotationTestClass());

        MatcherAssert.assertThat(json, Matchers.notNullValue());
        MatcherAssert.assertThat(json, Matchers.equalTo("{\"B\":\"test\",\"C\":[5,8],\"E\":[3,2],\"A\":0}"));

    }

    @Test
    public void should_return_json_representation_of_common_data_provider () {

        System.setProperty(TraceeContextLoggerConstants.SYSTEM_PROPERTY_NAME_STAGE, "DEBUG");
        System.setProperty(TraceeContextLoggerConstants.SYSTEM_PROPERTY_NAME_SYSTEM, "SYSTEM_1");

        Gson gson = new GsonBuilder().registerTypeAdapter(CommonDataProvider.class, new TraceeGenericGsonSerializer()).create();
        String json = gson.toJson(new CommonDataProvider());

        MatcherAssert.assertThat(json, Matchers.notNullValue());
        MatcherAssert.assertThat(json.matches("\\{\\\"timestamp\\\":\\\".*?\\\",\\\"stage\\\":\\\"DEBUG\\\",\\\"system-name\\\":\\\"SYSTEM_1\\\",\\\"thread-name\\\":\\\"main\\\",\\\"thread-id\\\":1\\}"), Matchers.equalTo(true));

    }

}
