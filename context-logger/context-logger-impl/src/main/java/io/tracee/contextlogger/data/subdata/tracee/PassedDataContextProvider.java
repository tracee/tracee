package io.tracee.contextlogger.data.subdata.tracee;

import io.tracee.contextlogger.api.Flatten;
import io.tracee.contextlogger.api.TraceeContextLogProvider;
import io.tracee.contextlogger.api.TraceeContextLogProviderMethod;
import io.tracee.contextlogger.api.WrappedContextData;
import io.tracee.contextlogger.data.subdata.NameObjectValuePair;
import io.tracee.contextlogger.utility.PassedContextDataElementWrapper;
import io.tracee.contextlogger.utility.PassedContextDataElementWrapperComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ContextDataProvider for all instances passed to {@link io.tracee.contextlogger.data.subdata.tracee.TraceeContextProvider}
 * Created by Tobias Gindler, holisticon AG on 20.03.14.
 */

@TraceeContextLogProvider(displayName = "contexts")
public class PassedDataContextProvider implements WrappedContextData<Object[]> {

    private Object[] instances;

    public PassedDataContextProvider() {

    }

    public PassedDataContextProvider(Object[] instances) {
        this.instances = instances;
    }


    @Override
    public final void setContextData(Object instance) throws ClassCastException {
        this.instances = (Object[]) instance;
    }

    @Override
    public final Class<Object[]> getWrappedType() {
        return Object[].class;
    }

    @Flatten
    @TraceeContextLogProviderMethod(displayName = "instances", propertyName = "")
    public List<NameObjectValuePair> getContextData() {

        if (instances == null) {
            return null;
        }

        // sort NameObjectValuePairs
        List<PassedContextDataElementWrapper> toSortList = new ArrayList<PassedContextDataElementWrapper>();
        for (Object entry : instances) {

            if (entry != null) {
                toSortList.add(new PassedContextDataElementWrapper(new NameObjectValuePair(entry)));
            }

        }
        Collections.sort(toSortList, new PassedContextDataElementWrapperComparator());

        // Now create the List of NameObjectPairs
        List<NameObjectValuePair> nameObjectValuePairs = new ArrayList<NameObjectValuePair>();

        for (PassedContextDataElementWrapper entry :  toSortList) {
            if (entry != null) {
                nameObjectValuePairs.add(entry.getNameObjectValuePair());
            }
        }

        return nameObjectValuePairs.size() > 0 ? nameObjectValuePairs : null;
    }
}
