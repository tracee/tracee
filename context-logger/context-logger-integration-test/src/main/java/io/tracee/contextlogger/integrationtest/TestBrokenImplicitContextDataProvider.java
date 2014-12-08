package io.tracee.contextlogger.integrationtest;

import io.tracee.contextlogger.api.CustomImplicitContextData;
import io.tracee.contextlogger.api.TraceeContextProvider;
import io.tracee.contextlogger.api.TraceeContextProviderMethod;

/**
 * Test provider that provides implicit context information that triggers an exception during deserialization.
 */
@TraceeContextProvider(displayName = "testBrokenImplicitContextData", order = 200)
public class TestBrokenImplicitContextDataProvider implements CustomImplicitContextData {

	public static final String PROPERTY_NAME = "io.tracee.contextlogger.integrationtest.TestBrokenImplicitContextDataProvider.output";

	@SuppressWarnings("unused")
	@TraceeContextProviderMethod(displayName = "output", order = 10)
	public final String getOutput() {
		throw new NullPointerException("Whoops!!!");
	}

}
