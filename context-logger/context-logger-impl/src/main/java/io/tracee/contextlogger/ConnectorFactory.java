package io.tracee.contextlogger;

import io.tracee.Tracee;
import io.tracee.TraceeLogger;
import io.tracee.contextlogger.connector.LogConnector;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class to pipe messages to all configured connectors.
 * Created by Tobias Gindler, holisticon AG on 26.03.14.
 */
class ConnectorFactory {

    // Connector settings
    private static final Pattern KEY_MATCHER_PATTERN = Pattern.compile(TraceeContextLoggerConstants.SYSTEM_PROPERTY_CONTEXT_LOGGER_CONNECTOR_KEY_PATTERN);
    private static final String CONNECTOR_PROPERTY_GRABBER_PATTERN =
            TraceeContextLoggerConstants.SYSTEM_PROPERTY_CONNECTOR_PREFIX.replaceAll("\\.", "\\.") + "%s\\.(.*)";
    private static final TraceeLogger LOGGER = Tracee.getBackend().getLoggerFactory().getLogger(TraceeContextLogger.class);
    private static final Map<String, String> WELL_KNOW_CONNECTOR_MAPPINGS = new HashMap<String, String>();

    static {
        WELL_KNOW_CONNECTOR_MAPPINGS.put("HttpConnector", WellKnownConnectorClassNames.HTTP_CONNECTOR);
        WELL_KNOW_CONNECTOR_MAPPINGS.put(LogConnector.class.getName(), LogConnector.class.getCanonicalName());
    }

    //Connector
    private final Map<String, Connector> connectorMap = new HashMap<String, Connector>();

    ConnectorFactory() {
        initConnectors();
    }

    /**
     * Initializes all available connectors.
     */
    private void initConnectors() {

        // first get all connector configuration Names
        Set<String> connectorConfigurationNames = this.getConnectorConfigurationNames();

        for (String connectorConfigurationName : connectorConfigurationNames) {

            Connector connector = this.createConnector(connectorConfigurationName);

            if (connector != null) {

                this.connectorMap.put(connectorConfigurationName, connector);

            }

        }

        // Add mandatory logger
        if (!isConnectorConfigured(LogConnector.class)) {
            Connector logConnector = new LogConnector();
            this.connectorMap.put("LOGGER", logConnector);
        }

    }


    /**
     * Send error report to all initialized connector instances.
     *
     * @param prefix the prefix to prepend only via the createStringRepresentation connector output
     * @param json   the context data to output
     */
    final void sendErrorReportToConnectors(String prefix, String json) {

        for (Connector connector : this.connectorMap.values()) {

            // prevent
            if (LogConnector.class.isInstance(connector)) {
                connector.sendErrorReport(prefix != null ? prefix + json : json);
            } else {
                connector.sendErrorReport(json);
            }
        }
    }

    /**
     * Extracts all names for connector configurations from System properties.
     *
     * @return a Set containing all connector configuration names
     */
    final Set<String> getConnectorConfigurationNames() {

        Set<String> connectorNames = new HashSet<String>();

        Enumeration<Object> keyEnumeration = getSystemProperties().keys();
        while (keyEnumeration.hasMoreElements()) {
            String key = keyEnumeration.nextElement().toString();

            // check if property key has tracee connector format
            Matcher matcher = KEY_MATCHER_PATTERN.matcher(key);
            if (matcher.matches() && matcher.groupCount() > 0) {

                connectorNames.add(matcher.group(1));

            }

        }

        return connectorNames;

    }

    /**
     * Collects all properties for a given connector configuration name and writes them to a Map.
     *
     * @param connectorName the name of the connector configuration
     * @return a Map containing all properties for a connector configuration name
     */
    final Map<String, String> getPropertiesForConnectorConfigurationName(final String connectorName) {

        final Map<String, String> propertyMap = new HashMap<String, String>();

        final String patternString = String.format(CONNECTOR_PROPERTY_GRABBER_PATTERN, connectorName);
        final Pattern propertyGrabPattern = Pattern.compile(patternString);

        final Set<Map.Entry<Object, Object>> entries = getSystemProperties().entrySet();

        for (Map.Entry<Object, Object> entry : entries) {
            final String key = entry.getKey().toString();
            final Object value = entry.getValue();

            // check if property key has tracee connector format
            final Matcher matcher = propertyGrabPattern.matcher(key);
            if (value != null && matcher.matches() && matcher.groupCount() > 0) {

                final String propertyName = matcher.group(1);

                propertyMap.put(propertyName, value.toString());

            }

        }

        return propertyMap;

    }


    /**
     * Tries to create a Connector for a given connector configuration name.
     *
     * @param connectorConfigurationName the name of the connector configuration
     * @return the connector if it could be created and initialized without error, otherwise null
     */
    final Connector createConnector(final String connectorConfigurationName) {

        Map<String, String> propertyMap = this.getPropertiesForConnectorConfigurationName(connectorConfigurationName);
        String type = propertyMap.get(TraceeContextLoggerConstants.SYSTEM_PROPERTY_CONTEXT_LOGGER_CONNECTOR_TYPE);

        // get canonical class name for well known connectors
        if (WELL_KNOW_CONNECTOR_MAPPINGS.containsKey(type)) {
            type = WELL_KNOW_CONNECTOR_MAPPINGS.get(type);
        }

        try {

            // try to create connector instance
            Connector connector = (Connector) Class.forName(type).newInstance();

            // now try to call init method
            connector.init(propertyMap);

            return connector;

        } catch (Exception e) {
            LOGGER.error("An error occurred while creating connector with name '" + connectorConfigurationName + "' of type '" + type + "'", e);
        }

        return null;
    }

    /**
     * Checks whether the LogConnector is defined or not.
     *
     * @param connectorClass the connector to check for
     * @return true, if LogConnector is already defined, otherwise false.
     */
    private boolean isConnectorConfigured(Class connectorClass) {
        for (Connector connector : this.connectorMap.values()) {

            if (connectorClass.isInstance(connector)) {
                return true;
            }

        }

        return false;
    }

    protected Properties getSystemProperties() {
        return System.getProperties();
    }

}
