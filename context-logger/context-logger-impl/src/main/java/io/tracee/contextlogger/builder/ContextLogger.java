package io.tracee.contextlogger.builder;

/**
 * The context logger interface used to enable fluent API.
 * Created by Tobias Gindler, holisticon AG on 28.03.14.
 */
public interface ContextLogger {
    /**
     * Creates a json string representation of the passed instancesToLog.
     * @param instancesToLog The instances to be converted into a json string.
     * @return
     */
    String createJson(Object... instancesToLog);

    /**
     * Creates a json string representation of the passed instancesToLog and passes them to all configured connectors.
     * @param instancesToLog The instances to be converted into a json string.
     */
    void logJson(Object... instancesToLog);

    /**
     * Creates a json string representation of the passed instancesToLog and passes them to all configured connectors.
     * Adds a prefixed message string for {@link io.tracee.contextlogger.connector.LogConnector}.
     * @param prefixedMessage The message to be prefixed with the LogConnector
     * @param instancesToLog The instances to be converted into a json string.
     */
    void logJsonWithPrefixedMessage(String prefixedMessage, Object... instancesToLog);

}
