package de.holisticon.util.tracee;

import de.holisticon.util.tracee.spi.TraceeBackendProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

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
		final Set<TraceeBackendProvider> backendProviders;
		try {
			backendProviders = resolver.getBackendProviders();
		} catch (RuntimeException e) {
			throw new TraceeException("Unable to load available backend providers", e);
		}
		if (backendProviders.isEmpty()) {
			throw new TraceeException("Unable to find a TracEE backend provider. Make sure that you have an implementation on the classpath.");
		}
		if (backendProviders.size() > 1) {
			final List<Class<?>> providerClasses = new ArrayList<Class<?>>(backendProviders.size());
			for (TraceeBackendProvider backendProvider : backendProviders) {
				providerClasses.add(backendProvider.getClass());
			}
			final String providerClassNames = Arrays.toString(providerClasses.toArray());
			throw new TraceeException("Multiple TracEE backend providers found. Don't know which one of the following to use: "
					+ providerClassNames);
		}
		return backendProviders.iterator().next().provideBackend();
	}
}
