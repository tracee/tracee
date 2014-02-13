package de.holisticon.util.tracee.jaxws;

import de.holisticon.util.tracee.Tracee;
import de.holisticon.util.tracee.TraceeBackend;

import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

/**
 * @author Tobias Gindler (Holisticon AG)
 */
public abstract class AbstractTraceeHandler implements SOAPHandler<SOAPMessageContext> {

    private final TraceeBackend traceeBackend = Tracee.getBackend();

    @Override
    public final boolean handleMessage(final SOAPMessageContext context) {
        if (this.isOutgoing(context)) {
			this.handleOutgoing(context);
        } else {
			this.handleIncoming(context);
        }
        return true;
    }

    private boolean isOutgoing(MessageContext messageContext) {
        return (Boolean) messageContext.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
    }

    @Override
    public final void close(MessageContext context) {

    }

    protected final TraceeBackend getTraceeBackend() {
        return traceeBackend;
    }

    protected abstract void handleIncoming(SOAPMessageContext context);

    protected abstract void handleOutgoing(SOAPMessageContext context);
}
