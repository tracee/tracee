package de.holisticon.util.tracee;

import de.holisticon.util.tracee.spi.TraceeBackendProvider;

import java.lang.ref.SoftReference;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.WeakHashMap;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
public final class Tracee {

    private Tracee() {

    }

    /**
     * Returns the TraceeBackend. There must be exactly one Tracee implementation on the classpath.
     * <p/>
     * A call to this method may initially block to lookup the implementation with a {@link java.util.ServiceLoader}.
     * The call to this method from multiple threads with different class loader contexts may initially be slow
     * because cache writes can overwrite each other in concurrent situations and some class loader contexts may have
     * to be looked up multiple times. This allows the lookup mechanism to completely avoid synchronization.
     * <p/>
     * TODO: If you run a nested class loader environment (like a servlet container) and have the Tracee Api in a top
     * level class loader and a Tracee Implementation in a child class loader, the child class loader may not be unloaded
     * until a low memory situation occurs (since the SoftReference keeps the TraceeBackendProvider in memory).
     * It could be a solution to change the SoftReference to WeakReference but let a TraceeBackend keep a strong
     * reference to its TraceeBackendProvider.
     */
    public static TraceeBackend getBackend() {
		return getBackend(new BackendProviderResolver());
    }
	
	protected static TraceeBackend getBackend(final BackendProviderResolver resolver) {
		final List<TraceeBackendProvider> backendProviders;
		try {
			backendProviders = resolver.getContextProviders();
		} catch (RuntimeException e) {
			throw new TraceeException("Unable to load available backend providers", e);
		}
		if (backendProviders.isEmpty()) {
			throw new TraceeException("Unable to find a tracee backend provider. Make sure that you have a implementation on your classpath.");
		}
		if (backendProviders.size() > 1) {
			final List<Class<?>> providerClasses = new ArrayList<Class<?>>(backendProviders.size());
			for (TraceeBackendProvider backendProvider : backendProviders) {
				providerClasses.add(backendProvider.getClass());
			}
			final String providerClassNames = Arrays.toString(providerClasses.toArray());
			throw new TraceeException("Multiple context providers found. Don't know which one of the following to use: "
					+ providerClassNames);
		}
		return backendProviders.get(0).provideBackend();
	}


    protected static class BackendProviderResolver {

        private static Map<ClassLoader, SoftReference<List<TraceeBackendProvider>>> PROVIDERS_PER_CLASSLOADER =
                new WeakHashMap<ClassLoader, SoftReference<List<TraceeBackendProvider>>>();

		public List<TraceeBackendProvider> getContextProviders() {
			final Map<ClassLoader, SoftReference<List<TraceeBackendProvider>>> cache = PROVIDERS_PER_CLASSLOADER;

			final ClassLoader contextClassloader = GetClassLoader.fromContext();

			// 1. take cached from context class loader
			final List<TraceeBackendProvider> cachedContextClassLoaderProviders = getFromCache(cache, contextClassloader);
			if (cachedContextClassLoaderProviders != null) {
				return cachedContextClassLoaderProviders;
			}

			// 2. load from context class loader and cache if not empty
			final List<TraceeBackendProvider> contextClassLoaderProviders = loadProviders(contextClassloader);
			if (!contextClassLoaderProviders.isEmpty()) {
				PROVIDERS_PER_CLASSLOADER = updatedCache(cache, contextClassloader, contextClassLoaderProviders);
				return contextClassLoaderProviders;
			} else {

				// 3. take cached from current class loader
				final ClassLoader classloader = GetClassLoader.fromClass(BackendProviderResolver.class);
				final List<TraceeBackendProvider> cachedClassLoaderProviders = getFromCache(cache, classloader);
				if (cachedClassLoaderProviders != null) {
					// if already processed return the cached provider list
					return cachedClassLoaderProviders;
				}

				// 4. load from current class loader and cache the result
				final List<TraceeBackendProvider> classLoaderProviders = loadProviders(classloader);
				PROVIDERS_PER_CLASSLOADER = updatedCache(cache, classloader, classLoaderProviders);
				return classLoaderProviders;
			}

		}

        private List<TraceeBackendProvider> getFromCache(Map<ClassLoader, SoftReference<List<TraceeBackendProvider>>> cache, ClassLoader classLoader) {
            final SoftReference<List<TraceeBackendProvider>> entry = cache.get(classLoader);
            return entry == null ? null : entry.get();
        }

        private Map<ClassLoader, SoftReference<List<TraceeBackendProvider>>> updatedCache(
                Map<ClassLoader, SoftReference<List<TraceeBackendProvider>>> cache,
                ClassLoader classLoader, List<TraceeBackendProvider> provider) {
            final Map<ClassLoader, SoftReference<List<TraceeBackendProvider>>> copyOnWriteMap = new WeakHashMap<ClassLoader, SoftReference<List<TraceeBackendProvider>>>(cache);
            copyOnWriteMap.put(classLoader, new SoftReference<List<TraceeBackendProvider>>(provider));
            return copyOnWriteMap;
        }

        private List<TraceeBackendProvider> loadProviders(ClassLoader classloader) {
            final ServiceLoader<TraceeBackendProvider> loader = ServiceLoader.load(TraceeBackendProvider.class, classloader);
            final Iterator<TraceeBackendProvider> providerIterator = loader.iterator();
            final List<TraceeBackendProvider> validationProviderList = new ArrayList<TraceeBackendProvider>();
            while (providerIterator.hasNext()) {
                try {
                    validationProviderList.add(providerIterator.next());
                } catch (ServiceConfigurationError e) {
                    // ignore, because it can happen when multiple
                    // providers are present and some of them are not class loader
                    // compatible with our API.
                }
            }
            return validationProviderList;
        }

			private static final class GetClassLoader implements PrivilegedAction<ClassLoader> {
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
}
