package de.holisticon.util.tracee.jaxws;

import de.holisticon.util.tracee.TraceeBackend;

import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Tobias Gindler (Holisticon AG)
 */
public abstract class AbstractTraceeHandler implements SOAPHandler<SOAPMessageContext> {

	public AbstractTraceeHandler(TraceeBackend traceeBackend) {
		this.traceeBackend = traceeBackend;
	}

    private final TraceeBackend traceeBackend;
	private final Set<QName> HANDLED_HEADERS = Collections.unmodifiableSet(new HashSet<QName>(Collections.singleton(TraceeWsHandlerConstants.TRACEE_SOAP_HEADER_QNAME)));

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

	@Override
	public Set<QName> getHeaders() {
		return HANDLED_HEADERS;
	}
}
