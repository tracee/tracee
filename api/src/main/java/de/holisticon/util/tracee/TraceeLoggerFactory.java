package de.holisticon.util.tracee;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
public interface TraceeLoggerFactory {
	TraceeLogger getLogger(Class<?> clazz);
}
