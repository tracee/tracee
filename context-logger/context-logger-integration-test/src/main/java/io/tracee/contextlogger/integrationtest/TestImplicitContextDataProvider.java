package io.tracee.contextlogger.integrationtest;

import io.tracee.contextlogger.ImplicitContext;
import io.tracee.contextlogger.api.CustomImplicitContextData;
import io.tracee.contextlogger.api.ImplicitContextData;
import io.tracee.contextlogger.api.TraceeContextLogProvider;
import io.tracee.contextlogger.api.TraceeContextLogProviderMethod;

/**
 * Test provider that provides implicit context information.
 */
@TraceeContextLogProvider(displayName = "testImplicitContextData", order = 200)
public class TestImplicitContextDataProvider implements CustomImplicitContextData {

    public static final String PROPERTY_NAME = "TestImplicitContextDataProvider.testOutputPropertyName";
    public static final String OUTPUT = "IT WORKS TOO!!!";

    @SuppressWarnings("unused")
    @TraceeContextLogProviderMethod(
            displayName = "output",
            propertyName = TestImplicitContextDataProvider.PROPERTY_NAME,
            order = 10)
    public final String getOutput() {
        return OUTPUT;
    }

}
