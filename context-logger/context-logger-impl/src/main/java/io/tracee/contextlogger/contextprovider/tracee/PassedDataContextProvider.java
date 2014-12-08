package io.tracee.contextlogger.contextprovider.tracee;

import io.tracee.contextlogger.api.Flatten;
import io.tracee.contextlogger.api.TraceeContextProviderMethod;
import io.tracee.contextlogger.api.WrappedContextData;
import io.tracee.contextlogger.contextprovider.utility.NameObjectValuePair;
import io.tracee.contextlogger.utility.PassedContextDataElementWrapper;
import io.tracee.contextlogger.utility.PassedContextDataElementWrapperComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ContextDataProvider for all instances passed to {@link TraceeContextProvider}
 * Created by Tobias Gindler, holisticon AG on 20.03.14.
 */

@io.tracee.contextlogger.api.TraceeContextProvider(displayName = "contexts")
public class PassedDataContextProvider implements WrappedContextData<Object[]> {

    private Object[] instances;

    private boolean keepOrder = false;

    public PassedDataContextProvider() {

    }

    public PassedDataContextProvider(Object[] instances, boolean keepOrder) {
        this.instances = instances;
        this.keepOrder = keepOrder;
    }

    public PassedDataContextProvider(Object[] instances) {
        this(instances, false);
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
    @TraceeContextProviderMethod(displayName = "instances", enabledPerDefault = true)
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
        if (!keepOrder) {
            Collections.sort(toSortList, new PassedContextDataElementWrapperComparator());
        }

        // Now create the List of NameObjectPairs
        List<NameObjectValuePair> nameObjectValuePairs = new ArrayList<NameObjectValuePair>();

        for (PassedContextDataElementWrapper entry : toSortList) {
            if (entry != null) {
                nameObjectValuePairs.add(entry.getNameObjectValuePair());
            }
        }

        return nameObjectValuePairs.size() > 0 ? nameObjectValuePairs : null;
    }
}
