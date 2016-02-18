package io.tracee.binding.cxf.interceptor;

import io.tracee.TraceeBackend;
import io.tracee.configuration.TraceeFilterConfiguration.Profile;
import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageUtils;
import org.apache.cxf.phase.Phase;

import static io.tracee.configuration.TraceeFilterConfiguration.Channel.OutgoingRequest;

public class TraceeRequestOutInterceptor extends AbstractTraceeOutInterceptor {

	public TraceeRequestOutInterceptor(TraceeBackend backend) {
		this(backend, Profile.DEFAULT);
	}

	public TraceeRequestOutInterceptor(TraceeBackend backend, String profile) {
		super(Phase.USER_LOGICAL, OutgoingRequest, backend, profile);
	}

	@Override
	protected boolean shouldHandleMessage(Message message) {
		return MessageUtils.isRequestor(message);
	}
}
