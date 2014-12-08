package io.tracee.backend.log4j;

import io.tracee.ThreadLocalHashSet;
import io.tracee.TraceeBackend;
import io.tracee.spi.TraceeBackendProvider;

import java.util.Set;

public class Log4jTraceeBackendProvider implements TraceeBackendProvider {

	private final Log4jMdcLikeAdapter log4jMdcLikeAdapter = new Log4jMdcLikeAdapter();
	private final ThreadLocal<Set<String>> traceeKeyStore = new ThreadLocalHashSet<String>();

	private final Log4jTraceeBackend log4jTraceeBackend = new Log4jTraceeBackend(log4jMdcLikeAdapter, traceeKeyStore);

	@Override
	public final TraceeBackend provideBackend() {
		return log4jTraceeBackend;
	}
}
