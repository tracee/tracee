package io.tracee.binding.cxf;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.binding.cxf.interceptor.TraceeRequestInInterceptor;
import io.tracee.binding.cxf.interceptor.TraceeRequestOutInterceptor;
import io.tracee.binding.cxf.interceptor.TraceeResponseInInterceptor;
import io.tracee.binding.cxf.interceptor.TraceeResponseOutInterceptor;
import io.tracee.configuration.PropertiesBasedTraceeFilterConfiguration;
import io.tracee.configuration.TraceeFilterConfiguration;
import io.tracee.configuration.TraceeFilterConfiguration.Profile;
import org.apache.cxf.Bus;
import org.apache.cxf.feature.AbstractFeature;
import org.apache.cxf.interceptor.InterceptorProvider;

public class TraceeCxfFeature extends AbstractFeature {

	private TraceeFilterConfiguration filterConfiguration;
	private TraceeBackend backend;

	/**
	 * @deprecated use full ctor
	 */
	@Deprecated
	public TraceeCxfFeature() {
		this(Tracee.getBackend(), PropertiesBasedTraceeFilterConfiguration.instance().DEFAULT);
	}

	TraceeCxfFeature(TraceeBackend backend, TraceeFilterConfiguration filterConfiguration) {
		this.backend = backend;
		this.filterConfiguration = filterConfiguration;
	}


	@Override
	protected void initializeProvider(InterceptorProvider provider, Bus bus) {
		final TraceeRequestInInterceptor requestInInterceptor = new TraceeRequestInInterceptor(backend, filterConfiguration);
		final TraceeResponseInInterceptor responseInInterceptor = new TraceeResponseInInterceptor(backend, filterConfiguration);
		final TraceeRequestOutInterceptor requestOutInterceptor = new TraceeRequestOutInterceptor(backend, filterConfiguration);
		final TraceeResponseOutInterceptor responseOutInterceptor = new TraceeResponseOutInterceptor(backend, filterConfiguration);

		provider.getInInterceptors().add(requestInInterceptor);
		provider.getInInterceptors().add(responseInInterceptor);

		provider.getOutInterceptors().add(requestOutInterceptor);
		provider.getOutInterceptors().add(responseOutInterceptor);

		provider.getOutFaultInterceptors().add(responseOutInterceptor);
		provider.getInFaultInterceptors().add(requestInInterceptor);
	}
}
