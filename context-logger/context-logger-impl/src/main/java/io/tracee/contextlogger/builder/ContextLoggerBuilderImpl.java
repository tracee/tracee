package io.tracee.contextlogger.builder;


import io.tracee.contextlogger.builder.gson.TraceeGsonContextLogBuilder;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
class ContextLoggerBuilderImpl implements ContextLoggerBuilder {


	private final ContextLoggerConfiguration contextLoggerConfiguration;

	private ConfigBuilderImpl configBuilder = new ConfigBuilderImpl(this);

	public ContextLoggerBuilderImpl(ContextLoggerConfiguration contextLoggerConfiguration) {
		this.contextLoggerConfiguration = contextLoggerConfiguration;
	}

	@Override
	public ConfigBuilder config() {
		return configBuilder;
	}

	@Override
	public ContextLogger build() {
		return new TraceeContextLogger(createGsonLogBuilder(), contextLoggerConfiguration);
	}


	/**
	 * Creates a TraceeGsonContextLogBuilder instance which can be used for creating the log message.
	 *
	 * @return An instance of TraceeGsonContextLogBuilder
	 */
	private TraceeGsonContextLogBuilder createGsonLogBuilder() {

		TraceeGsonContextLogBuilder tmpTraceeGsonContextLogBuilder = new TraceeGsonContextLogBuilder();
		tmpTraceeGsonContextLogBuilder.setWrapperClasses(contextLoggerConfiguration.getWrapperClasses());
		tmpTraceeGsonContextLogBuilder.setManualContextOverrides(configBuilder.getManualContextOverrides());
		tmpTraceeGsonContextLogBuilder.setProfile(this.configBuilder.getProfile());

		return tmpTraceeGsonContextLogBuilder;
	}
}
