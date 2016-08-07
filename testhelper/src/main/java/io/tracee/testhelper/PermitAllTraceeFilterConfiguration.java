package io.tracee.testhelper;

import io.tracee.configuration.TraceeFilterConfiguration;

import java.util.Map;

public class PermitAllTraceeFilterConfiguration implements TraceeFilterConfiguration {

	public static final PermitAllTraceeFilterConfiguration INSTANCE = new PermitAllTraceeFilterConfiguration();

	/** Alias for spring xml configuration */
	public static PermitAllTraceeFilterConfiguration instance() {
		return INSTANCE;
	}

	public static final int ARBITRARY_NUMBER = 32;

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
	public final boolean shouldGenerateInvocationId() {
		return true;
	}

	@Override
	public final int generatedInvocationIdLength() {
		return ARBITRARY_NUMBER;
	}

	@Override
	public final boolean shouldGenerateSessionId() {
		return true;
	}

	@Override
	public final int generatedSessionIdLength() {
		return ARBITRARY_NUMBER;
	}
}
