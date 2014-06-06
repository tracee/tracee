package io.tracee.contextlogger.jaxws.container;

import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.util.Set;

/**
 * Test class needed by {@link io.tracee.contextlogger.jaxws.container.TraceeClientHandlerResolverTest}.
 */
public class TestHandlerWithoutDefaultConstructor implements SOAPHandler<SOAPMessageContext> {

    public TestHandlerWithoutDefaultConstructor (String anyParameter) {

    }


    @Override
    public Set<QName> getHeaders() {
        return null;
    }

    @Override
    public boolean handleMessage(SOAPMessageContext context) {
        return false;
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        return false;
    }

    @Override
    public void close(MessageContext context) {

    }
}