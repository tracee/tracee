package de.holisticon.util.tracee.contextlogger.api;

/**
 * Annotation to mark methods annotated by {@link de.holisticon.util.tracee.contextlogger.api.TraceeContextLogProviderMethod}
 * with return value of type {@link de.holisticon.util.tracee.contextlogger.data.subdata.NameStringValuePair}
 * to be printed inline.
 * Used for dynamic context informations.
 * Created by Tobias Gindler, holisticon AG on 17.03.14.
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD})
public @interface Flatten {
}
