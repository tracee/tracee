package io.tracee.contextlogger.data.subdata.tracee;

import io.tracee.contextlogger.ImplicitContext;
import io.tracee.contextlogger.TraceeContextLoggerConstants;
import io.tracee.contextlogger.api.*;
import io.tracee.contextlogger.data.Order;
import io.tracee.contextlogger.profile.ProfilePropertyNames;

import java.util.Calendar;
import java.util.Date;

/**
 * Common context data provider.
 * Created by Tobias Gindler, holisticon AG on 14.03.14.
 */
@io.tracee.contextlogger.api.TraceeContextProvider(displayName = "common", order = Order.COMMON)
public class CommonDataContextProvider implements ImplicitContextData {

    @Override
    public final ImplicitContext getImplicitContext() {
        return ImplicitContext.COMMON;
    }

    @SuppressWarnings("unused")
    @TraceeContextProviderMethod(
            displayName = "timestamp",
            propertyName = ProfilePropertyNames.COMMON_TIMESTAMP,
            order = 10)

    public final Date getTimestamp() {
        return Calendar.getInstance().getTime();
    }

    @SuppressWarnings("unused")
    @TraceeContextProviderMethod(
            displayName = "stage",
            propertyName = ProfilePropertyNames.COMMON_STAGE,
            order = 20)

    public final String getStage() {
        return getSystemProperty(TraceeContextLoggerConstants.SYSTEM_PROPERTY_NAME_STAGE);
    }

    @SuppressWarnings("unused")
    @TraceeContextProviderMethod(
            displayName = "system-name",
            propertyName = ProfilePropertyNames.COMMON_SYSTEM_NAME,
            order = 30)

    public final String getSystemName() {
        return getSystemProperty(TraceeContextLoggerConstants.SYSTEM_PROPERTY_NAME_SYSTEM);
    }

    @SuppressWarnings("unused")
    @TraceeContextProviderMethod(
            displayName = "thread-name",
            propertyName = ProfilePropertyNames.COMMON_THREAD_NAME,
            order = 40)

    public final String getThreadName() {
        return Thread.currentThread().getName();
    }

    @SuppressWarnings("unused")
    @TraceeContextProviderMethod(
            displayName = "thread-id",
            propertyName = ProfilePropertyNames.COMMON_THREAD_ID,
            order = 50)

    public final Long getThreadId() {
        return Thread.currentThread().getId();
    }


    public String getSystemProperty(final String attributeName) {
        return System.getProperty(attributeName);
    }
}
