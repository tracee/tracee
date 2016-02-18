package io.tracee.binding.cxf.interceptor;

import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import io.tracee.configuration.TraceeFilterConfiguration;
import io.tracee.transport.HttpHeaderTransport;
import io.tracee.transport.jaxb.TpicMap;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.headers.Header;
import org.apache.cxf.helpers.CastUtils;
import org.apache.cxf.jaxb.JAXBDataBinding;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

abstract class AbstractTraceeOutInterceptor extends AbstractPhaseInterceptor<Message> {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractTraceeOutInterceptor.class);

	protected final TraceeBackend backend;

	private final HttpHeaderTransport httpSerializer;
	private final TraceeFilterConfiguration.Channel channel;

	private String profile;

	public AbstractTraceeOutInterceptor(String phase, TraceeFilterConfiguration.Channel channel, TraceeBackend backend, String profile) {
		super(phase);
		this.channel = channel;
		this.backend = backend;
		this.profile = profile;
		this.httpSerializer = new HttpHeaderTransport();
	}

	@Override
	public void handleMessage(final Message message) {
		if (shouldHandleMessage(message)) {
			final TraceeFilterConfiguration filterConfiguration = backend.getConfiguration(profile);
			if (!backend.isEmpty() && filterConfiguration.shouldProcessContext(channel)) {
                final Map<String, String> filteredParams = filterConfiguration.filterDeniedParams(backend.copyToMap(), channel);

				LOGGER.debug("Interceptor handles message!");
				if (Boolean.TRUE.equals(message.getExchange().get(Message.REST_MESSAGE))) {
					Map<String, List<String>> responseHeaders = CastUtils.cast((Map<?, ?>) message.get(Message.PROTOCOL_HEADERS));
					if (responseHeaders == null) {
						responseHeaders = new HashMap<>();
						message.put(Message.PROTOCOL_HEADERS, responseHeaders);
					}

					final String contextAsHeader = httpSerializer.render(filteredParams);
					responseHeaders.put(TraceeConstants.TPIC_HEADER, Collections.singletonList(contextAsHeader));
				} else {
					try {
						final SoapMessage soapMessage = (SoapMessage) message;
						addSoapHeader(filteredParams, soapMessage);
					} catch (NoClassDefFoundError e) {
						LOGGER.error("Should handle SOAP-message but it seems that cxf soap dependency is not on the classpath. Unable to add Tracee-Headers: {}", e.getMessage(), e);
					}
				}
            }
		}
	}

	private void addSoapHeader(Map<String, String> filteredParams, SoapMessage soapMessage) {
		try {
			final Header tpicHeader = new Header(TraceeConstants.SOAP_HEADER_QNAME, TpicMap.wrap(filteredParams),
					new JAXBDataBinding(TpicMap.class));
			soapMessage.getHeaders().add(tpicHeader);
		} catch (JAXBException e) {
			LOGGER.warn("Error occured during TracEE soap header creation: {}", e.getMessage());
			LOGGER.debug("Detailed exception", e);
		}
	}

	protected abstract boolean shouldHandleMessage(Message message);
}
