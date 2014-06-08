package io.tracee.contextlogger.jaxws.container;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.TraceeLogger;
import io.tracee.jaxws.container.TraceeServerHandler;

import javax.xml.ws.handler.soap.SOAPMessageContext;

/**
 * JaxWs container side handler that detects uncaught exceptions and outputs contextual information.
 */
public class TraceeServerErrorLoggingHandler extends AbstractTraceeErrorLoggingHandler {

    TraceeServerErrorLoggingHandler(TraceeBackend traceeBackend) {
        super(traceeBackend);
    }

    public TraceeServerErrorLoggingHandler() {
        this(Tracee.getBackend());
    }


    @Override
    protected final void handleIncoming(SOAPMessageContext context) {
        storeMessageInThreadLocal(context);
    }

    @Override
    protected final void handleOutgoing(SOAPMessageContext context) {
        // Do nothing
    }
}
