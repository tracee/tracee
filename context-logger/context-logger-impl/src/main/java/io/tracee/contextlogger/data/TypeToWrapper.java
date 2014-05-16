package io.tracee.contextlogger.data;

import io.tracee.Tracee;
import io.tracee.contextlogger.TraceeContextLoggerConstants;
import io.tracee.contextlogger.api.ImplicitContextData;
import io.tracee.contextlogger.api.WrappedContextData;

import java.io.*;
import java.util.*;

/**
 * Class to store class to wrapper mapppings.
 */
public final class TypeToWrapper {

    private final static String[] RESOURCE_URLS = {
            TraceeContextLoggerConstants.WRAPPER_CLASS_INTERNAL_RESOURCE_URL,
            TraceeContextLoggerConstants.WRAPPER_CLASS_EXTERNAL_RESOURCE_URL
    };


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
     * Gets all internal and external implicit wrappers.
     * @return a set containing all available implicit wrappers
     */
    public static Set<ImplicitContextData> getImplicitWrappers() {



        Set<ImplicitContextData> implicitWrappers = new HashSet<ImplicitContextData>();

        for (final String resourceUrl : RESOURCE_URLS) {

            implicitWrappers.addAll(getImplicitWrappers(resourceUrl));

        }

        return implicitWrappers;
    }

    static Set<ImplicitContextData> getImplicitWrappers(final String resourceUrl) {
        Set<ImplicitContextData> result = new HashSet<ImplicitContextData>();


        BufferedReader bufferedReader = null;
        try {

            bufferedReader = new BufferedReader(
                    new InputStreamReader(
                            TypeToWrapper.class.getResourceAsStream(
                                    resourceUrl)
                    )
            );

            String line = bufferedReader.readLine();
            while (line != null) {

                try {

                    Class clazz = Class.forName(line);
                    if (ImplicitContextData.class.isAssignableFrom(clazz)) {
                        ImplicitContextData instance = (ImplicitContextData) clazz.newInstance();

                        result.add(instance);
                    }

                } catch (Exception e) {
                    // to be ignored
                } catch (NoClassDefFoundError error) {
                    // to be ignored
                }

                line = bufferedReader.readLine();
            }



        } catch (Exception e) {
            logError("Context logger - An error occurred while loading implicit type wrappers.", e);
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


    public static List<TypeToWrapper> getAvailableWrappers() {

        List<TypeToWrapper> wrappers = new ArrayList<TypeToWrapper>();

        for (final String resourceUrl : RESOURCE_URLS) {

            wrappers.addAll(getAvailableWrappers(resourceUrl));

        }

        return wrappers;
    }

    /**
     * Method to get all available wrappers.
     * @return all wrapping between wrapper classes and their wrapped types.
     */
    static List<TypeToWrapper> getAvailableWrappers(final String resourceUrl) {

        List<TypeToWrapper> result = new ArrayList<TypeToWrapper>();

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

                    Class clazz = Class.forName(line);

                    if (WrappedContextData.class.isAssignableFrom(clazz)) {
                        // try to create instance to get the wrapped type
                        WrappedContextData instance = (WrappedContextData) clazz.newInstance();

                        Class wrappedType = instance.getWrappedType();

                        result.add(new TypeToWrapper(wrappedType, clazz));
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

    private static void logError (final String message, final Throwable e) {
        Tracee.getBackend().getLoggerFactory().getLogger(TypeToWrapper.class).error(message, e);
    }

}
