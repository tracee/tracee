package io.tracee.contextlogger.builder;

import io.tracee.contextlogger.ImplicitContext;
import io.tracee.contextlogger.RegexMatcher;
import io.tracee.contextlogger.builder.gson.TraceeGsonContextStringRepresentationBuilder;
import io.tracee.contextlogger.data.subdata.java.JavaThrowableContextProvider;
import io.tracee.contextlogger.data.subdata.tracee.PassedDataContextProvider;
import io.tracee.contextlogger.profile.Profile;
import io.tracee.contextlogger.profile.ProfilePropertyNames;
import org.hamcrest.MatcherAssert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * Test class for {@link io.tracee.contextlogger.builder.TraceeContextLogger}.
 * Created by Tobias Gindler, holisticon AG on 21.03.14.
 */
public class TraceeContextLoggerTest {



    @Test
    public void should_create_context_log () {
        try {
            throw new NullPointerException("acd");
        } catch (NullPointerException e) {

            String json = TraceeContextLogger.createDefault().createJson(e);

            MatcherAssert.assertThat(json, RegexMatcher.matches("\\{\\\"throwable\\\":\\{\"message\":\"acd\",\"stacktrace\":\"java.lang.NullPointerException.*\\}"));


        }
    }

    @Test
    public void should_create_mixed_context_log () {
        try {
            throw new NullPointerException();
        } catch (NullPointerException e) {

            TraceeGsonContextStringRepresentationBuilder logBuilder = new TraceeGsonContextStringRepresentationBuilder();
            Set<Class> classes = new HashSet<Class>();
            classes.add(PassedDataContextProvider.class);

            logBuilder.setWrapperClasses(classes);

            Object instance1 = e;
            Object instance2 = "TATA";
            Object instance3 = new JavaThrowableContextProvider(e);


            String json = TraceeContextLogger.createDefault().createJson(instance1, instance2, 212, instance3, 212.2);

            MatcherAssert.assertThat(json, RegexMatcher.matches("\\{\\\"throwable\\\":\\{\"stacktrace\":\"java.lang.NullPointerException.*\\\"java.lang.Double\\\":\\\"212.2\\\",\\\"java.lang.Integer\\\":\\\"212\\\",\\\"java.lang.String\\\":\\\"TATA\\\"\\}"));

        }
    }

    @Test
    public void should_create_context_log_with_implicit_logs () {
        try {
            throw new NullPointerException("test");
        } catch (NullPointerException e) {

            String json = TraceeContextLogger.createDefault().createJson(ImplicitContext.COMMON, ImplicitContext.TRACEE, e);

            MatcherAssert.assertThat(json, RegexMatcher.matches("\\{\\\"common\\\".*tracee.*throwable.*\\}"));


        }
    }


    @Test
    public void should_return_empty_throwable_context_element_for_none_profile_by_fluent_api() {
        try {
            throw new NullPointerException("acd");
        } catch (NullPointerException e) {

            String json = TraceeContextLogger.create().config().enforceProfile(Profile.NONE).apply().build().createJson(e);

            MatcherAssert.assertThat(json, RegexMatcher.matches("\\{\\\"throwable\\\":\\{\\}\\}"));


        }
    }

    @Test
    public void should_return_throwable_context_element_with_message_for_none_profile_by_fluent_api() {
        try {
            throw new NullPointerException("acd");
        } catch (NullPointerException e) {

            String json = TraceeContextLogger.create().config().enforceProfile(Profile.NONE).enable(ProfilePropertyNames.EXCEPTION_MESSAGE).apply().build().createJson(e);

            MatcherAssert.assertThat(json, RegexMatcher.matches("\\{\\\"throwable\\\":\\{\"message\":\"acd\"\\}\\}"));


        }
    }

    @Test
    public void should_return_throwable_context_element_with_message_for_basic_profile_with_disabled_stacktrace_by_fluent_api() {
        try {
            throw new NullPointerException("acd");
        } catch (NullPointerException e) {

            String json = TraceeContextLogger.create().config().enforceProfile(Profile.BASIC).disable(ProfilePropertyNames.EXCEPTION_STACKTRACE).apply().build().createJson(e);

            MatcherAssert.assertThat(json, RegexMatcher.matches("\\{\\\"throwable\\\":\\{\"message\":\"acd\"\\}\\}"));


        }
    }

}
