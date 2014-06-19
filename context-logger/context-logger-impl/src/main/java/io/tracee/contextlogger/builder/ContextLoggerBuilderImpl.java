package io.tracee.contextlogger.builder;


import io.tracee.contextlogger.api.ContextLogger;
import io.tracee.contextlogger.api.ConfigBuilder;
import io.tracee.contextlogger.api.ContextLoggerBuilder;
import io.tracee.contextlogger.api.internal.Configuration;
import io.tracee.contextlogger.builder.config.ConfigBuilderImpl;
import io.tracee.contextlogger.builder.gson.TraceeGsonContextStringRepresentationBuilder;

/**
 * Class for creating and configuring a gson context logger.
 * Supports the fluent builder api.
 */
class ContextLoggerBuilderImpl implements ContextLoggerBuilder {


	private final ContextLoggerConfiguration contextLoggerConfiguration;

	private Configuration configuration = new ConfigBuilderImpl(this);

	public ContextLoggerBuilderImpl(ContextLoggerConfiguration contextLoggerConfiguration) {
		this.contextLoggerConfiguration = contextLoggerConfiguration;
	}

	@Override
	public ConfigBuilder config() {
		return configuration;
	}

	@Override
	public ContextLogger build() {
		return (ContextLogger)new TraceeContextLogger(createGsonContextStringRepresentationLogBuilder(), contextLoggerConfiguration);
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
