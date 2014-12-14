package io.tracee.cxf.interceptor;

import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import io.tracee.configuration.TraceeFilterConfiguration;
import io.tracee.transport.HttpJsonHeaderTransport;
import io.tracee.transport.TransportSerialization;
import io.tracee.transport.jaxb.TpicMap;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.headers.Header;
import org.apache.cxf.helpers.CastUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.jaxb.JAXBDataBinding;
import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageUtils;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

abstract class AbstractTraceeOutInterceptor extends AbstractPhaseInterceptor<Message> {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractTraceeOutInterceptor.class);

	private final TraceeBackend backend;

	private final TransportSerialization<String> httpSerializer;
	private final TraceeFilterConfiguration.Channel channel;

	private String profile;

	public AbstractTraceeOutInterceptor(String phase, TraceeFilterConfiguration.Channel channel, TraceeBackend backend, String profile) {
		super(phase);
		this.channel = channel;
		this.backend = backend;
		this.profile = profile;
		this.httpSerializer = new HttpJsonHeaderTransport(backend.getLoggerFactory());
	}

	@Override
	public void handleMessage(Message message) throws Fault {
		if (shouldHandleMessage(message)) {
			final TraceeFilterConfiguration filterConfiguration = backend.getConfiguration(profile);
			if (!backend.isEmpty() && filterConfiguration.shouldProcessContext(channel)) {
                final Map<String, String> filteredParams = filterConfiguration.filterDeniedParams(backend, channel);

				LOGGER.info("Interceptor {} handles message in direction {}", this.getClass().getSimpleName(),
						MessageUtils.isOutbound(message) ? "OUT" : "IN");
                if (message instanceof SoapMessage) {
                    final SoapMessage soapMessage = (SoapMessage) message;

                    addSoapHeader(filteredParams, soapMessage);
                } else {
                    Map<String, List<String>> responseHeaders = CastUtils.cast((Map<?, ?>) message.get(Message.PROTOCOL_HEADERS));
                    if (responseHeaders == null) {
                        responseHeaders = new HashMap<String, List<String>>();
                        message.put(Message.PROTOCOL_HEADERS, responseHeaders);
                    }

                    final String contextAsHeader = httpSerializer.render(filteredParams);
                    responseHeaders.put(TraceeConstants.HTTP_HEADER_NAME, Arrays.asList(contextAsHeader));
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
