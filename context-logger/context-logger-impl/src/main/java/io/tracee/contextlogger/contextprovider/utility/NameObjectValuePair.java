package io.tracee.contextlogger.contextprovider.utility;

import io.tracee.contextlogger.api.TraceeContextProvider;
import io.tracee.contextlogger.utility.TraceeContextLogAnnotationUtilities;

/**
 * Value class for JSON generation.
 * Created by Tobias Gindler, holisticon AG on 20.03.14.
 */
public class NameObjectValuePair extends NameValuePair<Object> {


    public NameObjectValuePair(final String name, final Object value) {
        super(name, value);
    }

    /**
     * Constructor which sets the name implicit depending on the passd value type.
     */
    public NameObjectValuePair(Object value) {
        super(getNameFromValueInstance(value), value);
    }

    /**
     * Gets the name depending on instance type.
     */
    protected static String getNameFromValueInstance(final Object instance) {
        if (instance == null) {
            return DEFAULT_NAME;
        }

        TraceeContextProvider annotation = TraceeContextLogAnnotationUtilities.getAnnotationFromType(instance);
        if (annotation != null) {
            return annotation.displayName();
        } else {
            return instance.getClass().getName();
        }
    }
}
