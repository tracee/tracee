package de.holisticon.util.tracee.contextlogger.watchdog;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark neuralgic points in application to for which context logging in case of an exception.
 * Created by Tobias Gindler, holisticon AG on 16.02.14.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.METHOD, ElementType.TYPE })
public @interface Watchdog {
    String id() default "";
    boolean suppressThrowsExceptions() default false;
    boolean isActive() default true;
}
