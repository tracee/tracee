package io.tracee.cxf.interceptor;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import io.tracee.configuration.TraceeFilterConfiguration;
import io.tracee.transport.HttpJsonHeaderTransport;
import io.tracee.transport.SoapHeaderTransport;
import io.tracee.transport.TransportSerialization;
import io.tracee.transport.jaxb.TpicMap;
import org.apache.cxf.binding.soap.SoapHeader;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.headers.Header;
import org.apache.cxf.helpers.CastUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.jaxb.JAXBDataBinding;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import javax.xml.bind.JAXBException;
import javax.xml.soap.SOAPException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.tracee.configuration.TraceeFilterConfiguration.Channel.OutgoingRequest;

public class TraceeOutInterceptor extends AbstractPhaseInterceptor<Message> {

	private static final Logger LOGGER = LoggerFactory.getLogger(TraceeInInterceptor.class);

	private final TraceeBackend backend;

	private final TransportSerialization<String> httpSerializer;

	private String profile;

	public TraceeOutInterceptor() {
		this(Tracee.getBackend());
	}

	public TraceeOutInterceptor(String profile) {
		this(Tracee.getBackend(), profile);
	}

	public TraceeOutInterceptor(TraceeBackend backend) {
		this(backend, null);
	}

	public TraceeOutInterceptor(TraceeBackend backend, String profile) {
		super(Phase.USER_LOGICAL);
		this.backend = backend;
		this.profile = profile;
		this.httpSerializer = new HttpJsonHeaderTransport(backend.getLoggerFactory());
	}

	@Override
	public void handleMessage(Message message) throws Fault {
		final TraceeFilterConfiguration filterConfiguration = backend.getConfiguration(profile);
		if (!backend.isEmpty() && filterConfiguration.shouldProcessContext(OutgoingRequest)) {
			final Map<String, String> filteredParams = filterConfiguration.filterDeniedParams(backend, OutgoingRequest);

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
}
