package de.holisticon.util.tracee.contextlogger.data.subdata.tracee;

import de.holisticon.util.tracee.contextlogger.api.TraceeContextLogProvider;
import de.holisticon.util.tracee.contextlogger.api.TraceeContextLogProviderMethod;
import de.holisticon.util.tracee.contextlogger.api.WrappedContextData;
import de.holisticon.util.tracee.contextlogger.data.Order;
import de.holisticon.util.tracee.contextlogger.data.subdata.aspectj.AspectjProceedingJoinPoint;
import de.holisticon.util.tracee.contextlogger.data.wrapper.WatchdogDataWrapper;
import de.holisticon.util.tracee.contextlogger.profile.ProfilePropertyNames;
import de.holisticon.util.tracee.contextlogger.utility.RecursiveReflectionToStringStyle;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.aspectj.lang.ProceedingJoinPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Watchdog context data provider.
 * Created by Tobias Gindler, holisticon AG on 17.03.14.
 */
@SuppressWarnings("unused")
@TraceeContextLogProvider(displayName = "watchdog", order = Order.WATCHDOG)
public final class WatchdogContextProvider implements WrappedContextData<WatchdogDataWrapper>{

    private WatchdogDataWrapper watchdogDataWrapper;

    public WatchdogContextProvider() {
    }

    @SuppressWarnings("unused")
    public WatchdogContextProvider(final WatchdogDataWrapper watchdogDataWrapper) {
        this.watchdogDataWrapper = watchdogDataWrapper;
    }

    @Override
    public void setContextData(Object instance) throws ClassCastException {
        this.watchdogDataWrapper = (WatchdogDataWrapper) instance;
    }

    @Override
    public Class<WatchdogDataWrapper> getWrappedType() {
        return WatchdogDataWrapper.class;
    }

    @SuppressWarnings("unused")
    @TraceeContextLogProviderMethod(
            displayName = "id",
            propertyName = ProfilePropertyNames.WATCHDOG_ID,
            order = 10)
    public String getId() {
        if (watchdogDataWrapper != null) {
            return watchdogDataWrapper.getAnnotatedId();
        }
        return null;
    }

    @SuppressWarnings("unused")
    @TraceeContextLogProviderMethod(
            displayName = "aspectj.proceedingJoinPoint",
            propertyName = ProfilePropertyNames.WATCHDOG_ASPECTJ_CONTEXT,
            order = 20)
    public AspectjProceedingJoinPoint getProceedingJoinPoint() {
        if (watchdogDataWrapper != null) {
            return AspectjProceedingJoinPoint.wrap(watchdogDataWrapper.getProceedingJoinPoint());
        }
        return null;
    }

}
