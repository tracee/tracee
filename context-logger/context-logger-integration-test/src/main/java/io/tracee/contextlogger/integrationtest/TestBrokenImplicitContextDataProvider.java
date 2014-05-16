package io.tracee.contextlogger.integrationtest;

import io.tracee.contextlogger.api.CustomImplicitContextData;
import io.tracee.contextlogger.api.TraceeContextLogProvider;
import io.tracee.contextlogger.api.TraceeContextLogProviderMethod;
import org.apache.commons.lang3.ObjectUtils;

/**
 * Test provider that provides implicit context information that triggers an exception during deserialization.
 */
@TraceeContextLogProvider(displayName = "testBrokenImplicitContextData", order = 200)
public class TestBrokenImplicitContextDataProvider implements CustomImplicitContextData {

    public static final String PROPERTY_NAME = "TestBrokenImplicitContextDataProvider.testOutputPropertyName";

    @SuppressWarnings("unused")
    @TraceeContextLogProviderMethod(
            displayName = "output",
            propertyName = TestImplicitContextDataProvider.PROPERTY_NAME,
            order = 10)
    public final String getOutput() {
        throw new NullPointerException("Whoops!!!");
    }

}
