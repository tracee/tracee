package de.holisticon.util.tracee.backend.threadlocalstore;

import de.holisticon.util.tracee.*;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
class ThreadLocalTraceeBackend extends MDCLikeTraceeBackend {

    public ThreadLocalTraceeBackend(final MDCLike mdcLike, ThreadLocalHashSet<String> traceeKeys) {
        super(mdcLike, traceeKeys, new TraceeLoggerFactory() {
			@Override
			public TraceeLogger getLogger(Class<?> clazz) {
				return new ThreadLocalTraceeLogger(clazz);
			}
		});
    }

}
