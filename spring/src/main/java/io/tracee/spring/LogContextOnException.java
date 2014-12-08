package io.tracee.spring;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * If a method with this annotation throws an exception the context gets logged on error level.
 *
 * @author <a href="carl.duevel@holisticon.de">Carl Anders Düvel</a>
 */
@Target(ElementType.METHOD)
public @interface LogContextOnException {
}
