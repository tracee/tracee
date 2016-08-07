package io.tracee.binding.cxf.interceptor;

import io.tracee.TraceeBackend;
import io.tracee.configuration.PropertiesBasedTraceeFilterConfiguration;
import io.tracee.configuration.TraceeFilterConfiguration;
import io.tracee.configuration.TraceeFilterConfiguration.Profile;
import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageUtils;
import org.apache.cxf.phase.Phase;

import static io.tracee.configuration.TraceeFilterConfiguration.Channel.OutgoingRequest;

public class TraceeRequestOutInterceptor extends AbstractTraceeOutInterceptor {

	/**
	 * @deprecated use full constructor
	 */
	@Deprecated
	public TraceeRequestOutInterceptor(TraceeBackend backend) {
		this(backend, PropertiesBasedTraceeFilterConfiguration.instance().DEFAULT);
	}

	public TraceeRequestOutInterceptor(TraceeBackend backend, TraceeFilterConfiguration filterConfiguration) {
		super(Phase.USER_LOGICAL, OutgoingRequest, backend, filterConfiguration);
	}

	@Override
	protected boolean shouldHandleMessage(Message message) {
		return MessageUtils.isRequestor(message);
	}
}
