package io.tracee.jaxws.container;


import io.tracee.*;
import io.tracee.jaxws.AbstractTraceeHandler;
import io.tracee.transport.SoapHeaderTransport;

import javax.xml.bind.JAXBException;
import javax.xml.soap.*;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.util.Map;

import static io.tracee.configuration.TraceeFilterConfiguration.Channel.IncomingRequest;
import static io.tracee.configuration.TraceeFilterConfiguration.Channel.OutgoingResponse;

public class TraceeServerHandler extends AbstractTraceeHandler {

	private final TraceeLogger traceeLogger;

	private final SoapHeaderTransport transportSerialization;

	public TraceeServerHandler() {
		this(Tracee.getBackend(), new SoapHeaderTransport());
	}

	public TraceeServerHandler(TraceeBackend traceeBackend) {
		this(traceeBackend, new SoapHeaderTransport());
	}

	TraceeServerHandler(TraceeBackend traceeBackend, SoapHeaderTransport soapHeaderTransport) {
		super(traceeBackend);
		this.transportSerialization = soapHeaderTransport;
		traceeLogger = traceeBackend.getLoggerFactory().getLogger(TraceeServerHandler.class);
	}

	protected final void handleIncoming(SOAPMessageContext context) {
		final SOAPPart soapPart = context.getMessage().getSOAPPart();
		try {
			final SOAPHeader header = soapPart.getEnvelope().getHeader();


			if (header != null && traceeBackend.getConfiguration().shouldProcessContext(IncomingRequest)) {
				final Map<String, String> parsedContext = transportSerialization.parseSoapHeader(header);
				final Map<String, String> filteredContext = traceeBackend.getConfiguration().filterDeniedParams(parsedContext, IncomingRequest);
				traceeBackend.putAll(filteredContext);
			}

			Utilities.generateRequestIdIfNecessary(traceeBackend);

		} catch (final SOAPException e) {
			traceeLogger.error("TraceeServerHandler - Error during precessing of inbound soap header");
			traceeLogger.debug("TraceeServerHandler - Error during precessing of inbound soap header", e);
		} catch (JAXBException e) {
			traceeLogger.error("TraceeServerHandler - Error during precessing of inbound soap header");
			traceeLogger.debug("TraceeServerHandler - Error during precessing of inbound soap header", e);
		}
	}

	protected final void handleOutgoing(SOAPMessageContext context) {
		final SOAPMessage msg = context.getMessage();
		try {
			if (msg != null && traceeBackend.getConfiguration().shouldProcessContext(OutgoingResponse)) {

				final SOAPEnvelope env = msg.getSOAPPart().getEnvelope();

				// get or create header
				final SOAPHeader header = getOrCreateHeader(env);

				final Map<String, String> filteredContext = traceeBackend.getConfiguration().filterDeniedParams(traceeBackend, OutgoingResponse);
				transportSerialization.renderSoapHeader(filteredContext, header);

				msg.saveChanges();
				context.setMessage(msg);
			}

		} catch (final SOAPException e) {
			traceeLogger.error("TraceeServerHandler : Exception occurred during processing of outbound message.");
			traceeLogger.debug("TraceeServerHandler : Exception occurred during processing of outbound message.", e);
		} catch (JAXBException e) {
			traceeLogger.error("TraceeServerHandler : Exception occurred during processing of outbound message.");
			traceeLogger.debug("TraceeServerHandler : Exception occurred during processing of outbound message.", e);
		} finally {
			// must reset tracee context
			traceeBackend.clear();
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
