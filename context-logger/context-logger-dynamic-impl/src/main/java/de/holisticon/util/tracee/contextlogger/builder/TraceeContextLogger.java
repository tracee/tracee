package de.holisticon.util.tracee.contextlogger.builder;

import de.holisticon.util.tracee.contextlogger.*;
import de.holisticon.util.tracee.contextlogger.api.ImplicitContextData;
import de.holisticon.util.tracee.contextlogger.api.WrappedContextData;
import de.holisticon.util.tracee.contextlogger.builder.gson.TraceeGsonContextLogBuilder;
import de.holisticon.util.tracee.contextlogger.data.TypeToWrapper;
import de.holisticon.util.tracee.contextlogger.data.subdata.tracee.PassedContextDataProvider;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The main context logger class.
 * This class is used to generate context information
 * <p/>
 * Created by Tobias Gindler, holisticon AG on 14.03.14.
 */

public final class TraceeContextLogger {

    private static TraceeContextLogger instance;

    private ConnectorFactory connectorsWrapper;

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
        connectorsWrapper = new ConnectorFactory();
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
        getInstance().connectorsWrapper.sendErrorReportToConnectors(prefix, toJson(instancesToLog));
    }

    public static void logJson(Object ...  instancesToLog) {
        getInstance().connectorsWrapper.sendErrorReportToConnectors(null, toJson(instancesToLog));
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



}
