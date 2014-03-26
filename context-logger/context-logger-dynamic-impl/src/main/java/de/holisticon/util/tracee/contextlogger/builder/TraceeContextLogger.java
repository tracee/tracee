package de.holisticon.util.tracee.contextlogger.builder;

import de.holisticon.util.tracee.Tracee;
import de.holisticon.util.tracee.TraceeLogger;
import de.holisticon.util.tracee.contextlogger.*;
import de.holisticon.util.tracee.contextlogger.api.ImplicitContextData;
import de.holisticon.util.tracee.contextlogger.api.WrappedContextData;
import de.holisticon.util.tracee.contextlogger.builder.gson.TraceeGsonContextLogBuilder;
import de.holisticon.util.tracee.contextlogger.connector.LogConnector;
import de.holisticon.util.tracee.contextlogger.data.TypeToWrapper;
import de.holisticon.util.tracee.contextlogger.data.subdata.tracee.PassedContextDataProvider;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The main context logger class.
 * This class is used to generate context information
 * <p/>
 * Created by Tobias Gindler, holisticon AG on 14.03.14.
 */

public final class TraceeContextLogger {

    private static TraceeContextLogger instance;

    // Connector settings
    private static final Pattern KEY_MATCHER_PATTERN = Pattern.compile(TraceeContextLoggerConstants.SYSTEM_PROPERTY_CONTEXT_LOGGER_CONNECTOR_KEY_PATTERN);
    private static final String CONNECTOR_PROPERTY_GRABBER_PATTERN = TraceeContextLoggerConstants.SYSTEM_PROPERTY_CONNECTOR_PREFIX.replaceAll("\\.", "\\.") + "%s\\.(.*)";
    private static final TraceeLogger LOGGER = Tracee.getBackend().getLoggerFactory().getLogger(TraceeContextLogger.class);
    private static final Map<String, String> WELL_KNOW_CONNECTOR_MAPPINGS = new HashMap<String, String>();

    static {
        WELL_KNOW_CONNECTOR_MAPPINGS.put("HttpConnector", WellKnownConnectorClassNames.HTTP_CONNECTOR);
        WELL_KNOW_CONNECTOR_MAPPINGS.put(LogConnector.class.getName(), LogConnector.class.getCanonicalName());
    }

    // Context log builder
    private Map<Class, Class> classToWrapperMap = new ConcurrentHashMap<Class, Class>();
    private Map<ImplicitContext, Class> implicitContextClassMap = new ConcurrentHashMap<ImplicitContext, Class>();
    private List<TypeToWrapper> wrapperList;
    private TraceeGsonContextLogBuilder traceeGsonContextLogBuilder;


    //Connector
    private final Map<String, Connector> connectorMap = new HashMap<String, Connector>();

    TraceeContextLogger() {
        initContextDataCreator();
        initConnectors();
    }

