package de.holisticon.util.tracee.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
public final class TraceePropertiesFileLoader {


	public static final String TRACEE_PROPERTIES_FILE = "tracee.properties";

	public Properties loadTraceeProperties() throws IOException {
		final Properties propertiesFromFile = new Properties();
		final ClassLoader loader = Thread.currentThread().getContextClassLoader();

		final Enumeration<URL> traceePropertyFiles = loader.getResources(TRACEE_PROPERTIES_FILE);

		while (traceePropertyFiles.hasMoreElements()) {
			final URL url = traceePropertyFiles.nextElement();
			InputStream stream = null;
			try {
				stream = url.openStream();
				propertiesFromFile.load(stream);
			} catch (IOException e) {
				throw e;
			} finally {
				try {
					if (stream != null)
						stream.close();
				} catch (IOException ignored) {}
			}
		}

		return propertiesFromFile;
	}

}
