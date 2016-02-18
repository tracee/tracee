package io.tracee.binding.cxf.interceptor;

import io.tracee.TraceeBackend;
import io.tracee.Utilities;
import io.tracee.configuration.TraceeFilterConfiguration.Profile;
import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageUtils;
import org.apache.cxf.phase.Phase;

import static io.tracee.configuration.TraceeFilterConfiguration.Channel.IncomingRequest;

public class TraceeRequestInInterceptor extends AbstractTraceeInInterceptor {

	public TraceeRequestInInterceptor(TraceeBackend backend) {
		this(backend, Profile.DEFAULT);
	}

	public TraceeRequestInInterceptor(TraceeBackend backend, String profile) {
		super(Phase.PRE_INVOKE, IncomingRequest, backend, profile);
	}

	@Override
	public void handleMessage(Message message) {
		super.handleMessage(message);
		if (shouldHandleMessage(message)) {
			Utilities.generateInvocationIdIfNecessary(backend);
		}
	}

	@Override
	protected boolean shouldHandleMessage(Message message) {
		return !MessageUtils.isRequestor(message);
	}
}
