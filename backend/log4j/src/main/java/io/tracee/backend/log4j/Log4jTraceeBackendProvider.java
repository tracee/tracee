package io.tracee.backend.log4j;

import io.tracee.ThreadLocalHashSet;
import io.tracee.TraceeBackend;
import io.tracee.spi.TraceeBackendProvider;

import java.util.Set;

public class Log4jTraceeBackendProvider implements TraceeBackendProvider {

	private static final ThreadLocalHashSet<String> TRACEE_KEYS = new ThreadLocalHashSet<String>();

	private final Log4jTraceeBackend log4jTraceeBackend = new Log4jTraceeBackend(TRACEE_KEYS);

	@Override
	public final TraceeBackend provideBackend() {
		return log4jTraceeBackend;
	}
}
