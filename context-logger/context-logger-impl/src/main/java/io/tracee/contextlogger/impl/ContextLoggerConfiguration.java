package io.tracee.contextlogger.impl;

import io.tracee.contextlogger.api.CustomImplicitContextData;
import io.tracee.contextlogger.api.ImplicitContext;
import io.tracee.contextlogger.api.ImplicitContextData;
import io.tracee.contextlogger.contextprovider.TypeToWrapper;
import io.tracee.contextlogger.profile.Profile;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A Singleton that holds the static configuration data.
 * Created by Tobias Gindler, holisticon AG on 28.03.14.
 */
public class ContextLoggerConfiguration {

    private static ContextLoggerConfiguration contextLoggerConfiguration;

    private final Map<Class, Class> classToWrapperMap;
    private final Map<ImplicitContext, Class> implicitContextClassMap;
    private final List<TypeToWrapper> wrapperList;
    private final Set<Class> wrapperClasses;
    private final Profile profile;


    /**
     * Does the initialization stuff like Creating the lookup map and bind the wrapper classes.
     */
    public ContextLoggerConfiguration() {
        List<TypeToWrapper> tmpWrapperList = TypeToWrapper.getTypeToWrapper();

        Map<ImplicitContext, Class> tmpImplicitContextClassMap = new ConcurrentHashMap<ImplicitContext, Class>();
        Map<Class, Class> tmpClassToWrapperMap = new ConcurrentHashMap<Class, Class>();

        // now iterate over types and fill map
        for (TypeToWrapper wrapper : tmpWrapperList) {
            tmpClassToWrapperMap.put(wrapper.getWrappedInstanceType(), wrapper.getWrapperType());
        }

        Set<Class> tmpWrapperClasses = TypeToWrapper.findWrapperClasses();
        Set<ImplicitContextData> implicitContextWrapperClasses = TypeToWrapper.getImplicitContextDataProviders();
        for (ImplicitContextData instance : implicitContextWrapperClasses) {
            tmpImplicitContextClassMap.put(instance.getImplicitContext(), instance.getClass());
            tmpWrapperClasses.add(instance.getClass());
        }

        // must register custom data provider classes
        Set<CustomImplicitContextData> customImplicitContextDataProviderClasses = TypeToWrapper.getCustomImplicitDataProviders();
        for (CustomImplicitContextData instance : customImplicitContextDataProviderClasses) {
            tmpWrapperClasses.add(instance.getClass());
        }

        // Make collections immutable
        wrapperList = Collections.unmodifiableList(tmpWrapperList);
        implicitContextClassMap = Collections.unmodifiableMap(tmpImplicitContextClassMap);
        classToWrapperMap = Collections.unmodifiableMap(tmpClassToWrapperMap);
        wrapperClasses = Collections.unmodifiableSet(tmpWrapperClasses);

        profile = Profile.getCurrentProfile();
    }


    public static ContextLoggerConfiguration getOrCreateContextLoggerConfiguration() {
        if (contextLoggerConfiguration == null) {
            contextLoggerConfiguration = new ContextLoggerConfiguration();
        }
        return contextLoggerConfiguration;
    }

    /**
     * Gets an input class to context provider class map.
     *
     * @return An input class to context provider class map.
     */
    public Map<Class, Class> getClassToWrapperMap() {
        return classToWrapperMap;
    }

    /**
     * Gets all implicit context provider classes.
     *
     * @return All implicit context provider classes.
     */
    public Map<ImplicitContext, Class> getImplicitContextClassMap() {
        return implicitContextClassMap;
    }

    public List<TypeToWrapper> getWrapperList() {
        return wrapperList;
    }

    /**
     * Gets a set that contains all context provider classes.
     *
     * @return A set that contains all available context provider classes
     */
    public Set<Class> getWrapperClasses() {
        return wrapperClasses;
    }

    /**
     * Gets the default profile.
     *
     * @return The default profile.
     */
    public Profile getProfile() {
        return profile;
    }


}
