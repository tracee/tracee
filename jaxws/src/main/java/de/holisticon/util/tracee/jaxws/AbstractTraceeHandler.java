package de.holisticon.util.tracee.jaxws;

import de.holisticon.util.tracee.Tracee;
import de.holisticon.util.tracee.TraceeBackend;

import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

/**
 * Created by Tobias Gindler, holisticon AG on 06.12.13.
 */
public abstract class AbstractTraceeHandler implements SOAPHandler<SOAPMessageContext> {

    private final TraceeBackend traceeBackend = Tracee.getBackend();

    @Override
    public final boolean handleMessage(final SOAPMessageContext context) {
        if (!this.isOutbound(context)) {
            this.handleInbound(context);
        } else {
            this.handleOutbound(context);
        }
        return true;
    }

    private boolean isOutbound(MessageContext messageContext) {
        return (Boolean) messageContext.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
    }

    @Override
    public final void close(MessageContext context) {

    }

    protected final TraceeBackend getTraceeBackend() {
        return traceeBackend;
    }

    protected abstract void handleInbound(SOAPMessageContext context);

    protected abstract void handleOutbound(SOAPMessageContext context);
}
