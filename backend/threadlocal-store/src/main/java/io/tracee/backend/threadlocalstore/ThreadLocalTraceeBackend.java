package io.tracee.backend.threadlocalstore;

import io.tracee.MDCLikeTraceeBackend;
import io.tracee.ThreadLocalHashSet;
import io.tracee.TraceeLogger;
import io.tracee.TraceeLoggerFactory;

class ThreadLocalTraceeBackend extends MDCLikeTraceeBackend {

	private final ThreadLocalMap<String, String> threadLocalMap;

	public ThreadLocalTraceeBackend(ThreadLocalHashSet<String> traceeKeys) {
		super(traceeKeys, new TraceeLoggerFactory() {
			@Override
			public TraceeLogger getLogger(Class<?> clazz) {
				return new ThreadLocalTraceeLogger(clazz);
			}
		});
		this.threadLocalMap = new ThreadLocalMap<String, String>();
	}

	@Override
	protected String getFromMdc(String key) {
		return threadLocalMap.get().get(key);
	}

	@Override
	protected void putToMdc(String key, String value) {
		threadLocalMap.get().put(key, value);
	}

	@Override
	protected void removeFromMdc(String key) {
		threadLocalMap.get().remove(key);
	}

	ThreadLocalMap<String, String> getThreadLocalMap() {
		return threadLocalMap;
	}
}
