package de.holisticon.util.tracee.contextlogger.data.subdata.aspectj;

import de.holisticon.util.tracee.contextlogger.api.TraceeContextLogProvider;
import de.holisticon.util.tracee.contextlogger.api.TraceeContextLogProviderMethod;
import de.holisticon.util.tracee.contextlogger.api.WrappedContextData;
import de.holisticon.util.tracee.contextlogger.profile.ProfilePropertyNames;
import de.holisticon.util.tracee.contextlogger.utility.RecursiveReflectionToStringStyle;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.aspectj.lang.ProceedingJoinPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Context provider for ProceedingJoinPoint.
 * Created by Tobias Gindler, holisticon AG on 20.03.14.
 */
@TraceeContextLogProvider(displayName = "proceedingJoinPoint")
public class AspectjProceedingJoinPoint implements WrappedContextData<ProceedingJoinPoint> {

    private ProceedingJoinPoint proceedingJoinPoint;

    @SuppressWarnings("unused")
    public AspectjProceedingJoinPoint() {
    }

    @SuppressWarnings("unused")
    public AspectjProceedingJoinPoint(final ProceedingJoinPoint proceedingJoinPoint) {
        this.proceedingJoinPoint = proceedingJoinPoint;
    }

    public void setContextData(Object instance) throws ClassCastException {
        this.proceedingJoinPoint = (ProceedingJoinPoint) instance;
    }

    public Class<ProceedingJoinPoint> getWrappedType () {
        return ProceedingJoinPoint.class;
    }

    @SuppressWarnings("unused")
    @TraceeContextLogProviderMethod(
            displayName = "class",
            propertyName = ProfilePropertyNames.ASPECTJ_PROCEEDING_JOIN_POINT_CLASS,
            order = 20)
    public String getClazz() {
        return proceedingJoinPoint.getSignature().getDeclaringTypeName();
    }

    @SuppressWarnings("unused")
    @TraceeContextLogProviderMethod(
            displayName = "method",
            propertyName = ProfilePropertyNames.ASPECTJ_PROCEEDING_JOIN_POINT_METHOD,
            order = 30)
    public String getMethod() {
        return proceedingJoinPoint.getSignature().getName();
    }

    @SuppressWarnings("unused")
    @TraceeContextLogProviderMethod(
            displayName = "parameters",
            propertyName = ProfilePropertyNames.ASPECTJ_PROCEEDING_JOIN_POINT_PARAMETERS,
            order = 40)
    public List<String> getParameters() {

        // output parameters
        final List<String> parameters = new ArrayList<String>();
        for (final Object attr : proceedingJoinPoint.getArgs()) {
            parameters.add(attr == null ? null : ReflectionToStringBuilder.reflectionToString(attr, new RecursiveReflectionToStringStyle()));
        }

        return parameters;
    }

    @SuppressWarnings("unused")
    @TraceeContextLogProviderMethod(
            displayName = "deserialized-instance",
            propertyName = ProfilePropertyNames.ASPECTJ_PROCEEDING_JOIN_POINT_DESERIALIZED_INSTANCE,
            order = 50)
    public String getDeserializedInstance() {
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

}
