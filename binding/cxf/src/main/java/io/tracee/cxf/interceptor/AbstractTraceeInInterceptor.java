package io.tracee.cxf.interceptor;

import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import io.tracee.configuration.TraceeFilterConfiguration;
import io.tracee.transport.HttpJsonHeaderTransport;
import io.tracee.transport.SoapHeaderTransport;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.headers.Header;
import org.apache.cxf.helpers.CastUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageUtils;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import javax.xml.bind.JAXBException;
import java.util.List;
import java.util.Map;

abstract class AbstractTraceeInInterceptor extends AbstractPhaseInterceptor<Message> {
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractTraceeInInterceptor.class);
	private final TraceeBackend backend;

	private final HttpJsonHeaderTransport httpJsonSerializer;
	private final SoapHeaderTransport httpSoapSerializer;

	private String profile;

	private final TraceeFilterConfiguration.Channel channel;

	public AbstractTraceeInInterceptor(String phase, TraceeFilterConfiguration.Channel channel, TraceeBackend backend,
									   String profile) {
		super(phase);
		this.channel = channel;
		this.backend = backend;
		this.profile = profile;
		this.httpJsonSerializer = new HttpJsonHeaderTransport(backend.getLoggerFactory());
		this.httpSoapSerializer = new SoapHeaderTransport();
	}

	protected abstract boolean shouldHandleMessage(Message message);

	@Override
	public void handleMessage(Message message) throws Fault {
		if (shouldHandleMessage(message)) {
			final TraceeFilterConfiguration filterConfiguration = backend.getConfiguration(profile);

			LOGGER.info("Interceptor {} handles message in direction {}", this.getClass().getSimpleName(),
					MessageUtils.isOutbound(message) ? "OUT" : "IN");
			if (filterConfiguration.shouldProcessContext(channel)) {
				if (message instanceof SoapMessage) {
					try {
						handleSoapMessage((SoapMessage) message, filterConfiguration);
					} catch (JAXBException e) {
						throw new Fault(e);
					}
				} else {
					handleHttpMessage(message, filterConfiguration);
				}
			}
		}
	}

	private void handleHttpMessage(Message message, TraceeFilterConfiguration filterConfiguration) {
		final Map<String, List<String>> requestHeaders = CastUtils.cast((Map<?, ?>) message.get(Message.PROTOCOL_HEADERS));
		if (requestHeaders != null && !requestHeaders.isEmpty()) {
            final List<String> traceeHeader = requestHeaders.get(TraceeConstants.HTTP_HEADER_NAME);

            if (traceeHeader != null && !traceeHeader.isEmpty()) {
                final Map<String, String> parsedContext = httpJsonSerializer.parse(traceeHeader.get(0));
                backend.putAll(filterConfiguration.filterDeniedParams(parsedContext, channel));
            }
        }
	}

	private void handleSoapMessage(SoapMessage message, TraceeFilterConfiguration filterConfiguration) throws JAXBException {
		final Header soapHeader = message.getHeader(TraceeConstants.SOAP_HEADER_QNAME);
		if (soapHeader != null) {
			final Map<String, String> parsedContext = httpSoapSerializer.parseTpicHeader((Element) soapHeader.getObject());
            backend.putAll(filterConfiguration.filterDeniedParams(parsedContext, channel));
        }
	}
}
