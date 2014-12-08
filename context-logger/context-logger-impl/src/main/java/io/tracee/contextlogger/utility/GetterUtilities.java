package io.tracee.contextlogger.utility;

import java.lang.reflect.Method;

/**
 * Utility class for evaluating and processing getter methods.
 */
public class GetterUtilities {

    // possible prefixes for getter methods
    public static final String[] GETTER_PREFIXES = {"get", "is", "has"};

    /**
     * Checks whether the method name starts with a getter prefix.
     *
     * @param method the method to check for
     * @return true if the method name starts with eiher one of values stored in GETTER_PREFIXES
     */
    public static boolean isGetterMethod(final Method method) {
        if (method != null) {
            return isGetterMethod(method.getName());
        } else {
            return false;
        }
    }

    /**
     * Checks whether the method name starts with a getter prefix.
     *
     * @param methodName the method name to check for
     * @return true if the method name starts with eiher one of values stored in GETTER_PREFIXES
     */
    public static boolean isGetterMethod(final String methodName) {

        if (methodName != null) {

            for (String prefix : GETTER_PREFIXES) {
                if (methodName.startsWith(prefix)) {
                    return true;
                }
            }

        }

        return false;
    }

    /**
     * Strips getter prefix from method name.
     *
     * @param methodName the name of the method to be processed
     * @return the stripped method name with Decapitalized first letter or null if passed method name has no getter prefix.
     */
    public static String getFieldName(final String methodName) {

        if (isGetterMethod(methodName)) {
            return decapitalizeFirstCharOfString(stripGetterPrefix(methodName));
        }

        return null;
    }

    /**
     * Strips getter prefix from method name.
     *
     * @param method the method to be processed
     * @return the stripped method name with Decapitalized first letter or null if passed method is null or if the method name has no getter prefix.
     */
    public static String getFieldName(final Method method) {
        if (method != null) {
            return getFieldName(method.getName());
        } else {
            return null;
        }
    }

    /**
     * Gets the full qualified name of a field or non getter method.
     * Result has following format : "Full Qualified Classname"."field name for getter methods or method name for non getter methods".
     *
     * @param method The method to be processed
     * @return
     */
    public static String getFullQualifiedFieldName(final Class type, final Method method) {

        if (method != null) {

            // get the name of the type from passed type or method
            final String typeName;
            if (type != null) {
                typeName = type.getCanonicalName();
            } else {
                typeName = method.getDeclaringClass().getCanonicalName();
            }

            // get field name from getter method or method name for non getter methods
            final String fieldName;
            if (isGetterMethod(method.getName())) {
                fieldName = getFieldName(method.getName());
            } else {
                fieldName = method.getName();
            }

            return typeName + "." + fieldName;
        } else {
            return null;
        }

    }

    /**
     * Capitalizes first char of an input string.
     *
     * @param input the string to be processed;
     * @return The input string with the first capitalized char. Returns empty string, if passed input String is null or empty.
     */
    static String capitalizeFirstCharOfString(final String input) {
        if (input == null || input.length() == 0) {
            return "";
        } else if (input.length() == 1) {
            return input.toUpperCase();
        } else {
            return input.substring(0, 1).toUpperCase() + input.substring(1);
        }
    }

    /**
     * Decapitalizes first char of an input string.
     *
     * @param input the string to be processed;
     * @return The input string with the first capitalized char. Returns empty string, if passed input String is null or empty.
     */
    static String decapitalizeFirstCharOfString(final String input) {
        if (input == null || input.length() == 0) {
            return "";
        } else if (input.length() == 1) {
            return input.toLowerCase();
        } else {
            return input.substring(0, 1).toLowerCase() + input.substring(1);
        }
    }

    /**
     * Strips getter prefix from input string
     *
     * @param input the input string to be processed
     * @return the string stripped by getter prefix
     */
    static String stripGetterPrefix(final String input) {

        for (String prefix : GETTER_PREFIXES) {
            if (input.startsWith(prefix)) {
                return input.substring(prefix.length());
            }
        }

        return input;
    }

}
