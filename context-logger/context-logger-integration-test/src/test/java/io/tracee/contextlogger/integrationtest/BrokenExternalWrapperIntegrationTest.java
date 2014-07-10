package io.tracee.contextlogger.integrationtest;


import io.tracee.contextlogger.TraceeContextLogger;
import io.tracee.contextlogger.profile.Profile;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

public class BrokenExternalWrapperIntegrationTest {

    @Test
    public void should_ignore_properties_for_wrapper_that_throw_an_exception() {

        // should not break because of the thrown NPE, Exception is handled internally ==> getter with exception should be ignored
        String result = TraceeContextLogger.create().config().enforceProfile(Profile.ENHANCED).apply().build().createJson(new WrappedBrokenTestContextData());
        MatcherAssert.assertThat(result, Matchers.is("{\"brokenCustomContextDataWrapper\":{}}"));

    }


    @Test
     public void should_ignore_properties_for_custom_implicit_context_data_provider_that_throw_an_exception() {

        // should not break because of the thrown NPE, Exception is handled internally ==> getter with exception should be ignored
        String result = TraceeContextLogger.create().config().enforceProfile(Profile.ENHANCED).apply().build().createJson(new TestBrokenImplicitContextDataProvider());
        MatcherAssert.assertThat(result, Matchers.is("{\"testBrokenImplicitContextData\":{}}"));

    }

    @Test
    public void should_ignore_properties_for_custom_implicit_context_data_provider_that_throw_an_exception2() {

        // should not break because of the thrown NPE, Exception is handled internally ==> getter with exception should be ignored
        String result = TraceeContextLogger.create().config().enforceProfile(Profile.ENHANCED).apply().build().createJson(TestBrokenImplicitContextDataProvider.class);
        MatcherAssert.assertThat(result, Matchers.is("{\"testBrokenImplicitContextData\":{}}"));

    }

}
