package de.holisticon.util.tracee.backend.threadlocalstore;

import de.holisticon.util.tracee.ThreadLocalHashSet;
import de.holisticon.util.tracee.TraceeBackend;
import de.holisticon.util.tracee.spi.TraceeBackendProvider;

/**
 * @author Daniel
 */
public class ThreadLocalTraceeBackendProvider implements TraceeBackendProvider {

    private static final ThreadLocalMap THREAD_LOCAL_MAP = new ThreadLocalMap();
    private static final ThreadLocalHashSet<String> TRACEE_KEYS = new ThreadLocalHashSet<String>();
    private static final MdcLikeThreadLocalMapAdapter THREAD_LOCAL_MAP_ADAPTER = new MdcLikeThreadLocalMapAdapter(THREAD_LOCAL_MAP);

    private final TraceeBackend traceeBackend = new ThreadLocalTraceeBackend(THREAD_LOCAL_MAP_ADAPTER, TRACEE_KEYS);

    @Override
    public final TraceeBackend provideBackend() {
        return traceeBackend;
    }


}
