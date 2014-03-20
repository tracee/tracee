package de.holisticon.util.tracee.contextlogger.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to mark classes that can be processed via the {@link de.holisticon.util.tracee.contextlogger.builder.gson.TraceeGenericGsonSerializer}.
 * Created by Tobias Gindler, holisticon AG on 14.03.14.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE})
public @interface TraceeContextLogProvider {

    String displayName();
    int order() default 100;

}
