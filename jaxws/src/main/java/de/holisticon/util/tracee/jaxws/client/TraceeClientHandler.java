package de.holisticon.util.tracee.jaxws.client;

import de.holisticon.util.tracee.TraceeBackend;
import de.holisticon.util.tracee.TraceeLogger;
import de.holisticon.util.tracee.jaxws.AbstractTraceeHandler;
import de.holisticon.util.tracee.jaxws.TraceeWsHandlerConstants;
import de.holisticon.util.tracee.jaxws.protocol.SoapHeaderTransport;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static de.holisticon.util.tracee.configuration.TraceeFilterConfiguration.Channel.IncomingResponse;
import static de.holisticon.util.tracee.configuration.TraceeFilterConfiguration.Channel.OutgoingRequest;

public class TraceeClientHandler extends AbstractTraceeHandler {

    private final TraceeLogger traceeLogger = getTraceeBackend().getLoggerFactory().getLogger(TraceeClientHandler.class);

	private final SoapHeaderTransport transportSerialization = new SoapHeaderTransport();

    public final Set<QName> getHeaders() {
        HashSet<QName> set = new HashSet<QName>();
        set.add(TraceeWsHandlerConstants.TRACEE_SOAP_HEADER_QNAME);
        return set;
    }

    @Override
    public final boolean handleFault(final SOAPMessageContext context) {
        return true;
    }

    protected final void handleIncoming(final SOAPMessageContext context) {

        final SOAPMessage msg = context.getMessage();
		final TraceeBackend backend = getTraceeBackend();
        if (msg != null && backend.getConfiguration().shouldProcessContext(OutgoingRequest)) {

            try {
                final SOAPEnvelope env = msg.getSOAPPart().getEnvelope();

                // get soap header
                final SOAPHeader header = env.getHeader();

				if (header != null) {
					final Map<String, String> parsedContext = transportSerialization.parse(header);
					final Map<String, String> filteredContext = backend.getConfiguration().filterDeniedParams(parsedContext, OutgoingRequest);
					getTraceeBackend().putAll(filteredContext);
				}

            } catch (final SOAPException e) {
                e.printStackTrace();
                traceeLogger.error(
                        "TraceeClientHandler : Exception occurred during processing of inbound message.", e);
            }

        }
    }


    protected final void handleOutgoing(final SOAPMessageContext context) {

        final SOAPMessage msg = context.getMessage();
        final TraceeBackend backend = getTraceeBackend();
		if (msg != null && backend.getConfiguration().shouldProcessContext(IncomingResponse)) {

            try {
                final SOAPEnvelope env = msg.getSOAPPart().getEnvelope();
                // get or create header
                SOAPHeader header = env.getHeader();
                if (header == null) {
                    header = env.addHeader();
                }

				final Map<String, String> filteredContext = backend.getConfiguration().filterDeniedParams(backend, IncomingResponse);
				transportSerialization.renderTo(filteredContext, header);

				msg.saveChanges();

            } catch (final SOAPException e) {
                traceeLogger.error("TraceeClientHandler : Exception "
                        + "occurred during processing of outbound message.", e);
            }

            context.setMessage(msg);

        }
    }



}
