package de.holisticon.util.tracee.contextlogger.builder;

import de.holisticon.util.tracee.contextlogger.ImplicitContext;
import de.holisticon.util.tracee.contextlogger.api.WrappedContextData;
import de.holisticon.util.tracee.contextlogger.builder.gson.TraceeGsonContextLogBuilder;
import de.holisticon.util.tracee.contextlogger.data.TypeToWrapper;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The main context logger class.
 * This class is used to generate context information
 * <p/>
 * Created by Tobias Gindler, holisticon AG on 14.03.14.
 */

public final class TraceeContextLogger {

    private static TraceeContextLogger instance;

    private Map<Class, Class> classToWrapperMap = new ConcurrentHashMap<Class, Class>();
    private List<TypeToWrapper> wrapperList;
    private TraceeGsonContextLogBuilder traceeGsonContextLogBuilder;

    TraceeContextLogger() {
        init();
    }

    /**
     * Does the initialization stuff like Creating the lookup map and bind the wrapper classes.
     */
    private void init() {
        wrapperList = TypeToWrapper.getTypeToWrapper();

        // now iterate over types and fill map
        for (TypeToWrapper wrapper : wrapperList) {
            classToWrapperMap.put(wrapper.getWrappedInstanceType(), wrapper.getWrapperType());
        }

        traceeGsonContextLogBuilder = new TraceeGsonContextLogBuilder();
        traceeGsonContextLogBuilder.setWrapperClasses(TypeToWrapper.findWrapperClasses());
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
            return createInstance(((ImplicitContext) instance).getDeclaringClass());
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

    /**
     * This static method is used to create the contextual logging String
     *
     * @param instancesToLog
     * @return
     */
    public static String log(Object... instancesToLog) {

        final TraceeContextLogger localInstance;
        if (instance == null) {

            localInstance = new TraceeContextLogger();
            instance = localInstance;

        } else {
            localInstance = instance;
        }

        return localInstance.propagateLog(instancesToLog);
    }


    /**
     * This method handles the wrapping of the incoming object and passes them to the context log builder implementation.
     *
     * @param instancesToLog an array of objects to wrap
     * @return the contextual log information as a String
     */

    String propagateLog(Object[] instancesToLog) {

        Object[] propagateArray = null;
        if (instancesToLog != null) {
            propagateArray = new Object[instancesToLog.length];

            for (int i = 0; i < instancesToLog.length; i++) {

                propagateArray[i] = wrapInstance(instancesToLog[i]);

            }

        }

        return traceeGsonContextLogBuilder.log(propagateArray);
    }

}
