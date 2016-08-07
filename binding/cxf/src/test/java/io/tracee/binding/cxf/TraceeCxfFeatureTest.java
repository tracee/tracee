package io.tracee.binding.cxf;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.configuration.PropertiesBasedTraceeFilterConfiguration;
import io.tracee.configuration.TraceeFilterConfiguration;
import io.tracee.testhelper.FieldAccessUtil;
import io.tracee.testhelper.PermitAllTraceeFilterConfiguration;
import io.tracee.testhelper.SimpleTraceeBackend;
import org.apache.cxf.Bus;
import org.apache.cxf.interceptor.InterceptorProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TraceeCxfFeatureTest {

	@Mock
	private InterceptorProvider interceptorProvider;

	@Mock
	private Bus bus;

	private final SimpleTraceeBackend backend = new SimpleTraceeBackend();
	private final PermitAllTraceeFilterConfiguration filterConfiguration = PermitAllTraceeFilterConfiguration.INSTANCE;

	@SuppressWarnings("unchecked")
	@Before
	public void onSetup() throws Exception {
		when(interceptorProvider.getInInterceptors()).thenReturn(mock(List.class));
		when(interceptorProvider.getInFaultInterceptors()).thenReturn(mock(List.class));
		when(interceptorProvider.getOutInterceptors()).thenReturn(mock(List.class));
		when(interceptorProvider.getOutFaultInterceptors()).thenReturn(mock(List.class));
	}

	@Test
	public void shouldAddInInterceptorToDefaultMessage() {
		new TraceeCxfFeature(backend, filterConfiguration).initializeProvider(interceptorProvider, bus);
		verify(interceptorProvider, times(2)).getInInterceptors();
	}

	@Test
	public void shouldAddInFaultInterceptorToDefaultMessage() {
		new TraceeCxfFeature(backend, filterConfiguration).initializeProvider(interceptorProvider, bus);
		verify(interceptorProvider, times(1)).getInFaultInterceptors();
	}

	@Test
	public void shouldAddOutInterceptorToDefaultMessage() {
		new TraceeCxfFeature(backend, filterConfiguration).initializeProvider(interceptorProvider, bus);
		verify(interceptorProvider, times(2)).getOutInterceptors();
	}

	@Test
	public void shouldAddOutFaultInterceptorToDefaultMessage() {
		new TraceeCxfFeature(backend, filterConfiguration).initializeProvider(interceptorProvider, bus);
		verify(interceptorProvider, times(1)).getOutFaultInterceptors();
	}

	@Test
	public void defaultConstructorUsesDefaultFilterConfiguration() {
		final TraceeCxfFeature feature = new TraceeCxfFeature();
		assertThat((TraceeFilterConfiguration) FieldAccessUtil.getFieldVal(feature, "filterConfiguration"), sameInstance(PropertiesBasedTraceeFilterConfiguration.instance().DEFAULT));
	}

	@Test
	public void defaultConstructorUsesTraceeBackend() {
		final TraceeCxfFeature feature = new TraceeCxfFeature();
		assertThat((TraceeBackend) FieldAccessUtil.getFieldVal(feature, "backend"), is(Tracee.getBackend()));
	}

}
