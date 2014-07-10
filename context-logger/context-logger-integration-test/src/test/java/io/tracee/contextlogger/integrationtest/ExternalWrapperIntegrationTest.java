package io.tracee.contextlogger.integrationtest;


import io.tracee.contextlogger.TraceeContextLogger;
import io.tracee.contextlogger.profile.Profile;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

public class ExternalWrapperIntegrationTest {

    @Test
    public void should_wrap_with_external_wrappers_correctly_using_basic_profile() {

        String result = TraceeContextLogger.create().config().enforceProfile(Profile.BASIC).apply().build().createJson(new WrappedTestContextData());

        MatcherAssert.assertThat(result, Matchers.is("{\"testdata\":{}}"));

    }

    @Test
    public void should_wrap_with_external_wrappers_correctly_using_enhanced_profile() {

        String result = TraceeContextLogger.create().config().enforceProfile(Profile.ENHANCED).apply().build().createJson(new WrappedTestContextData());

        MatcherAssert.assertThat(result, Matchers.is("{\"testdata\":{\"testoutput\":\"IT WORKS !!!\"}}"));

    }


    @Test
    public void should_wrap_with_external_wrappers_correctly_using_enhanced_profile_using_manual_override_to_disable_output() {

        String result = TraceeContextLogger.create().config().enforceProfile(Profile.ENHANCED).disable(TestContextDataWrapper.PROPERTY_NAME).apply().build().createJson(new WrappedTestContextData());

        MatcherAssert.assertThat(result, Matchers.is("{\"testdata\":{}}"));

    }

    @Test
    public void should_wrap_with_implicit_context_provider_correctly_using_basic_profile() {

        String result = TraceeContextLogger.create().config().enforceProfile(Profile.BASIC).apply().build().createJson(new TestImplicitContextDataProvider());

        MatcherAssert.assertThat(result, Matchers.is("{\"testImplicitContextData\":{}}"));

    }

    @Test
    public void should_wrap_with_implicit_context_provider_correctly_using_enhanced_profile() {

        String result = TraceeContextLogger.create().config().enforceProfile(Profile.ENHANCED).apply().build().createJson(new TestImplicitContextDataProvider());

        MatcherAssert.assertThat(result, Matchers.is("{\"testImplicitContextData\":{\"output\":\"IT WORKS TOO!!!\"}}"));

    }


    @Test
    public void should_wrap_with_implicit_context_provider_correctly_using_enhanced_profile_using_manual_override_to_disable_output() {

        String result = TraceeContextLogger.create().config().enforceProfile(Profile.ENHANCED).disable(TestImplicitContextDataProvider.PROPERTY_NAME).apply().build().createJson(new  TestImplicitContextDataProvider());

        MatcherAssert.assertThat(result, Matchers.is("{\"testImplicitContextData\":{}}"));

    }

}
