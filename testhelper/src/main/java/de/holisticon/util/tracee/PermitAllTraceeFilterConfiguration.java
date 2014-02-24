package de.holisticon.util.tracee;

import de.holisticon.util.tracee.configuration.TraceeFilterConfiguration;

import java.util.Map;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
public class PermitAllTraceeFilterConfiguration implements TraceeFilterConfiguration {


	@Override
	public final boolean shouldProcessParam(String paramName, Channel channel) {
		return true;
	}

	@Override
	public final Map<String, String> filterDeniedParams(Map<String, String> unfiltered, Channel channel) {
		return unfiltered;
	}

	@Override
	public final boolean shouldProcessContext(Channel channel) {
		return true;
	}

	@Override
	public final boolean shouldGenerateRequestId() {
		return true;
	}

	@Override
	public final int generatedRequestIdLength() {
		return 32;
	}

	@Override
	public final boolean shouldGenerateSessionId() {
		return true;
	}

	@Override
	public final int generatedSessionIdLength() {
		return 32;
	}
}
