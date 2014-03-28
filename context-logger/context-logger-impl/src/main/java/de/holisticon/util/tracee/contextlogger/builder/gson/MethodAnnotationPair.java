package de.holisticon.util.tracee.contextlogger.builder.gson;

import de.holisticon.util.tracee.contextlogger.api.TraceeContextLogProvider;
import de.holisticon.util.tracee.contextlogger.api.TraceeContextLogProviderMethod;
import de.holisticon.util.tracee.contextlogger.profile.Profile;
import de.holisticon.util.tracee.contextlogger.profile.ProfileSettings;

import java.lang.reflect.Method;

/**
 * Wrapper class for methods and their {@link de.holisticon.util.tracee.contextlogger.api.TraceeContextLogProviderMethod} annotations.
 * Used for sorting (by {@link de.holisticon.util.tracee.contextlogger.builder.gson.MethodAnnotationPairComparator}) and other tasks.
 * <p/>
 * Created by Tobias Gindler, holisticon AG on 14.03.14.
 */
public class MethodAnnotationPair {

    private final TraceeContextLogProviderMethod annotation;
    private final Method method;

    public MethodAnnotationPair(final Method method, final TraceeContextLogProviderMethod annotation) {
        this.method = method;
        this.annotation = annotation;
    }

    public boolean shouldBeProcessed (final ProfileSettings profileSettings) {

        TraceeContextLogProviderMethod annotation = this.getAnnotation();
        return annotation == null || annotation.propertyName().isEmpty() || profileSettings.getPropertyValue(annotation.propertyName());

    }

    public TraceeContextLogProviderMethod getAnnotation() {
        return annotation;
    }

    public Method getMethod() {
        return method;
    }


}
