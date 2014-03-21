package de.holisticon.util.tracee.contextlogger.builder.gson;

import de.holisticon.util.tracee.contextlogger.api.TraceeContextLogProviderMethod;

import java.lang.reflect.Method;

/**
 * Wrapper class for methods and their {@link de.holisticon.util.tracee.contextlogger.api.TraceeContextLogProviderMethod} annotations.
 * Used for sorting (by {@link de.holisticon.util.tracee.contextlogger.builder.gson.MethodAnnotationPairComparator}) and other tasks.
 * <p/>
 * Created by Tobias Gindler, holisticon AG on 14.03.14.
 */
public final class MethodAnnotationPair {

    private final TraceeContextLogProviderMethod annotation;
    private final Method method;

    public MethodAnnotationPair(final Method method, final TraceeContextLogProviderMethod annotation) {
        this.method = method;
        this.annotation = annotation;
    }

    public TraceeContextLogProviderMethod getAnnotation() {
        return annotation;
    }

    public Method getMethod() {
        return method;
    }


}
