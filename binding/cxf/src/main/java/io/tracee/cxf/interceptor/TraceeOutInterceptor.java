package io.tracee.cxf.interceptor;

import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import io.tracee.configuration.TraceeFilterConfiguration;
import io.tracee.transport.HttpJsonHeaderTransport;
import io.tracee.transport.TransportSerialization;
import org.apache.cxf.binding.soap.SoapHeader;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.helpers.CastUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.jaxb.JAXBDataBinding;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

import javax.xml.bind.JAXBException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.tracee.configuration.TraceeFilterConfiguration.Channel.OutgoingRequest;

public class TraceeOutInterceptor extends AbstractPhaseInterceptor<Message> {

	private final TraceeBackend backend;

	private final TransportSerialization<String> httpSerializer;

	private String profile;

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
			final SoapHeader soapHeader = new SoapHeader(TraceeConstants.TRACEE_SOAP_HEADER_QNAME, new SoapHeaderMap(filteredParams), new JAXBDataBinding(SoapHeaderMap.class));
			soapMessage.getHeaders().add(soapHeader);
		} catch (JAXBException ignored) {
		}
	}
}
