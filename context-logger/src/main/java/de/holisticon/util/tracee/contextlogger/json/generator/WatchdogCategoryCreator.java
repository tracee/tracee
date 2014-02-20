package de.holisticon.util.tracee.contextlogger.json.generator;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import de.holisticon.util.tracee.contextlogger.json.beans.WatchdogCategory;
import de.holisticon.util.tracee.contextlogger.json.generator.datawrapper.WatchdogDataWrapper;
import de.holisticon.util.tracee.contextlogger.json.generator.util.RecursiveReflectionToStringStyle;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.aspectj.lang.reflect.MethodSignature;

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

        // output parameters
        final List<String> parameters = new ArrayList<String>();
        for (final Object attr : watchdogDataWrapper.getProceedingJoinPoint().getArgs()) {
            parameters.add(attr == null ? null : ReflectionToStringBuilder.reflectionToString(attr,new RecursiveReflectionToStringStyle()));
        }

        // output called instance


        String deSerializedInstance;
        Object targetInstance = watchdogDataWrapper.getProceedingJoinPoint().getTarget();
        if (targetInstance != null) {
            deSerializedInstance = ReflectionToStringBuilder.reflectionToString(targetInstance, new RecursiveReflectionToStringStyle());
        } else {
            deSerializedInstance = null;
        }

        return new WatchdogCategory(watchdogDataWrapper.getAnnotatedId(), clazz, method, parameters, deSerializedInstance);
    }



}
