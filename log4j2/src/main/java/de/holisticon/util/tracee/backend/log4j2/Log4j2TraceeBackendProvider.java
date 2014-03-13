package de.holisticon.util.tracee.backend.log4j2;

import de.holisticon.util.tracee.ThreadLocalHashSet;
import de.holisticon.util.tracee.TraceeBackend;
import de.holisticon.util.tracee.spi.TraceeBackendProvider;

import java.util.Set;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
public class Log4j2TraceeBackendProvider implements TraceeBackendProvider {

	private final Log4j2MdcLikeAdapter log4jMdcLikeAdapter = new Log4j2MdcLikeAdapter();
	private final ThreadLocal<Set<String>> traceeKeyStore = new ThreadLocalHashSet<String>();

	private final Log4j2TraceeBackend log4jTraceeBackend = new Log4j2TraceeBackend(log4jMdcLikeAdapter, traceeKeyStore);

	@Override
	public final TraceeBackend provideBackend() {
		return log4jTraceeBackend;
	}
}
