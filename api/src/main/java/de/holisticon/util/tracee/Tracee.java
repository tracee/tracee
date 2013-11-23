package de.holisticon.util.tracee;

import de.holisticon.util.tracee.spi.TraceeBackendProvider;

import java.io.*;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.*;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
public final class Tracee {

    private Tracee() {

    }

    public static TraceeBackend getBackend() {

        final ContextProviderResolver contextProviderResolver = new ContextProviderResolver();
        final List<TraceeBackendProvider> contextProviders;
        try {
            contextProviders = contextProviderResolver.getContextProviders();
        } catch (RuntimeException e) {
            throw new TraceeException("Unable to load available backend providers", e);
        }
        if (contextProviders.isEmpty()) {
            throw new TraceeException("Unable to find a tracee backend provider");
        }
        if (contextProviders.size() > 1) {
            final ArrayList<Class<?>> providerClasses = new ArrayList<Class<?>>(contextProviders.size());
            for (TraceeBackendProvider contextProvider : contextProviders) {
                providerClasses.add(contextProvider.getClass());
            }
            final String providerClassNames = Arrays.toString(providerClasses.toArray());
            throw new TraceeException("Multiple context providers found. Don't know which one of the following to use: "
                    + providerClassNames);
        }
        return contextProviders.get(0).provideBackend();
    }


    private static final class ContextProviderResolver {

        //cache per classloader for an appropriate discovery
        //keep them in a weak hashmap to avoid memory leaks and allow proper hot redeployment
        //TODO use a WeakConcurrentHashMap
        //FIXME The List<VP> does keep a strong reference to the key ClassLoader,
        // use the same model as JPA CachingPersistenceProviderResolver
        private static final Map<ClassLoader, List<TraceeBackendProvider>> PROVIDERS_PER_CLASSLOADER =
                new WeakHashMap<ClassLoader, List<TraceeBackendProvider>>();

        private static final String SERVICES_FILE = "META-INF/services/" + TraceeBackendProvider.class.getName();

        public List<TraceeBackendProvider> getContextProviders() {
            ClassLoader classloader = GetClassLoader.fromContext();
            if (classloader == null) {
                classloader = GetClassLoader.fromClass(ContextProviderResolver.class);
            }

            List<TraceeBackendProvider> providers;
            synchronized (PROVIDERS_PER_CLASSLOADER) {
                providers = PROVIDERS_PER_CLASSLOADER.get(classloader);
            }

            if (providers == null) {
                providers = new ArrayList<TraceeBackendProvider>();
                String name = null;

                try {
                    Enumeration<URL> providerDefinitions = classloader.getResources(SERVICES_FILE);
                    while (providerDefinitions.hasMoreElements()) {
                        final URL url = providerDefinitions.nextElement();

                        final List<String> classNames = readClassNamesFrom(url);

                        for (String className : classNames) {
                            name = className;
                            final Class<?> providerClass = loadClass(
                                    className,
                                    ContextProviderResolver.class
                            );
                            providers.add((TraceeBackendProvider) providerClass.newInstance());
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
                synchronized (PROVIDERS_PER_CLASSLOADER) {
                    PROVIDERS_PER_CLASSLOADER.put(classloader, providers);

                }
            }

            return providers;
        }

        private static final int EXPECTED_MAX_CLASS_NAME_LENGTH = 128;
        private static final String UTF_8 = "UTF-8";

        private List<String> readClassNamesFrom(URL url) throws IOException {
            final List<String> classNames = new LinkedList<String>();

            InputStream stream = null;
            BufferedReader reader = null;
            try {
                stream = url.openStream();

                try {
                    reader = new BufferedReader(new InputStreamReader(stream, UTF_8), EXPECTED_MAX_CLASS_NAME_LENGTH);
                } catch (UnsupportedEncodingException e) {
                    throw new Error("UTF8 charset not supported.");
                }
                String name = reader.readLine();
                while (name != null) {
                    name = name.trim();
                    if (!name.startsWith("#")) {
                        classNames.add(name);
                    }
                    name = reader.readLine();
                }
            } finally {
                try {
                    if (stream != null) stream.close();
                } catch (IOException ignored) {
                    //ignored
                }
                try {
                    if (reader != null) reader.close();
                } catch (IOException ignored) {
                    //ignored
                }
            }

            return classNames;
        }


        private static final class GetClassLoader implements PrivilegedAction<ClassLoader> {
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
