package io.tracee.binding.springws;

import io.tracee.TraceeBackend;
import io.tracee.TraceeLogger;
import io.tracee.configuration.TraceeFilterConfiguration;
import io.tracee.configuration.TraceeFilterConfiguration.Channel;
import io.tracee.transport.SoapHeaderTransport;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.soap.SoapHeader;
import org.springframework.ws.soap.SoapHeaderElement;
import org.springframework.ws.soap.SoapMessage;

import javax.xml.bind.JAXBException;
import java.util.Iterator;
import java.util.Map;

import static io.tracee.TraceeConstants.SOAP_HEADER_QNAME;

abstract class AbstractTraceeInterceptor {

	protected static final SoapHeaderTransport soapHeaderTransport = new SoapHeaderTransport();

	protected final TraceeBackend backend;

	protected String profile;

	protected final TraceeLogger traceeLogger;

	public AbstractTraceeInterceptor(final TraceeBackend backend, final String profile) {
		this.backend = backend;
		this.profile = profile;
		this.traceeLogger = backend.getLoggerFactory().getLogger(this.getClass());
	}

	protected void parseContextFromSoapHeader(final WebServiceMessage message, final Channel channel) throws JAXBException {
		if (message instanceof SoapMessage) {
			final SoapMessage soapMessage = (SoapMessage) message;

			final TraceeFilterConfiguration filterConfiguration = backend.getConfiguration(profile);

			if (filterConfiguration.shouldProcessContext(channel)) {
				final SoapHeader soapHeader = soapMessage.getSoapHeader();
				if (soapHeader != null) {
					final Iterator<SoapHeaderElement> tpicHeaders = soapHeader.examineHeaderElements(SOAP_HEADER_QNAME);
					if (tpicHeaders.hasNext()) {
						final Map<String, String> parsedTpic = soapHeaderTransport.parseTpicHeader(tpicHeaders.next().getSource());
						backend.putAll(filterConfiguration.filterDeniedParams(parsedTpic, channel));
					}
				}
			}
		} else {
			traceeLogger.info("Message is obviously no soap message - Not instance of Spring-WS SoapMessage");
		}
	}

	protected void serializeContextToSoapHeader(final WebServiceMessage message, final Channel channel) {
		if (message instanceof SoapMessage) {
			final SoapMessage soapMessage = (SoapMessage) message;

			final TraceeFilterConfiguration filterConfiguration = backend.getConfiguration(profile);

			if (!backend.isEmpty() && filterConfiguration.shouldProcessContext(channel)) {
				try {
					final SoapHeader soapHeader = soapMessage.getSoapHeader();
					if (soapHeader != null) {

						final Map<String, String> context = filterConfiguration.filterDeniedParams(backend.copyToMap(), channel);
						soapHeaderTransport.renderSoapHeaderToResult(context, soapHeader.getResult());
					}
				} catch (JAXBException e) {
					traceeLogger.warn("Can't write TPIC header", e);
				}
			}
		} else {
			traceeLogger.info("Message is obviously no soap message - Not instance of Spring-WS SoapMessage");
		}
	}
}
