package de.holisticon.util.tracee.contextlogger.builder;

import de.holisticon.util.tracee.contextlogger.*;
import de.holisticon.util.tracee.contextlogger.api.WrappedContextData;
import de.holisticon.util.tracee.contextlogger.builder.gson.TraceeGsonContextLogBuilder;
import de.holisticon.util.tracee.contextlogger.data.TypeToWrapper;
import de.holisticon.util.tracee.contextlogger.data.subdata.tracee.PassedContextDataProvider;
import de.holisticon.util.tracee.contextlogger.profile.Profile;

import java.util.*;

/**
 * The main context logger class.
 * This class is used to generate context information
 * <p/>
 * Created by Tobias Gindler, holisticon AG on 14.03.14.
 */

public final class TraceeContextLogger implements ConfigBuilder, ContextLoggerBuilder , ContextLogger{

    private static TraceeContextLogger instance;

    private ConnectorFactory connectorsWrapper;

    // Context log builder
    private TraceeGsonContextLogBuilder traceeGsonContextLogBuilder;

    private final ContextLoggerConfiguration contextLoggerConfiguration;

    // manual configuaration
    private Profile profile;
    private Map<String,Boolean> manualContextOverrides = new HashMap<String, Boolean>();


    //Connector
    private final Map<String, Connector> connectorMap = new HashMap<String, Connector>();

    TraceeContextLogger() {

        // get the log configuration
        this.contextLoggerConfiguration = ContextLoggerConfiguration.getOrCreateContextLoggerConfiguration();
        initConnectors();

    }

    /**
     * Initializes all available connectors.
     */
    private void initConnectors() {
        connectorsWrapper = new ConnectorFactory();
    }

    public static ContextLoggerBuilder create () {
        return new TraceeContextLogger();
    }

    public static ContextLogger createDefault () {
        return create().build();
    }

    @Override
    public ContextLogger build () {
        traceeGsonContextLogBuilder = createGsonLogBuilder();
        return this;
    }


    @Override
    public ConfigBuilder enforceProfile(Profile profile) {
        this.profile = profile;
        return this;
    }

    @Override
    public ConfigBuilder enable(String... contexts) {
        fillManualContextOverrideMap(contexts, true);
        return this;
    }

    @Override
    public ConfigBuilder disable(String... contexts) {
        fillManualContextOverrideMap(contexts, false);
        return this;
    }

    @Override
    public ContextLoggerBuilder apply() {
        return (ContextLoggerBuilder) this;
    }

    @Override
    public ConfigBuilder config() {
        return (ConfigBuilder)this;
    }

    @Override
    public String createJson(Object... instancesToLog) {
        return this.propagateToContextLogBuilder(instancesToLog);
    }

    @Override
    public void logJson(Object ...  instancesToLog) {
        this.logJsonWithPrefixedMessage(null, instancesToLog);
    }

    @Override
    public void logJsonWithPrefixedMessage(String prefixedMessage, Object... instancesToLog) {
        this.connectorsWrapper.sendErrorReportToConnectors(prefixedMessage, createJson(instancesToLog));
    }

    /**
     * This method handles the wrapping of the incoming object and passes them to the context toJson builder implementation.
     *
     * @param instancesToLog an array of objects to wrap
     * @return the contextual toJson information as a String
     */

    String propagateToContextLogBuilder(Object[] instancesToLog) {

        Object[] propagateArray = null;
        if (instancesToLog != null) {
            propagateArray = new Object[instancesToLog.length];

            for (int i = 0; i < instancesToLog.length; i++) {

                propagateArray[i] = wrapInstance(instancesToLog[i]);

            }

        }

        return traceeGsonContextLogBuilder.logPassedContext(new PassedContextDataProvider(propagateArray));
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
            return createInstance(contextLoggerConfiguration.getImplicitContextClassMap().get(instance));
        }

        // now try to find instance type in known wrapper types map
        Class knownWrapperType = contextLoggerConfiguration.getClassToWrapperMap().get(instance.getClass());
        if (knownWrapperType != null) {
            return createInstance(knownWrapperType);
        }

        // now try to find instance type in TypeToWrapper List
        for (TypeToWrapper wrapper : contextLoggerConfiguration.getWrapperList()) {
            if (wrapper.getWrappedInstanceType().isAssignableFrom(instance.getClass())) {
                try {
                    WrappedContextData wrapperInstance = (WrappedContextData) createInstance(wrapper.getWrapperType());
                    wrapperInstance.setContextData(instance);

                    // THIS WON'T WORK ANYMORE BECAUSE MAP IS IMMUTABLE
                    //if (wrapperInstance != null) {
                    // add class to map for future usage
                    //classToWrapperMap.put(instance.getClass(), wrapper.getWrapperType());
                    //}

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
     * Creates a TraceeGsonContextLogBuilder instance which can be used for creating the log message.
     * @return An instance of TraceeGsonContextLogBuilder
     */
    private TraceeGsonContextLogBuilder createGsonLogBuilder () {

        TraceeGsonContextLogBuilder tmpTraceeGsonContextLogBuilder = new TraceeGsonContextLogBuilder();
        tmpTraceeGsonContextLogBuilder.setWrapperClasses(contextLoggerConfiguration.getWrapperClasses());
        tmpTraceeGsonContextLogBuilder.setManualContextOverrides(manualContextOverrides);
        tmpTraceeGsonContextLogBuilder.setProfile(this.profile != null ? profile : this.contextLoggerConfiguration.getProfile());

        return tmpTraceeGsonContextLogBuilder;
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

    /**
     * Adds passed contexts value pairs to manualContextOverrides.
     * @param contexts The property name of the context data.
     * @param value the value which should be set.
     */
    private void fillManualContextOverrideMap (final String[] contexts, final boolean value) {
        if (contexts != null) {

            for (String context : contexts) {

                if (!context.isEmpty()) {
                    this.manualContextOverrides.put(context,value);
                }

            }

        }
    }

}
