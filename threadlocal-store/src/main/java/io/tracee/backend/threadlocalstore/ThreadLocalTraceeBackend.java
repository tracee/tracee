package io.tracee.backend.threadlocalstore;

import io.tracee.*;

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
