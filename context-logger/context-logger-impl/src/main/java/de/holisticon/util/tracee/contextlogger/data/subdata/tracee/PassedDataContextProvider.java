package de.holisticon.util.tracee.contextlogger.data.subdata.tracee;

import de.holisticon.util.tracee.contextlogger.api.Flatten;
import de.holisticon.util.tracee.contextlogger.api.TraceeContextLogProvider;
import de.holisticon.util.tracee.contextlogger.api.TraceeContextLogProviderMethod;
import de.holisticon.util.tracee.contextlogger.api.WrappedContextData;
import de.holisticon.util.tracee.contextlogger.data.subdata.NameObjectValuePair;
import de.holisticon.util.tracee.contextlogger.utility.PassedContextDataElementWrapper;
import de.holisticon.util.tracee.contextlogger.utility.PassedContextDataElementWrapperComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ContextDataProvider for all instances passed to {@link de.holisticon.util.tracee.contextlogger.data.subdata.tracee.TraceeContextProvider}
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
