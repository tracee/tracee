package io.tracee.contextlogger.integrationtest;

import io.tracee.contextlogger.api.CustomImplicitContextData;
import io.tracee.contextlogger.api.TraceeContextProvider;
import io.tracee.contextlogger.api.TraceeContextProviderMethod;

/**
 * Test provider that provides implicit context information but which has no args constructor.
 */
@TraceeContextProvider(displayName = "testBrokenImplicitContentDataProviderWithoutDefaultConstructor", order = 200)
public class TestBrokenImplicitContentDataProviderWithoutDefaultConstructor implements CustomImplicitContextData {

	public static final String PROPERTY_NAME = "io.tracee.contextlogger.integrationtest.TestBrokenImplicitContentDataProviderWithoutDefaultConstructor.output";

	TestBrokenImplicitContentDataProviderWithoutDefaultConstructor(String something) {

	}

	@SuppressWarnings("unused")
	@TraceeContextProviderMethod(displayName = "output", order = 10)
	public final String getOutput() {
		throw new NullPointerException("Whoops!!!");
	}

}
