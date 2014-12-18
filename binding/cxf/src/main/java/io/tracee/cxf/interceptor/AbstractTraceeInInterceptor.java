package io.tracee.cxf.interceptor;

import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import io.tracee.TraceeLogger;
import io.tracee.configuration.TraceeFilterConfiguration;
import io.tracee.transport.HttpHeaderTransport;
import io.tracee.transport.SoapHeaderTransport;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.headers.Header;
import org.apache.cxf.helpers.CastUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.w3c.dom.Element;

import javax.xml.bind.JAXBException;
import java.util.List;
import java.util.Map;

abstract class AbstractTraceeInInterceptor extends AbstractPhaseInterceptor<Message> {
	private final TraceeBackend backend;
	private final TraceeLogger LOGGER;

	private final HttpHeaderTransport httpJsonSerializer;
	private final SoapHeaderTransport httpSoapSerializer;

	private String profile;

	private final TraceeFilterConfiguration.Channel channel;

	public AbstractTraceeInInterceptor(String phase, TraceeFilterConfiguration.Channel channel, TraceeBackend backend,
									   String profile) {
		super(phase);
		this.channel = channel;
		this.backend = backend;
		this.LOGGER = backend.getLoggerFactory().getLogger(this.getClass());
		this.profile = profile;
		this.httpJsonSerializer = new HttpHeaderTransport(backend.getLoggerFactory());
		this.httpSoapSerializer = new SoapHeaderTransport();
	}

	protected abstract boolean shouldHandleMessage(Message message);

	@Override
	public void handleMessage(Message message) throws Fault {
		if (shouldHandleMessage(message)) {
			final TraceeFilterConfiguration filterConfiguration = backend.getConfiguration(profile);

			LOGGER.debug("Interceptor handles message!");
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
                final Map<String, String> parsedContext = httpJsonSerializer.parse(traceeHeader);
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
