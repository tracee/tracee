package io.tracee.backend.log4j2;

import io.tracee.ThreadLocalHashSet;
import io.tracee.TraceeBackend;
import io.tracee.spi.TraceeBackendProvider;

import java.util.Set;

public class Log4j2TraceeBackendProvider implements TraceeBackendProvider {

	private final Log4j2MdcLikeAdapter log4jMdcLikeAdapter = new Log4j2MdcLikeAdapter();
	private final ThreadLocal<Set<String>> traceeKeyStore = new ThreadLocalHashSet<String>();

	private final Log4j2TraceeBackend log4jTraceeBackend = new Log4j2TraceeBackend(log4jMdcLikeAdapter, traceeKeyStore);

	@Override
	public final TraceeBackend provideBackend() {
		return log4jTraceeBackend;
	}
}
