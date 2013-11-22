package de.holisticon.util.tracee;

import de.holisticon.util.tracee.spi.TraceeBackendProvider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.*;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
public class Tracee {

    public static TraceeBackend getBackend() {

        final ContextProviderResolver contextProviderResolver = new ContextProviderResolver();
        final List<TraceeBackendProvider> contextProviders;
        try {
             contextProviders = contextProviderResolver.getContextProviders();
        } catch (RuntimeException e) {
          throw new TraceeException( "Unable to load available backend providers", e );
        }
        if (contextProviders.isEmpty()) {
            throw new TraceeException( "Unable to find a tracee backend provider" );
        }
        if (contextProviders.size() > 1) {
            final ArrayList<Class<?>> providerClasses = new ArrayList<Class<?>>(contextProviders.size());
            for (TraceeBackendProvider contextProvider : contextProviders) {
                providerClasses.add(contextProvider.getClass());
            }
            final String providerClassNames = Arrays.toString(providerClasses.toArray());
            throw new TraceeException("Multiple context providers found. Don't know which one of the following to use: "+providerClassNames);
        }
        return contextProviders.get(0).provideBackend();
    }







    private static class ContextProviderResolver {

        //cache per classloader for an appropriate discovery
        //keep them in a weak hashmap to avoid memory leaks and allow proper hot redeployment
        //TODO use a WeakConcurrentHashMap
        //FIXME The List<VP> does keep a strong reference to the key ClassLoader, use the same model as JPA CachingPersistenceProviderResolver
        private static final Map<ClassLoader, List<TraceeBackendProvider>> providersPerClassloader =
                new WeakHashMap<ClassLoader, List<TraceeBackendProvider>>();

        private static final String SERVICES_FILE = "META-INF/services/" + TraceeBackendProvider.class.getName();

        public List<TraceeBackendProvider> getContextProviders() {
            ClassLoader classloader = GetClassLoader.fromContext();
            if (classloader == null) {
                classloader = GetClassLoader.fromClass(ContextProviderResolver.class);
            }

            List<TraceeBackendProvider> providers;
            synchronized (providersPerClassloader) {
                providers = providersPerClassloader.get(classloader);
            }

            if (providers == null) {
                providers = new ArrayList<TraceeBackendProvider>();
                String name = null;
                try {
                    Enumeration<URL> providerDefinitions = classloader.getResources(SERVICES_FILE);
                    while (providerDefinitions.hasMoreElements()) {
                        URL url = providerDefinitions.nextElement();
                        InputStream stream = url.openStream();
                        try {
                            BufferedReader reader = new BufferedReader(new InputStreamReader(stream), 100);
                            name = reader.readLine();
                            while (name != null) {
                                name = name.trim();
                                if (!name.startsWith("#")) {
                                    final Class<?> providerClass = loadClass(
                                            name,
                                            ContextProviderResolver.class
                                    );

                                    providers.add(
                                            (TraceeBackendProvider) providerClass.newInstance()
                                    );
                                }
                                name = reader.readLine();
                            }
                        } finally {
                            stream.close();
                        }
                    }
                } catch (IOException e) {
                    throw new TraceeException("Unable to read " + SERVICES_FILE, e);
                } catch (ClassNotFoundException e) {
                    throw new TraceeException("Unable to load Tracee Context provider " + name, e);
                } catch (IllegalAccessException e) {
                    throw new TraceeException("Unable to instanciate Tracee Context provider" + name, e);
                } catch (InstantiationException e) {
                    throw new TraceeException("Unable to instanciate Tracee Context provider" + name, e);
                }
                synchronized (providersPerClassloader) {
                    providersPerClassloader.put(classloader, providers);
                }
            }

            return providers;
        }


        private static class GetClassLoader implements PrivilegedAction<ClassLoader> {
            private final Class<?> clazz;

            public static ClassLoader fromContext() {
                final GetClassLoader action = new GetClassLoader(null);
                if (System.getSecurityManager() != null) {
                    return AccessController.doPrivileged(action);
                } else {
                    return action.run();
                }
            }

            public static ClassLoader fromClass(Class<?> clazz) {
                if (clazz == null) {
                    throw new IllegalArgumentException("Class is null");
                }
                final GetClassLoader action = new GetClassLoader(clazz);
                if (System.getSecurityManager() != null) {
                    return AccessController.doPrivileged(action);
                } else {
                    return action.run();
                }
            }

            private GetClassLoader(Class<?> clazz) {
                this.clazz = clazz;
            }

            public ClassLoader run() {
                if (clazz != null) {
                    return clazz.getClassLoader();
                } else {
                    return Thread.currentThread().getContextClassLoader();
                }
            }
        }

        private static Class<?> loadClass(String name, Class<?> caller) throws ClassNotFoundException {
            try {
                //try context classloader, if fails try caller classloader
                ClassLoader loader = GetClassLoader.fromContext();
                if (loader != null) {
                    return loader.loadClass(name);
                }
            } catch (ClassNotFoundException e) {
                //trying caller classloader
                if (caller == null) {
                    throw e;
                }
            }
            return Class.forName(name, true, GetClassLoader.fromClass(caller));
        }
    }


}
