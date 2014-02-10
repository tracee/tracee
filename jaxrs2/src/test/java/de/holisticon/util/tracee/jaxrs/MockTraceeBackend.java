package de.holisticon.util.tracee.jaxrs;

import de.holisticon.util.tracee.TraceeBackend;
import de.holisticon.util.tracee.TraceeLoggerFactory;
import de.holisticon.util.tracee.configuration.TraceeFilterConfiguration;

import java.util.HashMap;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
public class MockTraceeBackend  extends HashMap<String,String> implements TraceeBackend {

	public MockTraceeBackend(TraceeFilterConfiguration configuration, TraceeLoggerFactory loggerFactory) {
		this.configuration = configuration;
		this.loggerFactory = loggerFactory;
	}

	private final TraceeFilterConfiguration configuration;
	private final TraceeLoggerFactory loggerFactory;

	@Override
	public TraceeFilterConfiguration getConfiguration() {
		return configuration;
	}

	@Override
	public TraceeLoggerFactory getLoggerFactory() {
		return loggerFactory;
	}
}
