package io.tracee.contextlogger.impl;


import io.tracee.contextlogger.api.ConfigBuilder;
import io.tracee.contextlogger.api.ContextLogger;
import io.tracee.contextlogger.api.ContextLoggerBuilder;
import io.tracee.contextlogger.api.internal.Configuration;
import io.tracee.contextlogger.api.internal.ContextLoggerBuilderAccessable;
import io.tracee.contextlogger.impl.gson.TraceeGsonContextStringRepresentationBuilder;

/**
 * Class for creating and configuring a gson context logger.
 * Supports the fluent builder api.
 */
public class ContextLoggerBuilderImpl implements ContextLoggerBuilder {


    private final ContextLoggerBuilderAccessable contextLogger;
    private final ContextLoggerConfiguration contextLoggerConfiguration;

    private Configuration configuration = new ConfigBuilderImpl(this);

    public ContextLoggerBuilderImpl(ContextLoggerBuilderAccessable traceeContextLoggerBuilderAccessable) {
        this.contextLogger = traceeContextLoggerBuilderAccessable;
        this.contextLoggerConfiguration = traceeContextLoggerBuilderAccessable.getContextLoggerConfiguration();
    }

    @Override
    public ConfigBuilder config() {
        return configuration;
    }

    @Override
    public ContextLogger build() {
        contextLogger.setStringRepresentationBuilder(createGsonContextStringRepresentationLogBuilder());
        return contextLogger;
    }


    /**
     * Creates a TraceeGsonContextStringRepresentationBuilder instance which can be used for creating the createStringRepresentation message.
     *
     * @return An instance of TraceeGsonContextStringRepresentationBuilder
     */
    private TraceeGsonContextStringRepresentationBuilder createGsonContextStringRepresentationLogBuilder() {

        TraceeGsonContextStringRepresentationBuilder tmpTraceeGsonContextStringRepresentationBuilder = new TraceeGsonContextStringRepresentationBuilder();
        tmpTraceeGsonContextStringRepresentationBuilder.setWrapperClasses(contextLoggerConfiguration.getWrapperClasses());
        tmpTraceeGsonContextStringRepresentationBuilder.setManualContextOverrides(configuration.getManualContextOverrides());
        tmpTraceeGsonContextStringRepresentationBuilder.setProfile(this.configuration.getProfile());
        tmpTraceeGsonContextStringRepresentationBuilder.setKeepOrder(this.configuration.getKeepOrder());

        return tmpTraceeGsonContextStringRepresentationBuilder;
    }
}
