package io.tracee.jaxws;

import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;

import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractTraceeHandler implements SOAPHandler<SOAPMessageContext> {

	private final TraceeBackend traceeBackend;

	private static final Set<QName> HANDLED_HEADERS = Collections.unmodifiableSet(
			new HashSet<QName>(Collections.singleton(TraceeConstants.TRACEE_SOAP_HEADER_QNAME)));

	public AbstractTraceeHandler(TraceeBackend traceeBackend) {
		this.traceeBackend = traceeBackend;
	}

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
		Object outboundBoolean = messageContext.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		return outboundBoolean != null && (Boolean) outboundBoolean;
    }

    @Override
    public void close(MessageContext context) {

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
