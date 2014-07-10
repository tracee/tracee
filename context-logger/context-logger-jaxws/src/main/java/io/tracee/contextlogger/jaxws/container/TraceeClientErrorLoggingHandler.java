package io.tracee.contextlogger.jaxws.container;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.TraceeLogger;
import io.tracee.jaxws.container.TraceeServerHandler;

import javax.xml.ws.handler.soap.SOAPMessageContext;

/**
 * JaxWs client side handler that detects uncaught exceptions and outputs contextual information.
 */
public class TraceeClientErrorLoggingHandler extends AbstractTraceeErrorLoggingHandler {

    TraceeClientErrorLoggingHandler(TraceeBackend traceeBackend) {
        super(traceeBackend);
    }

    public TraceeClientErrorLoggingHandler() {
        this(Tracee.getBackend());
    }

    @Override
    protected final void handleIncoming(SOAPMessageContext context) {
        // Do nothing
    }

    @Override
    protected final void handleOutgoing(SOAPMessageContext context) {
        storeMessageInThreadLocal(context);
    }

}
