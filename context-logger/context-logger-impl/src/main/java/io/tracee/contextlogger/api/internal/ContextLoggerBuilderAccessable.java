package io.tracee.contextlogger.api.internal;

import io.tracee.contextlogger.api.ContextLogger;
import io.tracee.contextlogger.api.TraceeContextStringRepresentationBuilder;
import io.tracee.contextlogger.impl.ContextLoggerConfiguration;

/**
 * Marks a class that a {@link TraceeContextStringRepresentationBuilder} can be added.
 * Created by Tobias Gindler on 19.06.14.
 */
public interface ContextLoggerBuilderAccessable extends ContextLogger {

    /**
     * Used to apply the string representation builder
     *
     * @param stringRepresentationBuilder
     */
    void setStringRepresentationBuilder(final TraceeContextStringRepresentationBuilder stringRepresentationBuilder);

    /**
     * Gets the context logger configuration.
     */
    ContextLoggerConfiguration getContextLoggerConfiguration();

}
