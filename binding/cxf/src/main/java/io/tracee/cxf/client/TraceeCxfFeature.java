package io.tracee.cxf.client;

import io.tracee.Tracee;
import io.tracee.cxf.interceptor.TraceeInInterceptor;
import io.tracee.cxf.interceptor.TraceeOutInterceptor;
import org.apache.cxf.Bus;
import org.apache.cxf.feature.AbstractFeature;
import org.apache.cxf.interceptor.InterceptorProvider;

@SuppressWarnings("UnusedDeclaration")
public class TraceeCxfFeature extends AbstractFeature {

	private String profile;

	public TraceeCxfFeature() {
	}

	public TraceeCxfFeature(String profile) {
		this.profile = profile;
	}

	@Override
	protected void initializeProvider(InterceptorProvider provider, Bus bus) {
		final TraceeInInterceptor inInterceptor = new TraceeInInterceptor(Tracee.getBackend(), profile);
		final TraceeOutInterceptor outInterceptor = new TraceeOutInterceptor(Tracee.getBackend(), profile);

		provider.getInInterceptors().add(inInterceptor);
		provider.getInFaultInterceptors().add(inInterceptor);
		provider.getOutInterceptors().add(outInterceptor);
		provider.getOutFaultInterceptors().add(outInterceptor);
	}
}
