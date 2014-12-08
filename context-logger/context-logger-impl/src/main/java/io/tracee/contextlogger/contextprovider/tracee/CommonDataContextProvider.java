package io.tracee.contextlogger.contextprovider.tracee;

import io.tracee.contextlogger.TraceeContextLoggerConstants;
import io.tracee.contextlogger.api.ImplicitContext;
import io.tracee.contextlogger.api.ImplicitContextData;
import io.tracee.contextlogger.api.TraceeContextProviderMethod;
import io.tracee.contextlogger.contextprovider.Order;

import java.net.InetAddress;
import java.net.UnknownHostException;
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
    @TraceeContextProviderMethod(displayName = "timestamp", order = 10)
    public final Date getTimestamp() {
        return Calendar.getInstance().getTime();
    }

    @SuppressWarnings("unused")
    @TraceeContextProviderMethod(displayName = "stage", order = 20)
    public final String getStage() {
        return getSystemProperty(TraceeContextLoggerConstants.SYSTEM_PROPERTY_NAME_STAGE);
    }

    @SuppressWarnings("unused")
    @TraceeContextProviderMethod(displayName = "system-name", order = 30)
    public final String getSystemName() {

        String systemName = getSystemProperty(TraceeContextLoggerConstants.SYSTEM_PROPERTY_NAME_SYSTEM);

        if (systemName == null) {
            try {
                systemName = InetAddress.getLocalHost().getHostName();
            } catch (UnknownHostException e) {
                // ignore
            }
        }

        return systemName;

    }

    @SuppressWarnings("unused")
    @TraceeContextProviderMethod(displayName = "thread-name", order = 40)
    public final String getThreadName() {
        return Thread.currentThread().getName();
    }

    @SuppressWarnings("unused")
    @TraceeContextProviderMethod(displayName = "thread-id", order = 50)
    public final Long getThreadId() {
        return Thread.currentThread().getId();
    }

    public String getSystemProperty(final String attributeName) {
        return System.getProperty(attributeName);
    }
}
