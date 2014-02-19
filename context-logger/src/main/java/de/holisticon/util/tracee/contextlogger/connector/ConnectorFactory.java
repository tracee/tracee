package de.holisticon.util.tracee.contextlogger.connector;


import de.holisticon.util.tracee.Tracee;
import de.holisticon.util.tracee.TraceeLogger;
import de.holisticon.util.tracee.contextlogger.TraceeContextLoggerConstants;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The connection Factory that initializes all connectors and handles the invocation of those.
 * Created by Tobias Gindler, holisticon AG on 14.02.14.
 */
public final class ConnectorFactory {

    private static ConnectorFactory instance = new ConnectorFactory();

    private final static Pattern KEY_MATCHER_PATTERN = Pattern.compile(TraceeContextLoggerConstants.SYSTEM_PROPERTY_CONTEXT_LOGGER_CONNECTOR_KEY_PATTERN);
    private final static String CONNECTOR_PROPERTY_GRABBER_PATTERN = TraceeContextLoggerConstants.SYSTEM_PROPERTY_CONNECTOR_PREFIX.replaceAll("\\.","\\.") + "%s\\.(.*)";

    private final static TraceeLogger LOGGER = Tracee.getBackend().getLoggerFactory().getLogger(ConnectorFactory.class);

    private final static Map<String,String> WELL_KNOW_CONNECTOR_MAPPINGS = new HashMap<String, String>();

    static {
        WELL_KNOW_CONNECTOR_MAPPINGS.put(HttpConnector.class.getName(), HttpConnector.class.getCanonicalName());
        WELL_KNOW_CONNECTOR_MAPPINGS.put(LogConnector.class.getName(), LogConnector.class.getCanonicalName());
    }

    private final Map<String,Connector> connectorMap = new HashMap<String, Connector>();



    /**
     * private Constructor. Handles initialization of connectors.
     */
    private ConnectorFactory () {

    }

    public static  ConnectorFactory getInstance () {
        return instance;
    }


    /**
     * Initializes all available connectors.
     */
    @SuppressWarnings("unused")
    private void init() {

        // first get all connector configuration Names
        Set<String> connectorConfigurationNames = this.getConnectorConfigurationNames();

        for (String connectorConfigurationName : connectorConfigurationNames) {

            Connector connector = this.createConnector(connectorConfigurationName);

            if (connector != null) {

                this.connectorMap.put(connectorConfigurationName, connector);

            }

        }

    }

    /**
     * Extracts all names for connector configurations from System properties.
     * @return a Set containing all connector configuration names
     */
    Set<String> getConnectorConfigurationNames() {

        Set<String> connectorNames = new HashSet<String>();

        Enumeration<Object> keyEnumeration = System.getProperties().keys();
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
     * @param connectorName the name of the connector configuration
     * @return a Map containing all properties for a connector configuration name
     */
    Map<String,String> getPropertiesForConnectorConfigurationName(final String connectorName) {

        final Map<String,String> propertyMap = new HashMap<String,String> ();

        final String patternString = String.format(CONNECTOR_PROPERTY_GRABBER_PATTERN,connectorName);
        final Pattern propertyGrabPattern = Pattern.compile(patternString);

        final Set<Map.Entry<Object,Object>> entries = System.getProperties().entrySet();

        for (Map.Entry<Object,Object> entry : entries) {
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
     * @param connectorConfigurationName the name of the connector configuration
     * @return the connector if it could be created and initialized without error, otherwise null
     */
    Connector createConnector (final String connectorConfigurationName) {

        Map<String,String> propertyMap = this.getPropertiesForConnectorConfigurationName(connectorConfigurationName);
        String type = propertyMap.get(TraceeContextLoggerConstants.SYSTEM_PROPERTY_CONTEXT_LOGGER_CONNECTOR_TYPE);

        // get canonical class name for well known connectors
        if (WELL_KNOW_CONNECTOR_MAPPINGS.containsKey(type)) {
            type = WELL_KNOW_CONNECTOR_MAPPINGS.get(type);
        }

        try {

            // try to create connector instance
            Connector connector = (Connector)Class.forName(type).newInstance();

            // now try to call init method
            connector.init(propertyMap);

            return connector;

        } catch (Exception e) {
            LOGGER.error("An error occurred while creating connector with name '" + connectorConfigurationName + "' of type '" + type + "'",e);
        }

        return null;
    }




}
