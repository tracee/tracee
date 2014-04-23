package de.holisticon.util.tracee;

import de.holisticon.util.tracee.configuration.TraceeFilterConfiguration;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A testhelper for TraceeBackend dependent tests.
 * @author Daniel Wegener (Holisticon AG)
 */
public class SimpleTraceeBackend extends HashMap<String, String> implements TraceeBackend {


	private Map<String, String> valuesBeforeLastClear = Collections.emptyMap();

	public static SimpleTraceeBackend createNonLoggingAllPermittingBackend() {
		return new SimpleTraceeBackend(new PermitAllTraceeFilterConfiguration(), new NoopTraceeLoggerFactory());
	}

	public SimpleTraceeBackend(TraceeFilterConfiguration configuration, TraceeLoggerFactory loggerFactory) {
		this.configuration = configuration;
		this.loggerFactory = loggerFactory;
	}

	private final TraceeFilterConfiguration configuration;

	private final TraceeLoggerFactory loggerFactory;

	/**
	 * {@inheritDoc}
	 * <br /><strong>This implementation ignores profiles and always uses the default configuration.</strong>
	 */
	@Override
	public TraceeFilterConfiguration getConfiguration(String profileName) {
		return configuration;
	}

	@Override
	public TraceeFilterConfiguration getConfiguration() {
		return configuration;
	}

	@Override
	public TraceeLoggerFactory getLoggerFactory() {
		return loggerFactory;
	}

	@Override
	public void clear() {
		this.valuesBeforeLastClear = new HashMap<String, String>(this);
		super.clear();
	}

	public Map<String, String> getValuesBeforeLastClear() {
		return valuesBeforeLastClear;
	}
}
