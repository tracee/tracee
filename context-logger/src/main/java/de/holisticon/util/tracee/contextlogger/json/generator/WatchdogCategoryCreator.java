package de.holisticon.util.tracee.contextlogger.json.generator;

import java.util.ArrayList;
import java.util.List;

import de.holisticon.util.tracee.contextlogger.json.beans.WatchdogCategory;
import de.holisticon.util.tracee.contextlogger.json.generator.datawrapper.WatchdogDataWrapper;

/**
 * Factory for tracee context watchdog specific data (Method calls).
 * Created by Tobias Gindler, holisticon AG on 20.12.13.
 */
public final class WatchdogCategoryCreator {

    private WatchdogCategoryCreator() {
    }

    public static WatchdogCategory createWatchdogCategory(final WatchdogDataWrapper watchdogDataWrapper) {

        final String clazz = watchdogDataWrapper.getProceedingJoinPoint().getSignature().getDeclaringTypeName();
        final String method = watchdogDataWrapper.getProceedingJoinPoint().getSignature().getName();
        final List<String> parameters = new ArrayList<String>();
        for (final Object attr : watchdogDataWrapper.getProceedingJoinPoint().getArgs()) {
            parameters.add(attr == null ? null : attr.toString());
        }
        final String deserializedInstance = null;

        return new WatchdogCategory(clazz, method, parameters, deserializedInstance);
    }

}
