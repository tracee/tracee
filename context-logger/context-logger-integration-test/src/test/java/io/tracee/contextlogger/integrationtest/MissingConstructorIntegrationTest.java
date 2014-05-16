package io.tracee.contextlogger.integrationtest;

import io.tracee.contextlogger.builder.TraceeContextLogger;
import io.tracee.contextlogger.profile.Profile;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Integration test to check behavior for custom context data providers and wrapper that have no no args constructor.
 */
public class MissingConstructorIntegrationTest {

    @Test
    public void should_handle_missing_no_args_constructor_gently() {

        // should not break because of the missing no args constructor => type will be deserialized instead
        String result = TraceeContextLogger.create().config().enforceProfile(Profile.ENHANCED).apply().build().createJson(TestBrokenImplicitContentDataProviderWithoutDefaultConstructor.class);
        MatcherAssert.assertThat(result, Matchers.startsWith("{\"java.lang.Class\""));


    }

    @Test
    public void should_wrap_with_external_wrappers_correctly_using_enhanced_profile() {

        // should not default deserialization mechanism, because context data provider wrapper can't be created.
        String result = TraceeContextLogger.create().config().enforceProfile(Profile.ENHANCED).apply().build().createJson(new BrokenCustomContextDataWrapperWithMissingNoargsConstructor());
        MatcherAssert.assertThat(result, Matchers.startsWith("{\"io.tracee.contextlogger.integrationtest.BrokenCustomContextDataWrapperWithMissingNoargsConstructor\""));

    }

}
