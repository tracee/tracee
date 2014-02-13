package de.holisticon.util.tracee.jaxws.container;


import de.holisticon.util.tracee.TraceeConstants;
import de.holisticon.util.tracee.TraceeLogger;
import de.holisticon.util.tracee.Utilities;
import de.holisticon.util.tracee.configuration.TraceeFilterConfiguration;
import de.holisticon.util.tracee.jaxws.AbstractTraceeHandler;
import de.holisticon.util.tracee.jaxws.TraceeWsHandlerConstants;
import de.holisticon.util.tracee.jaxws.protocol.SoapHeaderTransport;

import javax.xml.namespace.QName;
import javax.xml.soap.*;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TraceeServerHandler extends AbstractTraceeHandler {

    private final TraceeLogger traceeLogger = getTraceeBackend().getLoggerFactory().getLogger(TraceeServerHandler.class);

	private final SoapHeaderTransport transportSerialization = new SoapHeaderTransport();

    protected final void handleIncoming(SOAPMessageContext context) {
        try {
            final SOAPMessage msg = context.getMessage();
            final SOAPEnvelope env = msg.getSOAPPart().getEnvelope();

            // get soap header
            final SOAPHeader header = env.getHeader();
            if (header != null && getTraceeBackend().getConfiguration().shouldProcessContext(TraceeFilterConfiguration.MessageType.IncomingRequest)) {
				final Map<String, String> parsed = transportSerialization.parse(header);
				getTraceeBackend().putAll(parsed);
			}

            // generate request id if it doesn't exist
            if (getTraceeBackend().get(TraceeConstants.REQUEST_ID_KEY) == null &&
					getTraceeBackend().getConfiguration().generatedRequestIdLength() > 0) {
                getTraceeBackend().put(TraceeConstants.REQUEST_ID_KEY,
						Utilities.createRandomAlphanumeric(getTraceeBackend().getConfiguration().generatedRequestIdLength()));
            }

        } catch (final SOAPException e) {
            traceeLogger.error("TraceeServerHandler - Error during precessing of inbound soap header");
        }
    }

    protected final void handleOutgoing(SOAPMessageContext context) {
        try {
            final SOAPMessage msg = context.getMessage();
            if (msg != null && getTraceeBackend().getConfiguration().shouldProcessContext(TraceeFilterConfiguration.MessageType.OutgoingResponse)) {
                try {
                    final SOAPEnvelope env = msg.getSOAPPart().getEnvelope();

                    // get or create header
                    SOAPHeader header = env.getHeader();
                    if (header == null) {
                        header = env.addHeader();
                    }

					transportSerialization.renderTo(getTraceeBackend(), header);
                    msg.saveChanges();
                    context.setMessage(msg);
                } catch (final SOAPException e) {
                    traceeLogger.error("TraceeServerHandler : Exception "
                            + "occurred during processing of outbound message.", e);
                }
            }

        } finally {
            // must reset tracee context
            getTraceeBackend().clear();
        }
    }

    @Override
    public final boolean handleFault(SOAPMessageContext context) {
        this.handleOutgoing(context);
        return true;
    }

    @Override
    public final Set<QName> getHeaders() {
        HashSet<QName> set = new HashSet<QName>();
        set.add(TraceeWsHandlerConstants.TRACEE_SOAP_HEADER_QNAME);
        return set;
    }
}
