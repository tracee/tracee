package io.tracee.contextlogger.contextprovider.tracee;

import io.tracee.contextlogger.api.TraceeContextProvider;
import io.tracee.contextlogger.api.TraceeContextProviderMethod;
import io.tracee.contextlogger.api.WrappedContextData;
import io.tracee.contextlogger.contextprovider.Order;

/**
 * Message Context Provider. Used to add message to tracee log output.
 * Does call toString method on wrapped instance.
 */
@SuppressWarnings("unused")
@TraceeContextProvider(displayName = "message", order = Order.MESSAGE)
public class TraceeMessageContextProvider implements WrappedContextData<TraceeMessage> {

    private TraceeMessage message;

    /**
     * No-Args constructor
     */
    public TraceeMessageContextProvider() {

    }

    /**
     * Constructor that allows to set message directly.
     *
     * @param message the message instance to set
     */
    public TraceeMessageContextProvider(final TraceeMessage message) {
        this.message = message;
    }

    @Override
    public void setContextData(Object instance) throws ClassCastException {
        message = (TraceeMessage) instance;
    }

    @Override
    public Class<TraceeMessage> getWrappedType() {
        return TraceeMessage.class;
    }

    @SuppressWarnings("unused")
    @TraceeContextProviderMethod(displayName = "value", order = 10, enabledPerDefault = true)
    public String getValue() {
        if (message != null && message.getMessage() != null) {
            return message.getMessage().toString();
        }
        return null;
    }

}
