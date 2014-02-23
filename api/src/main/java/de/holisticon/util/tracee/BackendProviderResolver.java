package de.holisticon.util.tracee;

import de.holisticon.util.tracee.spi.TraceeBackendProvider;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.*;

/**
 * @author Sven Bunge, Holisticon AG
 */
class BackendProviderResolver {

	// We should use weak references for our cache. otherwise we block class unloading.
	private volatile static Map<ClassLoader, Set<TraceeBackendProvider>> PROVIDERS_PER_CLASSLOADER =
			new WeakHashMap<ClassLoader, Set<TraceeBackendProvider>>();

	public Set<TraceeBackendProvider> getBackendProviders() {
		// Create a working copy of Cache. Reference is updated upon cache update.
		final Map<ClassLoader, Set<TraceeBackendProvider>> cacheCopy = PROVIDERS_PER_CLASSLOADER;

		// Try to determine TraceeBackendProvider by context classloader. Fallback: use classloader of class.
		Set<TraceeBackendProvider> providerFromContextClassLoader = getTraceeProviderFromClassloader(cacheCopy, GetClassLoader.fromContext());
		if (!providerFromContextClassLoader.isEmpty()) {
			return providerFromContextClassLoader;
		} else {
			return getTraceeProviderFromClassloader(cacheCopy, GetClassLoader.fromClass(BackendProviderResolver.class));
		}
	}

	private Set<TraceeBackendProvider> getTraceeProviderFromClassloader(final Map<ClassLoader, Set<TraceeBackendProvider>> cacheCopy,
																																			final ClassLoader classLoader) {
		// use cache to get TraceeBackendProvider or empty results from old lookups
		Set<TraceeBackendProvider> contextClassLoaderProviders = cacheCopy.get(classLoader);
		// if null we have to lookup the provider for given classloader. if empty => lookup couldn't find any providers
		if (contextClassLoaderProviders == null) {
			contextClassLoaderProviders = loadProviders(classLoader);
			updatedCache(classLoader, contextClassLoaderProviders);
		}

		return contextClassLoaderProviders;
	}

	private void updatedCache(final ClassLoader classLoader, final Set<TraceeBackendProvider> provider) {
		final Map<ClassLoader, Set<TraceeBackendProvider>> copyOnWriteMap =
				new WeakHashMap<ClassLoader, Set<TraceeBackendProvider>>(PROVIDERS_PER_CLASSLOADER);

		// When we use weak references for the provider it will be garbage collected and the set is empty. In this case we don't
		// search again because the set is empty (empty => we couldn't find any providers).
		// To search for providers again is a bad idea. So we have to use a normal reference.
		// Other possible solutions: Keep name URL of class and reinit it.
		//final Set<TraceeBackendProvider> providerSet = Collections.newSetFromMap(new WeakHashMap<TraceeBackendProvider, Boolean>());
		final Set<TraceeBackendProvider> providerSet = new HashSet<TraceeBackendProvider>();
		providerSet.addAll(provider);
		copyOnWriteMap.put(classLoader, providerSet);
		PROVIDERS_PER_CLASSLOADER = copyOnWriteMap;
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
				// ignore, because it can happen when multiple
				// providers are present and some of them are not class loader
				// compatible with our API.
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
}