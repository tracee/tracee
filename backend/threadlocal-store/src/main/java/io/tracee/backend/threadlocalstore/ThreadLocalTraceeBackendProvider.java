package io.tracee.backend.threadlocalstore;

import io.tracee.ThreadLocalHashSet;
import io.tracee.TraceeBackend;
import io.tracee.spi.TraceeBackendProvider;

public class ThreadLocalTraceeBackendProvider implements TraceeBackendProvider {

	private static final ThreadLocalHashSet<String> TRACEE_KEYS = new ThreadLocalHashSet<String>();

	private final TraceeBackend traceeBackend = new ThreadLocalTraceeBackend(TRACEE_KEYS);

	@Override
	public final TraceeBackend provideBackend() {
		return traceeBackend;
	}


}
