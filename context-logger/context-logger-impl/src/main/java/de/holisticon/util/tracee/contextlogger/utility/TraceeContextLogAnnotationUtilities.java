package de.holisticon.util.tracee.contextlogger.utility;

import de.holisticon.util.tracee.contextlogger.api.Flatten;
import de.holisticon.util.tracee.contextlogger.api.TraceeContextLogProvider;
import de.holisticon.util.tracee.contextlogger.api.TraceeContextLogProviderMethod;
import de.holisticon.util.tracee.contextlogger.builder.gson.MethodAnnotationPair;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility functions for getting Annotations from Annotated classes and methods.
 * Created by Tobias Gindler, holisticon AG on 14.03.14.
 */
public final class TraceeContextLogAnnotationUtilities {

    @SuppressWarnings("unused")
    private TraceeContextLogAnnotationUtilities() {
        // hide constructor
    }

    /**
     * gets the {@link de.holisticon.util.tracee.contextlogger.api.TraceeContextLogProvider} annotation of passed instance.
     *
     * @param instance the instance to get the annotation from
     * @return the {@link de.holisticon.util.tracee.contextlogger.api.TraceeContextLogProvider} annotation if present, otherwise null
     */
    public static TraceeContextLogProvider getAnnotationFromType(final Object instance) {
        if (instance == null) {
            return null;
        }
        return instance.getClass().getAnnotation(TraceeContextLogProvider.class);
    }


    /**
     * Extracts all {@link de.holisticon.util.tracee.contextlogger.api.TraceeContextLogProviderMethod} annotated methods of the passed instance.
     *
     * @param instance the instance to check for
     * @return a list containing all annotated methods of an instance
     */
    public static List<MethodAnnotationPair> getAnnotatedMethodsOfInstance(final Object instance) {

        List<MethodAnnotationPair> result = new ArrayList<MethodAnnotationPair>();


        for (Method method : instance.getClass().getDeclaredMethods()) {

            if (checkIsPublic(method) && checkMethodHasNoParameters(method) && checkMethodHasNonVoidReturnType(method)) {

                TraceeContextLogProviderMethod annotation = method.getAnnotation(TraceeContextLogProviderMethod.class);

                // check if method has no parameters and is annotated
                if (annotation != null) {
                    result.add(new MethodAnnotationPair(method, annotation));
                }

            }

        }

        return result;

    }

    /**
     * Checks whether the passsed method has no parameters.
     *
     * @param method the method to check
     * @return true if the method has no parameters, otherwise false
     */
    public static boolean checkMethodHasNoParameters(final Method method) {

        return method == null || method.getParameterTypes().length == 0;

    }

    /**
     * Checks whether the passsed method has a non void return value.
     *
     * @param method the method to check
     * @return true if the method has a non void return type, otherwise false
     */
    public static boolean checkMethodHasNonVoidReturnType(final Method method) {

        if (method == null) {
            return false;
        }
        try {

            return !(Void.TYPE == method.getReturnType());

        } catch (Exception e) {
            return false;
        }

    }

    public static boolean checkIsPublic(final Method method) {
        return method != null && Modifier.isPublic(method.getModifiers());
    }

    public static boolean isFlatable(final Method method) {

        return method != null && method.getAnnotation(Flatten.class) != null;

    }


}
