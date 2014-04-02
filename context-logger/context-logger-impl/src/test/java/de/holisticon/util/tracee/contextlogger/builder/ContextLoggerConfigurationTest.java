package de.holisticon.util.tracee.contextlogger.builder;

import de.holisticon.util.tracee.contextlogger.ImplicitContext;
import de.holisticon.util.tracee.contextlogger.data.TypeToWrapper;
import de.holisticon.util.tracee.contextlogger.data.subdata.java.JavaThrowableContextProvider;
import de.holisticon.util.tracee.contextlogger.data.subdata.servlet.ServletRequestContextProvider;
import de.holisticon.util.tracee.contextlogger.profile.Profile;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;

/**
 * Test class for {@link de.holisticon.util.tracee.contextlogger.builder.ContextLoggerConfiguration}.
 * Created by Tobias Gindler, holisticon AG on 01.04.14.
 */
public class ContextLoggerConfigurationTest {

    @Test
    public void should_get_context_logger_configuration() {

        ContextLoggerConfiguration contextLoggerConfiguration = ContextLoggerConfiguration.getOrCreateContextLoggerConfiguration();

        MatcherAssert.assertThat(contextLoggerConfiguration, Matchers.notNullValue());

    }

    @Test
    public void should_get_implicit_wrapper_list_of_context_logger_configuration() {

        ContextLoggerConfiguration contextLoggerConfiguration = ContextLoggerConfiguration.getOrCreateContextLoggerConfiguration();

        MatcherAssert.assertThat(contextLoggerConfiguration, Matchers.notNullValue());
        MatcherAssert.assertThat(contextLoggerConfiguration.getImplicitContextClassMap(),Matchers.notNullValue());
        MatcherAssert.assertThat(contextLoggerConfiguration.getImplicitContextClassMap().containsKey(ImplicitContext.COMMON),Matchers.equalTo(true));
        MatcherAssert.assertThat(contextLoggerConfiguration.getImplicitContextClassMap().containsKey(ImplicitContext.TRACEE),Matchers.equalTo(true));

    }

    @Test
    public void should_get_wrapper_class_mapping_of_context_logger_configuration() {

        ContextLoggerConfiguration contextLoggerConfiguration = ContextLoggerConfiguration.getOrCreateContextLoggerConfiguration();

        MatcherAssert.assertThat(contextLoggerConfiguration, Matchers.notNullValue());
        MatcherAssert.assertThat(contextLoggerConfiguration.getClassToWrapperMap(),Matchers.notNullValue());
        MatcherAssert.assertThat(contextLoggerConfiguration.getClassToWrapperMap().containsKey(HttpServletRequest.class),Matchers.equalTo(true));
        MatcherAssert.assertThat(contextLoggerConfiguration.getClassToWrapperMap().get(HttpServletRequest.class).equals(ServletRequestContextProvider.class),Matchers.equalTo(true));

    }


    @Test
    public void should_get_profile_of_context_logger_configuration() {

        ContextLoggerConfiguration contextLoggerConfiguration = ContextLoggerConfiguration.getOrCreateContextLoggerConfiguration();

        MatcherAssert.assertThat(contextLoggerConfiguration, Matchers.notNullValue());
        MatcherAssert.assertThat(contextLoggerConfiguration.getProfile(),Matchers.notNullValue());
        MatcherAssert.assertThat(contextLoggerConfiguration.getProfile(),Matchers.equalTo(Profile.BASIC));

    }


    @Test
    public void should_get_wrapper_classes_of_context_logger_configuration() {

        ContextLoggerConfiguration contextLoggerConfiguration = ContextLoggerConfiguration.getOrCreateContextLoggerConfiguration();

        MatcherAssert.assertThat(contextLoggerConfiguration, Matchers.notNullValue());
        MatcherAssert.assertThat(contextLoggerConfiguration.getWrapperClasses(),Matchers.notNullValue());
        MatcherAssert.assertThat(contextLoggerConfiguration.getWrapperClasses().contains(ServletRequestContextProvider.class),Matchers.equalTo(true));
        MatcherAssert.assertThat(contextLoggerConfiguration.getWrapperClasses().contains(JavaThrowableContextProvider.class),Matchers.equalTo(true));

    }


    @Test
    public void should_get_wrappers_of_context_logger_configuration() {

        ContextLoggerConfiguration contextLoggerConfiguration = ContextLoggerConfiguration.getOrCreateContextLoggerConfiguration();

        MatcherAssert.assertThat(contextLoggerConfiguration, Matchers.notNullValue());

        MatcherAssert.assertThat(contextLoggerConfiguration.getWrapperList(),Matchers.notNullValue());
        MatcherAssert.assertThat(contextLoggerConfiguration.getWrapperList().size(),Matchers.greaterThan(0));
    }

}
