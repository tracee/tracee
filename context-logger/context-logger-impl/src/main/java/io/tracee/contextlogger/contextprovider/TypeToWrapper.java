package io.tracee.contextlogger.contextprovider;

import io.tracee.Tracee;
import io.tracee.contextlogger.TraceeContextLoggerConstants;
import io.tracee.contextlogger.api.CustomImplicitContextData;
import io.tracee.contextlogger.api.ImplicitContextData;
import io.tracee.contextlogger.api.WrappedContextData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Class to store class to wrapper mapppings.
 */
public final class TypeToWrapper {

    private final static List<String> RESOURCE_URLS = Collections.unmodifiableList(Arrays.asList(
            TraceeContextLoggerConstants.WRAPPER_CONTEXT_PROVIDER_INTERNAL_RESOURCE_URL,
            TraceeContextLoggerConstants.WRAPPER_CONTEXT_PROVIDER_CUSTOM_RESOURCE_URL));

    private static List<TypeToWrapper> typeToWrapperList;

    private final Class wrappedInstanceType;
    private final Class wrapperType;

    public TypeToWrapper(final Class wrappedInstanceType, final Class wrapperType) {
        this.wrappedInstanceType = wrappedInstanceType;
        this.wrapperType = wrapperType;
    }

    public Class getWrappedInstanceType() {
        return wrappedInstanceType;
    }

    public Class getWrapperType() {
        return wrapperType;
    }


    public static Set<Class> getAllWrappedClasses() {

        final List<TypeToWrapper> localTypeToWrapperList = getTypeToWrapper();

        Set<Class> resultList = new HashSet<Class>();

        if (localTypeToWrapperList != null) {
            for (TypeToWrapper typeToWrapper : localTypeToWrapperList) {
                resultList.add(typeToWrapper.getWrappedInstanceType());
            }
        }

        return resultList;

    }

    public static List<TypeToWrapper> getTypeToWrapper() {
        if (typeToWrapperList == null) {
            typeToWrapperList = getAvailableWrappers();
        }
        return typeToWrapperList;
    }

    /**
     * Gets a list with all wrapper classes.
     *
     * @return a list with all wrapper classes
     */
    public static Set<Class> findWrapperClasses() {

        final List<TypeToWrapper> localTypeToWrapperList = getTypeToWrapper();

        Set<Class> resultList = new HashSet<Class>();

        if (localTypeToWrapperList != null) {
            for (TypeToWrapper typeToWrapper : localTypeToWrapperList) {
                resultList.add(typeToWrapper.getWrapperType());
            }
        }

        return resultList;

    }

    /**
     * Gets all internal implicit data providers.
     *
     * @return a set containing all available implicit wrappers
     */
    public static Set<ImplicitContextData> getImplicitContextDataProviders() {
        return getImplicitWrappers(ImplicitContextData.class, TraceeContextLoggerConstants.IMPLICIT_CONTEXT_PROVIDER_CLASS_INTERNAL_RESOURCE_URL);
    }

    /**
     * Gets all custom implicit wrappers.
     *
     * @return a set containing all available implicit wrappers
     */
    public static Set<CustomImplicitContextData> getCustomImplicitDataProviders() {
        return getImplicitWrappers(CustomImplicitContextData.class, TraceeContextLoggerConstants.IMPLICIT_CONTEXT_PROVIDER_CLASS_CUSTOM_RESOURCE_URL);
    }

    /**
     * Generic function to get a implicit data provider classes from resource files.
     *
     * @param type        the type of implicit context data provider to look for
     * @param resourceUrl the resource file url process
     * @param <T>         The generic type of implicit data provider either {@link io.tracee.contextlogger.api.CustomImplicitContextData} or {@link io.tracee.contextlogger.api.ImplicitContextData}
     * @return a set that contains all context provider type the were found
     */
    static <T> Set<T> getImplicitWrappers(final Class<T> type, final String resourceUrl) {
        final Set<T> result = new HashSet<T>();

        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(
                    new InputStreamReader(TypeToWrapper.class.getResourceAsStream(resourceUrl))
            );

            String line;
            while ((line = bufferedReader.readLine()) != null) {

                try {
                    final Class<?> clazz = Class.forName(line);
                    if (type.isAssignableFrom(clazz)) {
                        T instance = type.cast(clazz.newInstance());
                        result.add(instance);
                    }
                } catch (Exception e) {
                    // to be ignored
                } catch (NoClassDefFoundError error) {
                    // to be ignored
                }
            }
        } catch (Exception e) {
            logError("Context logger - An error occurred while loading implicit type wrappers.", e);
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ignored) {
                    // ignore
                }
            }
        }

        return result;
    }


    public static List<TypeToWrapper> getAvailableWrappers() {

        final List<TypeToWrapper> wrappers = new ArrayList<TypeToWrapper>();

        for (final String resourceUrl : RESOURCE_URLS) {
            wrappers.addAll(getAvailableWrappers(resourceUrl));
        }

        return wrappers;
    }

    /**
     * Method to get all available wrappers.
     *
     * @return all wrapping between wrapper classes and their wrapped types.
     */
    static List<TypeToWrapper> getAvailableWrappers(final String resourceUrl) {

        final List<TypeToWrapper> result = new ArrayList<TypeToWrapper>();

        BufferedReader bufferedReader = null;
        try {

            bufferedReader = new BufferedReader(
                    new InputStreamReader(
                            TypeToWrapper.class.getResourceAsStream(resourceUrl)
                    )
            );

            String line = bufferedReader.readLine();
            while (line != null) {

                try {
                    Class<?> clazz = Class.forName(line);

                    if (WrappedContextData.class.isAssignableFrom(clazz)) {
                        // try to create instance to get the wrapped type
                        final WrappedContextData instance = (WrappedContextData) clazz.newInstance();
                        result.add(new TypeToWrapper(instance.getWrappedType(), clazz));
                    }

                } catch (Exception e) {
                    // to be ignored
                } catch (NoClassDefFoundError error) {
                    // to be ignored
                }

                line = bufferedReader.readLine();
            }
        } catch (Exception e) {
            logError("Context logger - An error occurred while loading explicit type wrappers.", e);

        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e1) {
                    // ignore
                }
            }
        }

        return result;
    }

    private static void logError(final String message, final Throwable e) {
        Tracee.getBackend().getLoggerFactory().getLogger(TypeToWrapper.class).error(message, e);
    }
}
