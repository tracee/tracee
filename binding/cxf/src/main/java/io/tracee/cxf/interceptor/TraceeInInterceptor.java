package io.tracee.cxf.interceptor;

import io.tracee.Tracee;
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
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import javax.xml.bind.JAXBException;
import java.util.List;
import java.util.Map;

import static io.tracee.configuration.TraceeFilterConfiguration.Channel.IncomingResponse;

public class TraceeInInterceptor extends AbstractPhaseInterceptor<Message> {

	private static final Logger LOGGER = LoggerFactory.getLogger(TraceeInInterceptor.class);

	private final TraceeBackend backend;

	private final HttpJsonHeaderTransport httpJsonSerializer;
	private final SoapHeaderTransport httpSoapSerializer;

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
		this.httpJsonSerializer = new HttpJsonHeaderTransport(backend.getLoggerFactory());
		this.httpSoapSerializer = new SoapHeaderTransport();
	}

	@Override
	public void handleMessage(Message message) throws Fault {
		final TraceeFilterConfiguration filterConfiguration = backend.getConfiguration(profile);

		if (filterConfiguration.shouldProcessContext(IncomingResponse)) {
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

	private void handleHttpMessage(Message message, TraceeFilterConfiguration filterConfiguration) {
		final Map<String, List<String>> requestHeaders = CastUtils.cast((Map<?, ?>) message.get(Message.PROTOCOL_HEADERS));
		if (requestHeaders != null && !requestHeaders.isEmpty()) {
            final List<String> traceeHeader = requestHeaders.get(TraceeConstants.HTTP_HEADER_NAME);

            if (traceeHeader != null && !traceeHeader.isEmpty()) {
                final Map<String, String> parsedContext = httpJsonSerializer.parse(traceeHeader.get(0));
                backend.putAll(filterConfiguration.filterDeniedParams(parsedContext, IncomingResponse));
            }
        }
	}

	private void handleSoapMessage(SoapMessage message, TraceeFilterConfiguration filterConfiguration) throws JAXBException {
		final Header soapHeader = message.getHeader(TraceeConstants.SOAP_HEADER_QNAME);
		if (soapHeader != null) {
			final Map<String, String> parsedContext = httpSoapSerializer.parseTpicHeader((Element) soapHeader.getObject());
            backend.putAll(filterConfiguration.filterDeniedParams(parsedContext, IncomingResponse));
        }
	}
}
