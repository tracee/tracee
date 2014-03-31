package de.holisticon.util.tracee.jaxws.container;


import de.holisticon.util.tracee.*;
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

import static de.holisticon.util.tracee.configuration.TraceeFilterConfiguration.Channel.IncomingRequest;
import static de.holisticon.util.tracee.configuration.TraceeFilterConfiguration.Channel.OutgoingResponse;

public class TraceeServerHandler extends AbstractTraceeHandler {

    private final TraceeLogger traceeLogger = getTraceeBackend().getLoggerFactory().getLogger(TraceeServerHandler.class);

	private final SoapHeaderTransport transportSerialization = new SoapHeaderTransport();

	public TraceeServerHandler() {
		this(Tracee.getBackend());
	}

	TraceeServerHandler(TraceeBackend traceeBackend) {
		super(traceeBackend);
	}

	protected final void handleIncoming(SOAPMessageContext context) {
        try {
            final SOAPMessage msg = context.getMessage();
            final SOAPEnvelope env = msg.getSOAPPart().getEnvelope();

            // get soap header
            final SOAPHeader header = env.getHeader();

			final TraceeBackend backend = getTraceeBackend();

            if (header != null && backend.getConfiguration().shouldProcessContext(IncomingRequest)) {
				final Map<String, String> parsedContext = transportSerialization.parse(header);
				final Map<String, String> filteredContext = backend.getConfiguration().filterDeniedParams(parsedContext, IncomingRequest);
				getTraceeBackend().putAll(filteredContext);

			}

            // generate request id if it doesn't exist
            if (getTraceeBackend().get(TraceeConstants.REQUEST_ID_KEY) == null &&
					getTraceeBackend().getConfiguration().shouldGenerateRequestId()) {
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
			final TraceeBackend backend = getTraceeBackend();
            if (msg != null && backend.getConfiguration().shouldProcessContext(OutgoingResponse)) {
                try {
                    final SOAPEnvelope env = msg.getSOAPPart().getEnvelope();

                    // get or create header
                    SOAPHeader header = env.getHeader();
                    if (header == null) {
                        header = env.addHeader();
                    }

					final Map<String, String> filteredContext = backend.getConfiguration().filterDeniedParams(backend, OutgoingResponse);
					transportSerialization.renderTo(filteredContext, header);

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
}
