package io.tracee.contextlogger.builder.config;

import io.tracee.contextlogger.api.ConfigBuilder;
import io.tracee.contextlogger.api.ContextLogger;
import io.tracee.contextlogger.api.ContextLoggerBuilder;
import io.tracee.contextlogger.api.internal.Configuration;
import io.tracee.contextlogger.profile.Profile;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Test class for {@link io.tracee.contextlogger.builder.config.ConfigBuilderImpl}.
 * Created by Tobias Gindler on 19.06.14.
 */
public class ConfigBuilderImplTest {


    ContextLoggerBuilder contextLoggerBuilder;
    ConfigBuilder configBuilder;

    @Before
    public void init() {

        contextLoggerBuilder = new

                ContextLoggerBuilder() {
                    @Override
                    public ConfigBuilder config() {
                        return new ConfigBuilderImpl(this);
                    }

                    @Override
                    public ContextLogger build() {
                        return null;
                    }
                }
        ;
        configBuilder =  contextLoggerBuilder.config();
    }

    @Test
    public void should_set_enable_and_disable_correctly() {

        final String DISABLED_1 = "D1";
        final String DISABLED_2 = "D2";

        final String ENABLED_1 = "E1";
        final String ENABLED_2 = "E2";

        Configuration configuration = (Configuration) contextLoggerBuilder.config().disable(DISABLED_1, DISABLED_2).enable(ENABLED_1, ENABLED_2);
        assertThat(configuration, Matchers.notNullValue());

        Map<String, Boolean> manualContextOverrides = configuration.getManualContextOverrides();

        assertThat(manualContextOverrides.get(DISABLED_1), Matchers.is(false));
        assertThat(manualContextOverrides.get(DISABLED_2), Matchers.is(false));
        assertThat(manualContextOverrides.get(ENABLED_1), Matchers.is(true));
        assertThat(manualContextOverrides.get(ENABLED_2), Matchers.is(true));

    }

    @Test
    public void should_have_default_empty_manual_context_override_map() {
        Configuration configuration = (Configuration) contextLoggerBuilder.config();
        assertThat(configuration, Matchers.notNullValue());

        Map<String, Boolean> manualContextOverrides = configuration.getManualContextOverrides();
        assertThat(manualContextOverrides, Matchers.notNullValue());
        assertThat(manualContextOverrides.size(), Matchers.is(0));
    }

    @Test
    public void should_set_keepOrder_correctly() {
        Configuration configuration = (Configuration) contextLoggerBuilder.config().keepOrder();
        assertThat(configuration, Matchers.notNullValue());
        assertThat(configuration.getKeepOrder(), Matchers.is(true));
    }

    @Test
    public void should_have_correct_default_keep_order_value() {
        Configuration configuration = (Configuration) contextLoggerBuilder.config();
        assertThat(configuration, Matchers.notNullValue());
        assertThat(configuration.getKeepOrder(), Matchers.is(false));
    }



    @Test
    public void should_return_config_logger_builder_on_apply() {

        ContextLoggerBuilder result = contextLoggerBuilder.config().apply();
        assertThat(result, Matchers.is(contextLoggerBuilder));

    }

    @Test
    public void should_return_null_valued_default_profile () {

        Configuration configuration = (Configuration) contextLoggerBuilder.config();
        assertThat(configuration, Matchers.notNullValue());
        assertThat(configuration.getProfile(), Matchers.nullValue());

    }

    @Test
    public void should_set_profile_correctly () {

        Configuration configuration = (Configuration) contextLoggerBuilder.config().enforceProfile(Profile.FULL);
        assertThat(configuration, Matchers.notNullValue());
        assertThat(configuration.getProfile(), Matchers.is(Profile.FULL));

    }

}
