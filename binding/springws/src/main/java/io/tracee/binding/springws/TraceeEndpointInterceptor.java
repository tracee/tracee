package io.tracee.binding.springws;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.Utilities;
import io.tracee.configuration.PropertiesBasedTraceeFilterConfiguration;
import io.tracee.configuration.TraceeFilterConfiguration;
import io.tracee.configuration.TraceeFilterConfiguration.Profile;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.EndpointInterceptor;

import static io.tracee.configuration.TraceeFilterConfiguration.Channel.IncomingRequest;
import static io.tracee.configuration.TraceeFilterConfiguration.Channel.OutgoingResponse;

public final class TraceeEndpointInterceptor extends AbstractTraceeInterceptor implements EndpointInterceptor {

	/**
	 * @deprecated Prefer using a managed TraceeBackend and use another constructor.
	 */
	@Deprecated
	public TraceeEndpointInterceptor() {
		this(Tracee.getBackend(), PropertiesBasedTraceeFilterConfiguration.instance().DEFAULT);
	}

	/**
	 * @deprecated Prefer using a managed TraceeBackend and use another constructor.
	 */
	@Deprecated
	public TraceeEndpointInterceptor(final String profile) {
		this(Tracee.getBackend(), PropertiesBasedTraceeFilterConfiguration.instance().forProfile(profile));
	}

	public TraceeEndpointInterceptor(final TraceeBackend backend, final TraceeFilterConfiguration filterConfiguration) {
		super(backend, filterConfiguration);
	}

	@Override
	public boolean handleRequest(MessageContext messageContext, Object o) {
		parseContextFromSoapHeader(messageContext.getRequest(), IncomingRequest);

		Utilities.generateInvocationIdIfNecessary(backend, filterConfiguration);
		return true;
	}

	@Override
	public boolean handleResponse(MessageContext messageContext, Object o) {
		serializeContextToSoapHeader(messageContext.getResponse(), OutgoingResponse);
		return true;
	}

	@Override
	public boolean handleFault(MessageContext messageContext, Object o) {
		return handleResponse(messageContext, o);
	}

	@Override
	public void afterCompletion(MessageContext messageContext, Object o, Exception e) {
		backend.clear();
	}
}
