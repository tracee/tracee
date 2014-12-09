package io.tracee.jaxws.container;


import io.tracee.*;
import io.tracee.jaxws.AbstractTraceeHandler;
import io.tracee.transport.SoapHeaderTransport;

import javax.xml.soap.*;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.util.Map;

import static io.tracee.configuration.TraceeFilterConfiguration.Channel.IncomingRequest;
import static io.tracee.configuration.TraceeFilterConfiguration.Channel.OutgoingResponse;

public class TraceeServerHandler extends AbstractTraceeHandler {

    private final TraceeLogger traceeLogger = getTraceeBackend().getLoggerFactory().getLogger(TraceeServerHandler.class);

	private final SoapHeaderTransport transportSerialization;

	public TraceeServerHandler() {
		this(Tracee.getBackend(), new SoapHeaderTransport());
	}

	TraceeServerHandler(TraceeBackend traceeBackend, SoapHeaderTransport soapHeaderTransport) {
		super(traceeBackend);
		this.transportSerialization = soapHeaderTransport;
	}

	protected final void handleIncoming(SOAPMessageContext context) {
		final SOAPPart soapPart = context.getMessage().getSOAPPart();
		try {
			final TraceeBackend backend = getTraceeBackend();
			final SOAPHeader header = soapPart.getEnvelope().getHeader();


            if (header != null && backend.getConfiguration().shouldProcessContext(IncomingRequest)) {
				final Map<String, String> parsedContext = transportSerialization.parse(header);
				final Map<String, String> filteredContext = backend.getConfiguration().filterDeniedParams(parsedContext, IncomingRequest);
				getTraceeBackend().putAll(filteredContext);

			}

			Utilities.generateRequestIdIfNecessary(backend);

        } catch (final SOAPException e) {
            traceeLogger.error("TraceeServerHandler - Error during precessing of inbound soap header");
        }
    }

    protected final void handleOutgoing(SOAPMessageContext context) {
		final TraceeBackend backend = getTraceeBackend();
		final SOAPMessage msg = context.getMessage();
		try {

			if (msg != null && backend.getConfiguration().shouldProcessContext(OutgoingResponse)) {

				final SOAPEnvelope env = msg.getSOAPPart().getEnvelope();

				// get or create header
				final SOAPHeader header = getOrCreateHeader(env);

				final Map<String, String> filteredContext = backend.getConfiguration().filterDeniedParams(backend, OutgoingResponse);
				transportSerialization.renderTo(filteredContext, header);

				msg.saveChanges();
				context.setMessage(msg);
			}

        } catch (final SOAPException e) {
			traceeLogger.error("TraceeServerHandler : Exception occurred during processing of outbound message.", e);
		} finally {
            // must reset tracee context
            backend.clear();
        }
    }

	SOAPHeader getOrCreateHeader(SOAPEnvelope soapEnvelope) throws SOAPException {
		final SOAPHeader header = soapEnvelope.getHeader();
		if (header != null) {
			return header;
		} else {
			return soapEnvelope.addHeader();
		}
	}

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        this.handleOutgoing(context);
        return true;
    }
}
