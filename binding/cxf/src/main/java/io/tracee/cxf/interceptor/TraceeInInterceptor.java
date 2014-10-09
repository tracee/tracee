package io.tracee.cxf.interceptor;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import io.tracee.configuration.TraceeFilterConfiguration;
import io.tracee.transport.HttpJsonHeaderTransport;
import io.tracee.transport.TransportSerialization;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.databinding.DataReader;
import org.apache.cxf.headers.Header;
import org.apache.cxf.helpers.CastUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.jaxb.JAXBDataBinding;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

import javax.xml.bind.JAXBException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static io.tracee.configuration.TraceeFilterConfiguration.Channel.IncomingResponse;

public class TraceeInInterceptor extends AbstractPhaseInterceptor<Message> {

	private static final Logger LOGGER = LoggerFactory.getLogger(TraceeInInterceptor.class);

	private final TraceeBackend backend;

	private final TransportSerialization<String> httpSerializer;

	private String profile;

	public TraceeInInterceptor() {
		this(Tracee.getBackend());
	}

	public TraceeInInterceptor(String profile) {
		this(Tracee.getBackend(), profile);
	}

	public TraceeInInterceptor(TraceeBackend backend) {
		this(backend, null);
	}

	public TraceeInInterceptor(TraceeBackend backend, String profile) {
		super(Phase.PRE_INVOKE);
		this.backend = backend;
		this.profile = profile;
		this.httpSerializer = new HttpJsonHeaderTransport(backend.getLoggerFactory());
	}

	@Override
	public void handleMessage(Message message) throws Fault {
		final TraceeFilterConfiguration filterConfiguration = backend.getConfiguration(profile);

		if (filterConfiguration.shouldProcessContext(IncomingResponse)) {
			if (message instanceof SoapMessage) {
				handleSoapMessage((SoapMessage) message, filterConfiguration);
			} else {
				handleHttpMessage(message, filterConfiguration);
			}
		}
	}

	private void handleHttpMessage(Message message, TraceeFilterConfiguration filterConfiguration) {
		final Map<String, List<String>> requestHeaders = CastUtils.cast((Map<?, ?>) message.get(Message.PROTOCOL_HEADERS));
		if (requestHeaders != null && !requestHeaders.isEmpty()) {
            final List<String> traceeHeader = requestHeaders.get(TraceeConstants.HTTP_HEADER_NAME);

            if (traceeHeader != null && !traceeHeader.isEmpty()) {
                final Map<String, String> parsedContext = httpSerializer.parse(traceeHeader.get(0));
                backend.putAll(filterConfiguration.filterDeniedParams(parsedContext, IncomingResponse));
            }
        }
	}

	private void handleSoapMessage(SoapMessage message, TraceeFilterConfiguration filterConfiguration) {
		final Header soapHeader = message.getHeader(TraceeConstants.TRACEE_SOAP_HEADER_QNAME);
		if (soapHeader != null) {
            final Map<String, String> parsedContext = readSoapHeader(soapHeader);
            backend.putAll(filterConfiguration.filterDeniedParams(parsedContext, IncomingResponse));
        }
	}

	private Map<String, String> readSoapHeader(Header soapHeader) {
		try {
			DataReader<Node> dataReader = new JAXBDataBinding(SoapHeaderMap.class).createReader(Node.class);
			final SoapHeaderMap soapHeaderMap = (SoapHeaderMap) dataReader.read(TraceeConstants.TRACEE_SOAP_HEADER_QNAME, (Node) soapHeader.getObject(), SoapHeaderMap.class);
			return soapHeaderMap.getDataMap();
		} catch (JAXBException e) {
			LOGGER.warn("Error occured during TracEE soap header extraction: {}", e.getMessage());
			LOGGER.debug("Detailed exception", e);
		}
		return Collections.emptyMap();
	}
}
