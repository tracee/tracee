package io.tracee.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

public final class TraceePropertiesFileLoader {

	public Properties loadTraceeProperties(String traceePropertiesFile) throws IOException {
		final Properties propertiesFromFile = new Properties();
		final ClassLoader loader = Thread.currentThread().getContextClassLoader();
		final Enumeration<URL> traceePropertyFiles = loader.getResources(traceePropertiesFile);

		while (traceePropertyFiles.hasMoreElements()) {
			final URL url = traceePropertyFiles.nextElement();
			InputStream stream = null;
			try {
				stream = url.openStream();
				propertiesFromFile.load(stream);
			} finally {
				try {
					if (stream != null)
						stream.close();
				} catch (IOException ignored) { }
			}
		}

		return propertiesFromFile;
	}
}
