package io.tracee;

public interface TraceeLoggerFactory {
	TraceeLogger getLogger(Class<?> clazz);
}
