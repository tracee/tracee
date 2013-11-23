package de.holisticon.util.tracee.jaxws;


import de.holisticon.util.tracee.Tracee;
import de.holisticon.util.tracee.TraceeBackend;

import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPMessageContext;

public class TraceeHandler implements Handler<SOAPMessageContext> {


    private final TraceeBackend traceeBackend = Tracee.getBackend();

    @Override
    public final boolean handleMessage(SOAPMessageContext context) {
        if (isOutbound(context)) {
            handleInbound();
        } else {
            handleOutbound();
        }

        return true;
    }

    private boolean isOutbound(MessageContext messageContext) {
        return (Boolean) messageContext.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
    }

    private void handleInbound() {
        //TODO continue
    }

    private void handleOutbound() {
        //TODO continue
    }

    @Override
    public final boolean handleFault(SOAPMessageContext context) {


        return true;
    }

    @Override
    public final void close(MessageContext context) {

    }
}
