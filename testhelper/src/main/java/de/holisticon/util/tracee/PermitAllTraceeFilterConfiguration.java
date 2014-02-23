package de.holisticon.util.tracee;

import de.holisticon.util.tracee.configuration.TraceeFilterConfiguration;

import java.util.Map;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
public class PermitAllTraceeFilterConfiguration implements TraceeFilterConfiguration {


	@Override
	public boolean shouldProcessParam(String paramName, Channel channel) {
		return true;
	}

	@Override
	public Map<String, String> filterDeniedParams(Map<String, String> unfiltered, Channel channel) {
		return unfiltered;
	}

	@Override
	public boolean shouldProcessContext(Channel channel) {
		return true;
	}

	@Override
	public boolean shouldGenerateRequestId() {
		return true;
	}

	@Override
	public int generatedRequestIdLength() {
		return 32;
	}

	@Override
	public boolean shouldGenerateSessionId() {
		return true;
	}

	@Override
	public int generatedSessionIdLength() {
		return 32;
	}
}
