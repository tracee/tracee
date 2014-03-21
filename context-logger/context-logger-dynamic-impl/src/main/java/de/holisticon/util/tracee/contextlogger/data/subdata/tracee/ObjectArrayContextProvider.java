package de.holisticon.util.tracee.contextlogger.data.subdata.tracee;

import de.holisticon.util.tracee.contextlogger.api.Flatten;
import de.holisticon.util.tracee.contextlogger.api.TraceeContextLogProvider;
import de.holisticon.util.tracee.contextlogger.api.TraceeContextLogProviderMethod;
import de.holisticon.util.tracee.contextlogger.api.WrappedContextData;
import de.holisticon.util.tracee.contextlogger.data.subdata.NameObjectValuePair;
import de.holisticon.util.tracee.contextlogger.utility.RecursiveReflectionToStringStyle;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * ContextDataProvider for all instances passed to {@link de.holisticon.util.tracee.contextlogger.data.subdata.tracee.TraceeContextProvider}
 * Created by Tobias Gindler, holisticon AG on 20.03.14.
 */

@TraceeContextLogProvider(displayName = "contexts")
public class ObjectArrayContextProvider implements WrappedContextData<Object[]>{

    private Object[] instances;

    public ObjectArrayContextProvider () {

    }

    public ObjectArrayContextProvider (Object[] instances) {
        this.instances = instances;
    }


    @Override
    public void setContextData(Object instance) throws ClassCastException {
        this.instances = (Object[]) instance;
    }

    @Override
    public Class<Object[]> getWrappedType() {
        return Object[].class;
    }

    @Flatten
    @TraceeContextLogProviderMethod(displayName = "instances", propertyName = "")
    public List<NameObjectValuePair> getContextData() {

        if (instances == null) {
            return null;
        }

        List<NameObjectValuePair> nameObjectValuePairs = new ArrayList<NameObjectValuePair>();


        for (Object entry :  instances) {
            if (entry != null) {
                nameObjectValuePairs.add(new NameObjectValuePair(entry.getClass().getName(), entry));
            }
        }

        return nameObjectValuePairs.size() > 0 ? nameObjectValuePairs : null;
    }
}
