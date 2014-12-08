package io.tracee.contextlogger.contextprovider.aspectj;

import io.tracee.contextlogger.TraceeContextLoggerConstants;
import io.tracee.contextlogger.api.TraceeContextProvider;
import io.tracee.contextlogger.api.TraceeContextProviderMethod;
import io.tracee.contextlogger.api.WrappedContextData;
import io.tracee.contextlogger.utility.RecursiveReflectionToStringStyle;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.aspectj.lang.ProceedingJoinPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Context provider for ProceedingJoinPoint.
 * Created by Tobias Gindler, holisticon AG on 20.03.14.
 */
@TraceeContextProvider(displayName = "proceedingJoinPoint")
public class AspectjProceedingJoinPointContextProvider implements WrappedContextData<ProceedingJoinPoint> {

    private ProceedingJoinPoint proceedingJoinPoint;

    @SuppressWarnings("unused")
    public AspectjProceedingJoinPointContextProvider() {
    }

    @SuppressWarnings("unused")
    public AspectjProceedingJoinPointContextProvider(final ProceedingJoinPoint proceedingJoinPoint) {
        this.proceedingJoinPoint = proceedingJoinPoint;
    }

    public final void setContextData(Object instance) throws ClassCastException {
        this.proceedingJoinPoint = (ProceedingJoinPoint) instance;
    }

    public final Class<ProceedingJoinPoint> getWrappedType() {
        return ProceedingJoinPoint.class;
    }

    public static AspectjProceedingJoinPointContextProvider wrap(final ProceedingJoinPoint proceedingJoinPoint) {
        return new AspectjProceedingJoinPointContextProvider(proceedingJoinPoint);
    }

    @SuppressWarnings("unused")
    @TraceeContextProviderMethod(displayName = "class", order = 20)
    public final String getClazz() {
        if (proceedingJoinPoint != null && proceedingJoinPoint.getSignature() != null) {
            return proceedingJoinPoint.getSignature().getDeclaringTypeName();
        }
        return null;
    }

    @SuppressWarnings("unused")
    @TraceeContextProviderMethod(displayName = "method", order = 30)
    public final String getMethod() {
        if (proceedingJoinPoint != null && proceedingJoinPoint.getSignature() != null) {
            return proceedingJoinPoint.getSignature().getName();
        }
        return null;
    }

    @SuppressWarnings("unused")
    @TraceeContextProviderMethod(displayName = "parameters", order = 40)
    public final List<String> getParameters() {

        if (proceedingJoinPoint != null && proceedingJoinPoint.getArgs() != null) {
            // output parameters
            final List<String> parameters = new ArrayList<String>();
            for (final Object attr : proceedingJoinPoint.getArgs()) {

                if (attr != null && TraceeContextLoggerConstants.IGNORED_AT_DESERIALIZATION.contains(attr.getClass())) {
                    parameters.add(attr.toString());
                } else {
                    parameters.add(attr == null ? null : ReflectionToStringBuilder.reflectionToString(attr, new RecursiveReflectionToStringStyle()));
                }
            }

            return parameters;
        }
        return null;
    }

    @SuppressWarnings("unused")
    @TraceeContextProviderMethod(displayName = "serialized-target-instance",
            order = 50)
    public final String getSerializedTargetInstance() {
        if (proceedingJoinPoint != null) {
            // output invoked instance
            String deSerializedInstance;
            Object targetInstance = proceedingJoinPoint.getTarget();
            if (targetInstance != null) {
                deSerializedInstance = ReflectionToStringBuilder.reflectionToString(targetInstance, new RecursiveReflectionToStringStyle());
            } else {
                deSerializedInstance = null;
            }
            return deSerializedInstance;
        }

        return null;
    }

}
