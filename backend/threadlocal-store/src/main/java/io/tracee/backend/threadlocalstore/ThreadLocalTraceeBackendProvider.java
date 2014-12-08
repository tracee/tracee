package io.tracee.backend.threadlocalstore;

import io.tracee.ThreadLocalHashSet;
import io.tracee.TraceeBackend;
import io.tracee.spi.TraceeBackendProvider;

public class ThreadLocalTraceeBackendProvider implements TraceeBackendProvider {

	private static final ThreadLocalMap<String, String> THREAD_LOCAL_MAP = new ThreadLocalMap<String, String>();
	private static final ThreadLocalHashSet<String> TRACEE_KEYS = new ThreadLocalHashSet<String>();
	private static final MdcLikeThreadLocalMapAdapter THREAD_LOCAL_MAP_ADAPTER = new MdcLikeThreadLocalMapAdapter(THREAD_LOCAL_MAP);

	private final TraceeBackend traceeBackend = new ThreadLocalTraceeBackend(THREAD_LOCAL_MAP_ADAPTER, TRACEE_KEYS);

	@Override
	public final TraceeBackend provideBackend() {
		return traceeBackend;
	}


}
