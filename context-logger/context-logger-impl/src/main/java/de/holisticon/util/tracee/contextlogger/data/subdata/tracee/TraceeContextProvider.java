package de.holisticon.util.tracee.contextlogger.data.subdata.tracee;

import de.holisticon.util.tracee.Tracee;
import de.holisticon.util.tracee.TraceeBackend;
import de.holisticon.util.tracee.contextlogger.ImplicitContext;
import de.holisticon.util.tracee.contextlogger.api.Flatten;
import de.holisticon.util.tracee.contextlogger.api.ImplicitContextData;
import de.holisticon.util.tracee.contextlogger.api.TraceeContextLogProvider;
import de.holisticon.util.tracee.contextlogger.api.TraceeContextLogProviderMethod;
import de.holisticon.util.tracee.contextlogger.data.Order;
import de.holisticon.util.tracee.contextlogger.data.subdata.NameStringValuePair;
import de.holisticon.util.tracee.contextlogger.profile.ProfilePropertyNames;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Common context data provider.
 * Created by Tobias Gindler, holisticon AG on 14.03.14.
 */
@SuppressWarnings("unused")
@TraceeContextLogProvider(displayName = "tracee", order = Order.TRACEE)
public class TraceeContextProvider implements ImplicitContextData {

    private final TraceeBackend traceeBackend;

    public TraceeContextProvider() {
        this.traceeBackend = Tracee.getBackend();
    }

    @Override
    public ImplicitContext getImplicitContext() {
        return ImplicitContext.TRACEE;
    }

    @SuppressWarnings("unused")
    @Flatten
    @TraceeContextLogProviderMethod(
            displayName = "DYNAMIC",
            propertyName = ProfilePropertyNames.TRACEE,
            order = 10)
    public List<NameStringValuePair> getNameValuePairs() {

        final List<NameStringValuePair> list = new ArrayList<NameStringValuePair>();

        final Collection<String> keys = traceeBackend.keySet();
        if (keys != null) {
            for (String key : keys) {

                final String value = traceeBackend.get(key);
                list.add(new NameStringValuePair(key, value));

            }
        }
        return list.size() > 0 ? list : null;

    }

}
