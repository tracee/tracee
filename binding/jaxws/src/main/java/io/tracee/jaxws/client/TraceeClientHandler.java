package io.tracee.jaxws.client;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.TraceeLogger;
import io.tracee.jaxws.AbstractTraceeHandler;
import io.tracee.transport.SoapHeaderTransport;

import javax.xml.bind.JAXBException;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.util.Map;

import static io.tracee.configuration.TraceeFilterConfiguration.Channel.IncomingResponse;
import static io.tracee.configuration.TraceeFilterConfiguration.Channel.OutgoingRequest;

public class TraceeClientHandler extends AbstractTraceeHandler {

	private final TraceeLogger traceeLogger;

	private final SoapHeaderTransport transportSerialization = new SoapHeaderTransport();

	public TraceeClientHandler() {
		this(Tracee.getBackend());
	}

	public TraceeClientHandler(TraceeBackend traceeBackend) {
		super(traceeBackend);
		traceeLogger = traceeBackend.getLoggerFactory().getLogger(TraceeClientHandler.class);
	}

	@Override
	public final boolean handleFault(final SOAPMessageContext context) {
		return true;
	}

	protected final void handleIncoming(final SOAPMessageContext context) {

		final SOAPMessage msg = context.getMessage();
		if (msg != null && traceeBackend.getConfiguration().shouldProcessContext(OutgoingRequest)) {

			try {
				final SOAPEnvelope env = msg.getSOAPPart().getEnvelope();

				// get soap header
				final SOAPHeader header = env.getHeader();

				if (header != null) {
					final Map<String, String> parsedContext = transportSerialization.parseSoapHeader(header);
					traceeBackend.putAll(traceeBackend.getConfiguration().filterDeniedParams(parsedContext, OutgoingRequest));
				}

			} catch (final SOAPException e) {
				traceeLogger.error("TraceeClientHandler : Exception occurred during processing of inbound message.", e);
			} catch (JAXBException e) {
				traceeLogger.error("TraceeClientHandler : Exception occurred during processing of inbound message.", e);
			}

		}
	}


	protected final void handleOutgoing(final SOAPMessageContext context) {

		final SOAPMessage msg = context.getMessage();
		if (msg != null && traceeBackend.getConfiguration().shouldProcessContext(IncomingResponse)) {

			try {
				final SOAPEnvelope env = msg.getSOAPPart().getEnvelope();
				// get or create header
				SOAPHeader header = env.getHeader();
				if (header == null) {
					header = env.addHeader();
				}

				final Map<String, String> filteredContext = traceeBackend.getConfiguration().filterDeniedParams(traceeBackend.copyToMap(), IncomingResponse);
				transportSerialization.renderSoapHeader(filteredContext, header);

				msg.saveChanges();

			} catch (final SOAPException e) {
				traceeLogger.error("TraceeClientHandler : Exception occurred during processing of outbound message.", e);
			} catch (JAXBException e) {
				traceeLogger.error("TraceeClientHandler : Exception occurred during processing of outbound message.", e);
			}

			context.setMessage(msg);
		}
	}
}
