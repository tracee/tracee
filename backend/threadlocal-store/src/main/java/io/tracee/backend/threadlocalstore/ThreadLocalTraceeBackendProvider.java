package io.tracee.backend.threadlocalstore;

import io.tracee.TraceeBackend;
import io.tracee.spi.TraceeBackendProvider;

public class ThreadLocalTraceeBackendProvider implements TraceeBackendProvider {

	private final ThreadLocalTraceeBackend backend = new ThreadLocalTraceeBackend();

	@Override
	public final TraceeBackend provideBackend() {
		return backend;
	}

}
