package io.tracee.binding.cxf.client;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.binding.cxf.interceptor.*;
import org.apache.cxf.Bus;
import org.apache.cxf.feature.AbstractFeature;
import org.apache.cxf.interceptor.InterceptorProvider;

public class TraceeCxfFeature extends AbstractFeature {

	private String profile;
	private TraceeBackend backend;

	public TraceeCxfFeature() {
		this(Tracee.getBackend());
	}

	public TraceeCxfFeature(TraceeBackend backend) {
		this.backend = backend;
	}

	public TraceeCxfFeature(String profile) {
		this.profile = profile;
	}

	@Override
	protected void initializeProvider(InterceptorProvider provider, Bus bus) {
		final TraceeRequestInInterceptor requestInInterceptor = new TraceeRequestInInterceptor(backend, profile);
		final TraceeResponseInInterceptor responseInInterceptor = new TraceeResponseInInterceptor(backend, profile);
		final TraceeRequestOutInterceptor requestOutInterceptor = new TraceeRequestOutInterceptor(backend, profile);
		final TraceeResponseOutInterceptor responseOutInterceptor = new TraceeResponseOutInterceptor(backend, profile);

		provider.getInInterceptors().add(requestInInterceptor);
		provider.getInInterceptors().add(responseInInterceptor);

		provider.getOutInterceptors().add(requestOutInterceptor);
		provider.getOutInterceptors().add(responseOutInterceptor);

		provider.getOutFaultInterceptors().add(responseOutInterceptor);
		provider.getInFaultInterceptors().add(requestInInterceptor);
	}
}
