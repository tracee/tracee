package io.tracee.contextlogger.utility;

import io.tracee.contextlogger.api.TraceeContextProvider;
import io.tracee.contextlogger.data.subdata.NameObjectValuePair;

/**
 * Wrapper class used to sort passed instances.
 * Created by Tobias Gindler, holisticon AG on 22.03.14.
 */
public final class PassedContextDataElementWrapper {

    private final Integer order;
    private final NameObjectValuePair nameObjectValuePair;

    public PassedContextDataElementWrapper(NameObjectValuePair nameObjectValuePair) {
        this.nameObjectValuePair = nameObjectValuePair;

        TraceeContextProvider annotation = TraceeContextLogAnnotationUtilities.getAnnotationFromType(nameObjectValuePair.getValue());

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
