package io.tracee.contextlogger.integrationtest;

import io.tracee.contextlogger.api.CustomImplicitContextData;
import io.tracee.contextlogger.api.TraceeContextProvider;
import io.tracee.contextlogger.api.TraceeContextProviderMethod;

/**
 * Test provider that provides implicit context information but which has no args constructor.
 */
@TraceeContextProvider(displayName = "testBrokenImplicitContentDataProviderWithoutDefaultConstructor", order = 200)
public class TestBrokenImplicitContentDataProviderWithoutDefaultConstructor implements CustomImplicitContextData {

    public static final String PROPERTY_NAME = "TestBrokenImplicitContentDataProviderWithoutDefaultConstructor.testOutputPropertyName";

    TestBrokenImplicitContentDataProviderWithoutDefaultConstructor (String something) {

    }


    @SuppressWarnings("unused")
    @TraceeContextProviderMethod(
            displayName = "output",
            propertyName = TestImplicitContextDataProvider.PROPERTY_NAME,
            order = 10)
    public final String getOutput() {
        throw new NullPointerException("Whoops!!!");
    }

}
