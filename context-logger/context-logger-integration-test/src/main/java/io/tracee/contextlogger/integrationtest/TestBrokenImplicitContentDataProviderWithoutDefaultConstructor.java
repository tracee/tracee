package io.tracee.contextlogger.integrationtest;

import io.tracee.contextlogger.api.CustomImplicitContextData;
import io.tracee.contextlogger.api.TraceeContextLogProvider;
import io.tracee.contextlogger.api.TraceeContextLogProviderMethod;

/**
 * Test provider that provides implicit context information but which has no args constructor.
 */
@TraceeContextLogProvider(displayName = "testBrokenImplicitContentDataProviderWithoutDefaultConstructor", order = 200)
public class TestBrokenImplicitContentDataProviderWithoutDefaultConstructor implements CustomImplicitContextData {

    public static final String PROPERTY_NAME = "TestBrokenImplicitContentDataProviderWithoutDefaultConstructor.testOutputPropertyName";

    TestBrokenImplicitContentDataProviderWithoutDefaultConstructor (String something) {

    }


    @SuppressWarnings("unused")
    @TraceeContextLogProviderMethod(
            displayName = "output",
            propertyName = TestImplicitContextDataProvider.PROPERTY_NAME,
            order = 10)
    public final String getOutput() {
        throw new NullPointerException("Whoops!!!");
    }

}
