package de.holisticon.util.tracee.contextlogger.data.subdata.tracee;

import de.holisticon.util.tracee.contextlogger.ImplicitContext;
import de.holisticon.util.tracee.contextlogger.TraceeContextLoggerConstants;
import de.holisticon.util.tracee.contextlogger.api.ImplicitContextData;
import de.holisticon.util.tracee.contextlogger.api.TraceeContextLogProvider;
import de.holisticon.util.tracee.contextlogger.api.TraceeContextLogProviderMethod;
import de.holisticon.util.tracee.contextlogger.data.Order;
import de.holisticon.util.tracee.contextlogger.profile.ProfilePropertyNames;

import java.util.Calendar;
import java.util.Date;

/**
 * Common context data provider.
 * Created by Tobias Gindler, holisticon AG on 14.03.14.
 */
@TraceeContextLogProvider(displayName = "common", order = Order.COMMON)
public class CommonDataContextProvider implements ImplicitContextData {

    @Override
    public ImplicitContext getImplicitContext() {
        return ImplicitContext.COMMON;
    }

    @SuppressWarnings("unused")
    @TraceeContextLogProviderMethod(
            displayName = "timestamp",
            propertyName = ProfilePropertyNames.COMMON_TIMESTAMP,
            order = 10)

    public Date getTimestamp() {
        return Calendar.getInstance().getTime();
    }

    @SuppressWarnings("unused")
    @TraceeContextLogProviderMethod(
            displayName = "stage",
            propertyName = ProfilePropertyNames.COMMON_STAGE,
            order = 20)

    public String getStage() {
        return getSystemProperty(TraceeContextLoggerConstants.SYSTEM_PROPERTY_NAME_STAGE);
    }

    @SuppressWarnings("unused")
    @TraceeContextLogProviderMethod(
            displayName = "system-name",
            propertyName = ProfilePropertyNames.COMMON_SYSTEM_NAME,
            order = 30)

    public String getSystemName() {
        return getSystemProperty(TraceeContextLoggerConstants.SYSTEM_PROPERTY_NAME_SYSTEM);
    }

    @SuppressWarnings("unused")
    @TraceeContextLogProviderMethod(
            displayName = "thread-name",
            propertyName = ProfilePropertyNames.COMMON_THREAD_NAME,
            order = 40)

    public String getThreadName() {
        return Thread.currentThread().getName();
    }

    @SuppressWarnings("unused")
    @TraceeContextLogProviderMethod(
            displayName = "thread-id",
            propertyName = ProfilePropertyNames.COMMON_THREAD_ID,
            order = 50)

    public Long getThreadId() {
        return Thread.currentThread().getId();
    }

    private static String getSystemProperty(final String attributeName) {
        return System.getProperty(attributeName);
    }
}
