package io.tracee.contextlogger.integrationtest;

import io.tracee.contextlogger.api.CustomImplicitContextData;
import io.tracee.contextlogger.api.TraceeContextProvider;
import io.tracee.contextlogger.api.TraceeContextProviderMethod;

/**
 * Test provider that provides implicit context information.
 */
@TraceeContextProvider(displayName = "testImplicitContextData", order = 200)
public class TestImplicitContextDataProvider implements CustomImplicitContextData {

    public static final String PROPERTY_NAME = "TestImplicitContextDataProvider.testOutputPropertyName";
    public static final String OUTPUT = "IT WORKS TOO!!!";

    @SuppressWarnings("unused")
    @TraceeContextProviderMethod(displayName = "output", propertyName = TestImplicitContextDataProvider.PROPERTY_NAME, order = 10)
    public final String getOutput() {
        return OUTPUT;
    }

}
