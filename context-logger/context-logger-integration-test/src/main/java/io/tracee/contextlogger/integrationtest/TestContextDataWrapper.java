package io.tracee.contextlogger.integrationtest;

import io.tracee.contextlogger.api.TraceeContextLogProvider;
import io.tracee.contextlogger.api.TraceeContextLogProviderMethod;
import io.tracee.contextlogger.api.WrappedContextData;

/**
 * Test wrapper class that wraps type {@link io.tracee.contextlogger.integrationtest.WrappedTestContextData}.
 */
@TraceeContextLogProvider(displayName = "testdata", order = 50)
public class TestContextDataWrapper implements WrappedContextData<WrappedTestContextData> {

    public static final String PROPERTY_NAME = "WrappedTestContextData.testOutputPropertyName";

    private WrappedTestContextData contextData;

    public TestContextDataWrapper() {

    }

    public TestContextDataWrapper(final WrappedTestContextData contextData) {
        this.contextData = contextData;
    }

    @Override
    public void setContextData(Object instance) throws ClassCastException {
        this.contextData = (WrappedTestContextData) instance;
    }

    @Override
    public Class<WrappedTestContextData> getWrappedType() {
        return WrappedTestContextData.class;
    }

    @SuppressWarnings("unused")
    @TraceeContextLogProviderMethod(
            displayName = "testoutput",
            propertyName = TestContextDataWrapper.PROPERTY_NAME,
            order = 10)
    public String getOutput() {
        return contextData != null ? contextData.getOutput() : null;
    }
}
