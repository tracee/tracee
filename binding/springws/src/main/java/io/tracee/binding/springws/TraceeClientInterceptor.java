package io.tracee.binding.springws;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.configuration.PropertiesBasedTraceeFilterConfiguration;
import io.tracee.configuration.TraceeFilterConfiguration;
import io.tracee.configuration.TraceeFilterConfiguration.Profile;
import org.springframework.ws.client.WebServiceClientException;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.context.MessageContext;

import static io.tracee.configuration.TraceeFilterConfiguration.Channel.IncomingResponse;
import static io.tracee.configuration.TraceeFilterConfiguration.Channel.OutgoingRequest;

public final class TraceeClientInterceptor extends AbstractTraceeInterceptor implements ClientInterceptor{

	/**
	 * @deprecated Prefer using a managed TraceeBackend and use another constructor.
	 */
	@Deprecated
	public TraceeClientInterceptor() {
		this(Tracee.getBackend(), PropertiesBasedTraceeFilterConfiguration.instance().DEFAULT);
	}

	/**
	 * @deprecated Prefer using a managed TraceeBackend and use another constructor.
	 */
	@Deprecated
	public TraceeClientInterceptor(final String profile) {
		this(Tracee.getBackend(), PropertiesBasedTraceeFilterConfiguration.instance().forProfile(profile));
	}

	public TraceeClientInterceptor(final TraceeBackend backend, final TraceeFilterConfiguration filterConfiguration) {
		super(backend, filterConfiguration);
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

	@Override
	public void afterCompletion(MessageContext messageContext, Exception ex) throws WebServiceClientException {

	}
}
