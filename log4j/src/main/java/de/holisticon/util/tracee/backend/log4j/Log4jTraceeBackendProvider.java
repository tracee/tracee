package de.holisticon.util.tracee.backend.log4j;

import de.holisticon.util.tracee.ThreadLocalHashSet;
import de.holisticon.util.tracee.TraceeBackend;
import de.holisticon.util.tracee.spi.TraceeBackendProvider;

import java.util.Set;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
public class Log4jTraceeBackendProvider implements TraceeBackendProvider {

	private final Log4jMdcLikeAdapter log4jMdcLikeAdapter = new Log4jMdcLikeAdapter();
	private final ThreadLocal<Set<String>> traceeKeyStore = new ThreadLocalHashSet<String>();

	private final Log4jTraceeBackend log4jTraceeBackend = new Log4jTraceeBackend(log4jMdcLikeAdapter, traceeKeyStore);

	@Override
	public final TraceeBackend provideBackend() {
		return log4jTraceeBackend;
	}
}
