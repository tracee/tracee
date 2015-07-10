package io.tracee.binding.jaxws;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.transport.SoapHeaderTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.util.Map;

import static io.tracee.configuration.TraceeFilterConfiguration.Channel.IncomingResponse;
import static io.tracee.configuration.TraceeFilterConfiguration.Channel.OutgoingRequest;

public class TraceeClientHandler extends AbstractTraceeHandler {

	private static final Logger logger = LoggerFactory.getLogger(TraceeClientHandler.class);

	private final SoapHeaderTransport transportSerialization = new SoapHeaderTransport();

	public TraceeClientHandler() {
		this(Tracee.getBackend());
	}

	public TraceeClientHandler(TraceeBackend traceeBackend) {
		super(traceeBackend);
	}

	@Override
	public final boolean handleFault(final SOAPMessageContext context) {
		return true;
	}

	protected final void handleIncoming(final SOAPMessageContext context) {

		final SOAPMessage msg = context.getMessage();
		if (msg != null && traceeBackend.getConfiguration().shouldProcessContext(OutgoingRequest)) {

			try {
				final SOAPHeader header = msg.getSOAPHeader();

				if (header != null) {
					final Map<String, String> parsedContext = transportSerialization.parseSoapHeader(header);
					traceeBackend.putAll(traceeBackend.getConfiguration().filterDeniedParams(parsedContext, OutgoingRequest));
				}
			} catch (final SOAPException e) {
				logger.warn("Error during precessing of inbound soap header: " + e.getMessage());
				logger.debug("Detailed: Error during precessing of inbound soap header: {}", e.getMessage(), e);
			}
		}
	}

	protected final void handleOutgoing(final SOAPMessageContext context) {

		final SOAPMessage msg = context.getMessage();
		if (msg != null && !traceeBackend.isEmpty() && traceeBackend.getConfiguration().shouldProcessContext(IncomingResponse)) {

			try {
				final SOAPHeader header = getOrCreateHeader(msg);

				final Map<String, String> filteredContext = traceeBackend.getConfiguration().filterDeniedParams(traceeBackend.copyToMap(), IncomingResponse);
				transportSerialization.renderSoapHeader(filteredContext, header);

				msg.saveChanges();
			} catch (final SOAPException e) {
				logger.warn("TraceeClientHandler : Exception occurred during processing of outbound message.", e);
			}
		}
	}
}
