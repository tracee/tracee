package de.holisticon.util.tracee.contextlogger.utility;

import de.holisticon.util.tracee.contextlogger.api.TraceeContextLogProvider;
import de.holisticon.util.tracee.contextlogger.data.subdata.NameObjectValuePair;

/**
 * Wrapper class used to sort passed instances.
 * Created by Tobias Gindler, holisticon AG on 22.03.14.
 */
public final class PassedContextDataElementWrapper {

    private final Integer order;
    private final NameObjectValuePair nameObjectValuePair;

    public PassedContextDataElementWrapper(NameObjectValuePair nameObjectValuePair) {
        this.nameObjectValuePair = nameObjectValuePair;

        TraceeContextLogProvider annotation = TraceeContextLogAnnotationUtilities.getAnnotationFromType(nameObjectValuePair.getValue());

        if (annotation != null) {
            order = annotation.order();
        } else {
            order = null;
        }

    }

    public NameObjectValuePair getNameObjectValuePair() {
        return nameObjectValuePair;
    }

    public Integer getOrder() {
        return order;
    }

}
