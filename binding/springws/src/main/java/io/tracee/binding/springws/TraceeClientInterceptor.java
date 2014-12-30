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
	public boolean handleRequest(MessageContext messageContext) throws WebServiceClientException {
		serializeContextToSoapHeader(messageContext.getRequest(), OutgoingRequest);
		return true;
	}

	@Override
	public boolean handleResponse(MessageContext messageContext) throws WebServiceClientException {
		try {
			parseContextFromSoapHeader(messageContext.getResponse(), IncomingResponse);
		} catch (JAXBException e) {
			throw new WebServiceClientException(e.getMessage(), e){};
		}
		return true;
	}

	@Override
	public boolean handleFault(MessageContext messageContext) throws WebServiceClientException {
		return handleResponse(messageContext);
	}
}
