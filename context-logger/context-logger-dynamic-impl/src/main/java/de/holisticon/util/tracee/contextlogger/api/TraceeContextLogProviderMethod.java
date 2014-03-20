package de.holisticon.util.tracee.contextlogger.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to mark methods that provide context information for context logging.
 * This annotation must reside in classes that are annotated with the
 * {@link TraceeContextLogProvider} annotation.
 *
 * Created by Tobias Gindler, holisticon AG on 14.03.14.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD})
public @interface TraceeContextLogProviderMethod {

    String displayName();
    String propertyName();
    int order() default 0;

}