    /**
     * Does the initialization stuff like Creating the lookup map and bind the wrapper classes.
     */
    private void initContextDataCreator() {
        wrapperList = TypeToWrapper.getTypeToWrapper();

        // now iterate over types and fill map
        for (TypeToWrapper wrapper : wrapperList) {
            classToWrapperMap.put(wrapper.getWrappedInstanceType(), wrapper.getWrapperType());
        }

        traceeGsonContextLogBuilder = new TraceeGsonContextLogBuilder();
        Set<Class> wrapperClasses = TypeToWrapper.findWrapperClasses();
        Set<ImplicitContextData> implicitContextWrapperClasses = TypeToWrapper.getImplicitWrappers();
        for (ImplicitContextData instance : implicitContextWrapperClasses) {
            implicitContextClassMap.put(instance.getImplicitContext(), instance.getClass());
            wrapperClasses.add(instance.getClass());
        }

        traceeGsonContextLogBuilder.setWrapperClasses(wrapperClasses);
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
     * tries to wrap a single instance into known wrapper class instances.
     *
     * @param instance
     * @return
     */
    private Object wrapInstance(Object instance) {

        if (instance == null) {
            return null;
        }

        // check for implicit instances
        if (instance instanceof ImplicitContext) {
            return createInstance(implicitContextClassMap.get(instance));
        }

        // now try to find instance type in known wrapper types map
        Class knownWrapperType = classToWrapperMap.get(instance.getClass());
        if (knownWrapperType != null) {
            return createInstance(knownWrapperType);
        }

        // now try to find instance type in TypeToWrapper List
        for (TypeToWrapper wrapper : wrapperList) {
            if (wrapper.getWrappedInstanceType().isAssignableFrom(instance.getClass())) {
                try {
                    WrappedContextData wrapperInstance = (WrappedContextData) createInstance(wrapper.getWrapperType());
                    wrapperInstance.setContextData(instance);

                    if (wrapperInstance != null) {
                        // add class to map for future usage
                        classToWrapperMap.put(instance.getClass(), wrapper.getWrapperType());
                    }

                    return wrapperInstance;
                } catch (Exception e) {
                    // continue
                    return null;
                }

            }
        }

        // if instance can't be wrapped pass instance as is
        return instance;
    }

    /**
     * Creates a new instance of the passed type via reflection.
     *
     * @param type the type of the new instance
     * @return a new instance of the passed type or null if an exception occurred  during the creation of the instance of if the passed type is null.
     */
    private Object createInstance(final Class type) {
        if (type != null) {
            try {
                return type.newInstance();
            } catch (Exception e) {
                // should not occur
            }
        }
        return null;
    }


    public static void logJsonWithMessagePrefix(String prefix, Object ... instancesToLog){
        getInstance().localSendErrorReportToConnectors(prefix, toJson(instancesToLog));
    }

    public static void logJson(Object ...  instancesToLog) {
        getInstance().localSendErrorReportToConnectors(null, toJson(instancesToLog));
    }

    /**
     * This static method is used to create the contextual logging String
     *
     * @param instancesToLog
     * @return
     */
    public static String toJson(Object... instancesToLog) {
        return getInstance().propagateToJson(instancesToLog);
    }

    private static TraceeContextLogger getInstance () {
        final TraceeContextLogger localInstance;
        if (instance == null) {

            localInstance = new TraceeContextLogger();
            instance = localInstance;

        } else {
            localInstance = instance;
        }
        return localInstance;
    }

    /**
     * This method handles the wrapping of the incoming object and passes them to the context toJson builder implementation.
     *
     * @param instancesToLog an array of objects to wrap
     * @return the contextual toJson information as a String
     */

    String propagateToJson(Object[] instancesToLog) {

        Object[] propagateArray = null;
        if (instancesToLog != null) {
            propagateArray = new Object[instancesToLog.length];

            for (int i = 0; i < instancesToLog.length; i++) {

                propagateArray[i] = wrapInstance(instancesToLog[i]);

            }

        }

        return traceeGsonContextLogBuilder.log(new PassedContextDataProvider(propagateArray));
    }


    /**
     * Send error report to all initialized connector instances.
     *
     * @param prefix the prefix to prepend at log output
     * @param json the context data to output
     */
    public void localSendErrorReportToConnectors(String prefix, String json) {

       for (Connector connector : this.connectorMap.values()) {

            // prevent
            if (LogConnector.class.isInstance(connector)) {
                connector.sendErrorReport(prefix != null ? prefix +  json : json);
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
     *
     * @param connectorName the name of the connector configuration
     * @return a Map containing all properties for a connector configuration name
     */
    Map<String, String> getPropertiesForConnectorConfigurationName(final String connectorName) {

        final Map<String, String> propertyMap = new HashMap<String, String>();

        final String patternString = String.format(CONNECTOR_PROPERTY_GRABBER_PATTERN, connectorName);
        final Pattern propertyGrabPattern = Pattern.compile(patternString);

        final Set<Map.Entry<Object, Object>> entries = System.getProperties().entrySet();

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
    Connector createConnector(final String connectorConfigurationName) {

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

}
