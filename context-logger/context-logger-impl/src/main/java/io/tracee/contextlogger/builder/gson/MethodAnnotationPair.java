package io.tracee.contextlogger.builder.gson;

import io.tracee.contextlogger.api.TraceeContextProviderMethod;
import io.tracee.contextlogger.profile.ProfileSettings;

import java.lang.reflect.Method;

/**
 * Wrapper class for methods and their {@link io.tracee.contextlogger.api.TraceeContextProviderMethod} annotations.
 * Used for sorting (by {@link io.tracee.contextlogger.builder.gson.MethodAnnotationPairComparator}) and other tasks.
 * <p/>
 * Created by Tobias Gindler, holisticon AG on 14.03.14.
 */
public class MethodAnnotationPair {

    private final TraceeContextProviderMethod annotation;
    private final Method method;

    public MethodAnnotationPair(final Method method, final TraceeContextProviderMethod annotation) {
        this.method = method;
        this.annotation = annotation;
    }

    /**
     * Determines if a method should be processed. This will either be if no
	 * {@link io.tracee.contextlogger.api.TraceeContextProviderMethod} annotation
     * is present or if an empty property name is defined in the annotation or if passed profileSettings are null
     * or if the property name is disabled in the {@link io.tracee.contextlogger.profile.ProfileSettings}.
     *
	 * @param profileSettings The profile sttings object which should be used to check against.
     * @return true if the result of the method should be processed, otherwise false.
     */
    public boolean shouldBeProcessed(final ProfileSettings profileSettings) {

        return annotation == null || annotation.propertyName().isEmpty()
				|| profileSettings == null || profileSettings.getPropertyValue(annotation.propertyName());

    }

    public TraceeContextProviderMethod getAnnotation() {
        return annotation;
    }

    public final Method getMethod() {
        return method;
    }


}
