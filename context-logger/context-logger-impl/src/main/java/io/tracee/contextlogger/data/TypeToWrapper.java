package io.tracee.contextlogger.data;

import io.tracee.contextlogger.TraceeContextLoggerConstants;
import io.tracee.contextlogger.api.ImplicitContextData;
import io.tracee.contextlogger.api.WrappedContextData;

import java.io.*;
import java.util.*;

/**
 * Class to store class to wrapper mapppings.
 * Created by Tobias Gindler, holisticon AG on 20.03.14.
 */
public final class TypeToWrapper {

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

    public static Set<ImplicitContextData> getImplicitWrappers() {
        Set<ImplicitContextData> result = new HashSet<ImplicitContextData>();


        BufferedReader bufferedReader = null;
        try {

            bufferedReader = new BufferedReader(
                    new InputStreamReader(
                            TypeToWrapper.class.getResourceAsStream(
                                    TraceeContextLoggerConstants.WRAPPER_CLASS_RESOURCE_URL)
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

            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

        }


        return result;
    }

    /**
     * Class to get all available wrappers.
     * @return all wrapping between wrapper classes and their wrapped types.
     */
    static List<TypeToWrapper> getAvailableWrappers() {

        List<TypeToWrapper> result = new ArrayList<TypeToWrapper>();

        BufferedReader bufferedReader = null;
        try {

            bufferedReader = new BufferedReader(
                    new InputStreamReader(
                            TypeToWrapper.class.getResourceAsStream(
                                    TraceeContextLoggerConstants.WRAPPER_CLASS_RESOURCE_URL)
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

            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

        }


        return result;

    }



}
