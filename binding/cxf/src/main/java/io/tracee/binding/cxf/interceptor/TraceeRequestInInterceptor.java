package io.tracee.binding.cxf.interceptor;

import io.tracee.TraceeBackend;
import io.tracee.Utilities;
import io.tracee.configuration.PropertiesBasedTraceeFilterConfiguration;
import io.tracee.configuration.TraceeFilterConfiguration;
import io.tracee.configuration.TraceeFilterConfiguration.Profile;
import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageUtils;
import org.apache.cxf.phase.Phase;

import static io.tracee.configuration.TraceeFilterConfiguration.Channel.IncomingRequest;

public class TraceeRequestInInterceptor extends AbstractTraceeInInterceptor {


	/**
	 * @deprecated
	 */
	@Deprecated
	public TraceeRequestInInterceptor(TraceeBackend backend) {
		this(backend, PropertiesBasedTraceeFilterConfiguration.instance().DEFAULT);
	}

	public TraceeRequestInInterceptor(TraceeBackend backend, TraceeFilterConfiguration filterConfiguration) {
		super(Phase.PRE_INVOKE, IncomingRequest, backend, filterConfiguration);
	}

	@Override
	public void handleMessage(Message message) {
		super.handleMessage(message);
		if (shouldHandleMessage(message)) {
			Utilities.generateInvocationIdIfNecessary(backend, filterConfiguration);
		}
	}

	@Override
	protected boolean shouldHandleMessage(Message message) {
		return !MessageUtils.isRequestor(message);
	}
}
