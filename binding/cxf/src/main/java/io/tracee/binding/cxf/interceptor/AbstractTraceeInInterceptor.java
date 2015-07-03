package io.tracee.binding.cxf.interceptor;

import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import io.tracee.configuration.TraceeFilterConfiguration;
import io.tracee.transport.HttpHeaderTransport;
import io.tracee.transport.SoapHeaderTransport;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.headers.Header;
import org.apache.cxf.helpers.CastUtils;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import java.util.List;
import java.util.Map;

abstract class AbstractTraceeInInterceptor extends AbstractPhaseInterceptor<Message> {

	protected final TraceeBackend backend;

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractTraceeInInterceptor.class);

	private final HttpHeaderTransport httpJsonSerializer;
	private final SoapHeaderTransport httpSoapSerializer;

	private String profile;

	private final TraceeFilterConfiguration.Channel channel;

	public AbstractTraceeInInterceptor(String phase, TraceeFilterConfiguration.Channel channel, TraceeBackend backend,
									   String profile) {
		super(phase);
		this.channel = channel;
		this.backend = backend;
		this.profile = profile;
		this.httpJsonSerializer = new HttpHeaderTransport();
		this.httpSoapSerializer = new SoapHeaderTransport();
	}

	protected abstract boolean shouldHandleMessage(Message message);

	@Override
	public void handleMessage(final Message message) {
		if (shouldHandleMessage(message)) {
			final TraceeFilterConfiguration filterConfiguration = backend.getConfiguration(profile);

			LOGGER.debug("Interceptor handles message!");
			if (filterConfiguration.shouldProcessContext(channel)) {
				if (message instanceof SoapMessage) {
					handleSoapMessage((SoapMessage) message, filterConfiguration);
				} else {
					handleHttpMessage(message, filterConfiguration);
				}
			}
		}
	}

	private void handleHttpMessage(final Message message, final TraceeFilterConfiguration filterConfiguration) {
		final Map<String, List<String>> requestHeaders = CastUtils.cast((Map<?, ?>) message.get(Message.PROTOCOL_HEADERS));
		if (requestHeaders != null && !requestHeaders.isEmpty()) {
            final List<String> traceeHeader = requestHeaders.get(TraceeConstants.TPIC_HEADER);

            if (traceeHeader != null && !traceeHeader.isEmpty()) {
                final Map<String, String> parsedContext = httpJsonSerializer.parse(traceeHeader);
                backend.putAll(filterConfiguration.filterDeniedParams(parsedContext, channel));
            }
        }
	}

	private void handleSoapMessage(final SoapMessage message, final TraceeFilterConfiguration filterConfiguration) {
		final Header soapHeader = message.getHeader(TraceeConstants.SOAP_HEADER_QNAME);
		if (soapHeader != null) {
			final Map<String, String> parsedContext = httpSoapSerializer.parseTpicHeader((Element) soapHeader.getObject());
            backend.putAll(filterConfiguration.filterDeniedParams(parsedContext, channel));
        }
	}
}
