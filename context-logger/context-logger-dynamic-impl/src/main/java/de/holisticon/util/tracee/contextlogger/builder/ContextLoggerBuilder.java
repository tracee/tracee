package de.holisticon.util.tracee.contextlogger.builder;

/**
 * The context logger builder interface used to enable fluent API.
 * Created by Tobias Gindler, holisticon AG on 28.03.14.
 */
public interface ContextLoggerBuilder {

    /**
     * Switches fluent api to configuration mode
     * @return This instance cast as a ConfigBuilder
     */
    ConfigBuilder config();

    /**
     * Creates the context logger instance and switches to context logger mode.
     * @return This instance cast as a ContextLogger
     */
    ContextLogger build();

}
