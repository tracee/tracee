package io.tracee.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

public final class TraceePropertiesFileLoader {

	public static final String TRACEE_PROPERTIES_FILE = "META-INF/tracee.properties";
	public static final String TRACEE_DEFAULT_PROPERTIES_FILE = "META-INF/tracee.default.properties";

	public Properties loadTraceeProperties(String traceePropertiesFile) throws IOException {
		final Properties propertiesFromFile = new Properties();
		final ClassLoader loader = Thread.currentThread().getContextClassLoader();
		final Enumeration<URL> traceePropertyFiles = loader.getResources(traceePropertiesFile);

		while (traceePropertyFiles.hasMoreElements()) {
			final URL url = traceePropertyFiles.nextElement();
			try (InputStream stream = url.openStream()) {
				propertiesFromFile.load(stream);
			}
		}

		return propertiesFromFile;
	}
}
