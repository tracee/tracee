package io.tracee;

import io.tracee.spi.TraceeBackendProvider;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.AbstractSet;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.WeakHashMap;

class BackendProviderResolver {

	// We should use weak references for our cache. otherwise we block class unloading.
	// This map is updated by the "copyOnWrite"-pattern. Don't update this map directly!
	private static volatile Map<ClassLoader, Set<TraceeBackendProvider>> providersPerClassloader = new WeakHashMap<ClassLoader, Set<TraceeBackendProvider>>();

	/**
	 * Find correct backend provider for the current context classloader. If no context classloader is available, a
	 * fallback with the classloader of this resolver class is taken
	 *
	 * @return A bunch of TraceeBackendProvider registered and available in the current classloader
	 */
	public Set<TraceeBackendProvider> getBackendProviders() {
		// Create a working copy of Cache. Reference is updated upon cache update.
		final Map<ClassLoader, Set<TraceeBackendProvider>> cacheCopy = providersPerClassloader;

		// Try to determine TraceeBackendProvider by context classloader. Fallback: use classloader of class.
		final Set<TraceeBackendProvider> providerFromContextClassLoader = getTraceeProviderFromClassloader(cacheCopy,
				GetClassLoader.fromContext());
		if (!providerFromContextClassLoader.isEmpty()) {
			return providerFromContextClassLoader;
		} else {
			return getTraceeProviderFromClassloader(cacheCopy, GetClassLoader.fromClass(BackendProviderResolver.class));
		}
	}

	/**
	 * Loads the default implementation
	 */
	Set<TraceeBackendProvider> getDefaultTraceeBackendProvider() {
		try {
			final ClassLoader classLoader = GetClassLoader.fromContext();
			final Class<?> slf4jTraceeBackendProviderClass = Class.forName("io.tracee.backend.slf4j.Slf4jTraceeBackendProvider", true, classLoader);
			final TraceeBackendProvider instance = (TraceeBackendProvider) slf4jTraceeBackendProviderClass.newInstance();
			updatedCache(classLoader, Collections.singleton(instance));
			return Collections.singleton(instance);
		} catch (ClassNotFoundException cnfe) {
			return Collections.emptySet();
		} catch (InstantiationException e) {
			return Collections.emptySet();
		} catch (IllegalAccessException e) {
			return Collections.emptySet();
		} catch (ClassCastException e) {
			return Collections.emptySet();
		}
	}

	/**
	 * Search for TraceeBackendProvider in the given classloader. The result is stored in a cache with the classloader
	 * as (weak) key. If no backendProvider could be found a special type of collection is stored in cache and is returned.
	 *
	 * @param cacheCopy   Working copy of the current cache (copy-on-write-cache)
	 * @param classLoader the classloader we've to search for TraceeBackendProvider
	 * @return A BackendProviderSet if we found at least one provider. Otherwise we return an EmptyBackendProviderSet.
	 */
	private Set<TraceeBackendProvider> getTraceeProviderFromClassloader(
			final Map<ClassLoader, Set<TraceeBackendProvider>> cacheCopy,
			final ClassLoader classLoader) {
		// use cache to get TraceeBackendProvider or empty results from old lookups
		Set<TraceeBackendProvider> classLoaderProviders = cacheCopy.get(classLoader);
		if (isLookupNeeded(classLoaderProviders)) {
			classLoaderProviders = loadProviders(classLoader);
			updatedCache(classLoader, classLoaderProviders);
		}

		return classLoaderProviders;
	}

	/*
	 * Helper method for #getTraceeProviderFromClassloader
	 * We do a lookup / return true if result is null and when the result is not an instance of EmptyBackendProviderSet and empty.
	 * In the last case the garbage collector kicked out our resolvers and we've to recreate them
	 */
	boolean isLookupNeeded(Set<TraceeBackendProvider> classLoaderProviders) {
		return classLoaderProviders == null || !(classLoaderProviders instanceof EmptyBackendProviderSet) && classLoaderProviders.isEmpty();
	}

	/*
	 * Helper method to update the static class cache
	 */
	private void updatedCache(final ClassLoader classLoader, final Set<TraceeBackendProvider> provider) {
		final Map<ClassLoader, Set<TraceeBackendProvider>> copyOnWriteMap = new WeakHashMap<ClassLoader, Set<TraceeBackendProvider>>(providersPerClassloader);
		if (!provider.isEmpty()) {
			copyOnWriteMap.put(classLoader, new BackendProviderSet(provider));
		} else {
			copyOnWriteMap.put(classLoader, new EmptyBackendProviderSet());
		}
		providersPerClassloader = copyOnWriteMap;
	}

	/**
	 * Loads the list of providers with the java ServiceLoader.<br />
	 * <p>Test information:<br />
	 * I tried to implement a test for such method. But it's really complicated:
	 * * We have to implement an own "mocked" classloader that simulates ClassLoader#getResources() calls. The returned list of URLs
	 * is opened by the ServiceLoader.<br />
	 * * Because we couldn't simulate N different loader scenarios we should return URLs with an own URLStreamHandler.
	 * Every StreamHandler returns a text InputStream on the getInputStream-Method and return a text that points to a classname.<br />
	 * * Our mocked classloader could/should simulate such loader classes<br />
	 * <br />
	 * Due such cases I reviewed the code and keep it untested :-(
	 * <p/>
	 * </p>
	 *
	 * @param classloader the classloader that is searched for TraceeBackendProvider services
	 * @return A list of available TraceeBackendProvider
	 */
	private Set<TraceeBackendProvider> loadProviders(ClassLoader classloader) {
		final ServiceLoader<TraceeBackendProvider> loader = ServiceLoader.load(TraceeBackendProvider.class, classloader);
		final Iterator<TraceeBackendProvider> providerIterator = loader.iterator();
		final Set<TraceeBackendProvider> traceeProvider = new HashSet<TraceeBackendProvider>();
		while (providerIterator.hasNext()) {
			try {
				traceeProvider.add(providerIterator.next());
			} catch (ServiceConfigurationError e) {
				// ignore, because it can happen when multiple providers are present and some of
				// them are not class loader compatible with our API.
			}
		}
		return traceeProvider;
	}

	static final class GetClassLoader implements PrivilegedAction<ClassLoader> {
		private final Class<?> clazz;

		private GetClassLoader(final Class<?> clazz) {
			this.clazz = clazz;
		}

		public ClassLoader run() {
			if (clazz != null) {
				return clazz.getClassLoader();
			} else {
				return Thread.currentThread().getContextClassLoader();
			}
		}

		public static ClassLoader fromContext() {
			return doPrivileged(new GetClassLoader(null));
		}

		public static ClassLoader fromClass(Class<?> clazz) {
			if (clazz == null) {
				throw new IllegalArgumentException("Class is null");
			}
			return doPrivileged(new GetClassLoader(clazz));
		}

		private static ClassLoader doPrivileged(GetClassLoader action) {
			if (System.getSecurityManager() != null) {
				return AccessController.doPrivileged(action);
			} else {
				return action.run();
			}
		}
	}

	static final class EmptyBackendProviderSet extends AbstractSet<TraceeBackendProvider> {

		@Override
		public Iterator<TraceeBackendProvider> iterator() {
			return Collections.<TraceeBackendProvider>emptyList().iterator();
		}

		@Override
		public int size() {
			return 0;
		}
	}
}
