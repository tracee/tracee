package io.tracee.contextlogger.builder;

import io.tracee.contextlogger.profile.Profile;

/**
 * Interface for manual overrides. (Enabling / Disabling of certain logger context data)
 * Used to enable fluent API.
 * Created by Tobias Gindler on 28.03.14.
 */
public interface ConfigBuilder {

    /**
     * Enforces profile for a single log statement.
     * @param profile the profile to use
     * @return this instance
     */
    ConfigBuilder enforceProfile(Profile profile);

    /**
     * Manually enables context data output for a single log statement.
     * @param contexts The context data to be enabled.
     * @return This instance
     */
    ConfigBuilder enable(String... contexts);

    /**
     * Manually disables context data output for a single log statement.
     * @param contexts The context data to be disabled.
     * @return This instance
     */
    ConfigBuilder disable(String... contexts);

    /**
     * Closes configuration.
     * @return This instance cast as a ContextLoggerBuilder.
     */
    ContextLoggerBuilder apply();

}
