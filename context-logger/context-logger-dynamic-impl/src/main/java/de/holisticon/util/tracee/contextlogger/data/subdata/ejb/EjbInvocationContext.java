package de.holisticon.util.tracee.contextlogger.data.subdata.ejb;

import de.holisticon.util.tracee.contextlogger.api.TraceeContextLogProvider;
import de.holisticon.util.tracee.contextlogger.api.TraceeContextLogProviderMethod;
import de.holisticon.util.tracee.contextlogger.data.subdata.NameValuePair;
import de.holisticon.util.tracee.contextlogger.profile.ProfilePropertyNames;
import de.holisticon.util.tracee.contextlogger.utility.RecursiveReflectionToStringStyle;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import javax.interceptor.InvocationContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Context provider for ProceedingJoinPoint.
 * Created by Tobias Gindler, holisticon AG on 20.03.14.
 */
@TraceeContextLogProvider(displayName = "invocationContext")
public class EjbInvocationContext {

    private final InvocationContext invocationContext;

    public EjbInvocationContext (final InvocationContext invocationContext) {
        this.invocationContext = invocationContext;
    }

    @TraceeContextLogProviderMethod(displayName = "methodName", propertyName = ProfilePropertyNames.EJB_INVOCATION_CONTEXT_METHOD_NAME,order = 10)
    public String getMethodName() {
        return this.invocationContext.getMethod().getName();
    }

    @TraceeContextLogProviderMethod(displayName = "parameters", propertyName = ProfilePropertyNames.EJB_INVOCATION_CONTEXT_PARAMETERS,order = 20)
    public List<String> getParameters() {

        List<String> result = new ArrayList<String>();

        if (this.invocationContext != null) {
            for (Object parameter : invocationContext.getParameters()) {

                result.add(parameter != null ? parameter.toString() : null);

            }
        }

        return result.size() > 0 ? result : null;
    }

    @TraceeContextLogProviderMethod(displayName = "deserialized.targetInstance", propertyName = ProfilePropertyNames.EJB_INVOCATION_CONTEXT_TARGET_INSTANCE,order = 30)
    public String getTargetInstance() {
        String result = null;
        if (this.invocationContext != null) {
            Object targetInstance = this.invocationContext.getTarget();
            if (targetInstance != null) {
                result = ReflectionToStringBuilder.reflectionToString(targetInstance, new RecursiveReflectionToStringStyle());
            } else {
                result = null;
            }
        }

        return result;
    }

    @TraceeContextLogProviderMethod(displayName = "deserialized.contextData", propertyName = ProfilePropertyNames.EJB_INVOCATION_CONTEXT_DATA,order = 40)
    public List<NameValuePair> getContextData() {

        List<NameValuePair> result = new ArrayList<NameValuePair>();

        if (this.invocationContext != null) {

            for (Map.Entry<String,Object> entry : this.invocationContext.getContextData().entrySet()) {

                String key = entry.getKey();
                Object value = entry.getValue();

                final String deSerializedValue;
                if (value != null) {
                    deSerializedValue = ReflectionToStringBuilder.reflectionToString(value, new RecursiveReflectionToStringStyle());
                } else {
                    deSerializedValue = null;
                }

                result.add(new NameValuePair(key, deSerializedValue));


            }

        }

        return result.size() > 0 ? result : null;

    }

}
