package io.tracee.binding.springws;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import org.springframework.ws.client.WebServiceClientException;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.context.MessageContext;

import javax.xml.bind.JAXBException;

import static io.tracee.configuration.TraceeFilterConfiguration.Channel.IncomingResponse;
import static io.tracee.configuration.TraceeFilterConfiguration.Channel.OutgoingRequest;

public final class TraceeClientInterceptor extends AbstractTraceeInterceptor implements ClientInterceptor{

	public TraceeClientInterceptor() {
		this(Tracee.getBackend(), null);
	}

	public TraceeClientInterceptor(final String profile) {
		this(Tracee.getBackend(), profile);
	}

	TraceeClientInterceptor(final TraceeBackend backend, final String profile) {
		super(backend, profile);
	}

	@Override
	public boolean handleRequest(MessageContext messageContext) {
		serializeContextToSoapHeader(messageContext.getRequest(), OutgoingRequest);
		return true;
	}

	@Override
	public boolean handleResponse(MessageContext messageContext) {
		parseContextFromSoapHeader(messageContext.getResponse(), IncomingResponse);
		return true;
	}

	@Override
	public boolean handleFault(MessageContext messageContext) {
		return handleResponse(messageContext);
	}
}
