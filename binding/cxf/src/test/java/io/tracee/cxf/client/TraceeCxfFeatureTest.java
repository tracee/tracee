package io.tracee.cxf.client;

import io.tracee.SimpleTraceeBackend;
import io.tracee.Tracee;
import org.apache.cxf.Bus;
import org.apache.cxf.interceptor.InterceptorProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Tracee.class)
public class TraceeCxfFeatureTest {

	private InterceptorProvider interceptorProvider = mock(InterceptorProvider.class);

	private Bus bus = mock(Bus.class);

	@Before
	public void onSetup() throws Exception {
		PowerMockito.mockStatic(Tracee.class);
		PowerMockito.when(Tracee.getBackend()).thenReturn(SimpleTraceeBackend.createNonLoggingAllPermittingBackend());

		when(interceptorProvider.getInInterceptors()).thenReturn(mock(List.class));
		when(interceptorProvider.getInFaultInterceptors()).thenReturn(mock(List.class));
		when(interceptorProvider.getOutInterceptors()).thenReturn(mock(List.class));
		when(interceptorProvider.getOutFaultInterceptors()).thenReturn(mock(List.class));
	}

	@Test
	public void shouldAddInInterceptorToDefaultMessage() {
		new TraceeCxfFeature().initializeProvider(interceptorProvider, bus);
		verify(interceptorProvider.getInInterceptors());
	}

	@Test
	public void shouldAddInFaultInterceptorToDefaultMessage() {
		new TraceeCxfFeature().initializeProvider(interceptorProvider, bus);
		verify(interceptorProvider.getInFaultInterceptors());
	}

	@Test
	public void shouldAddOutInterceptorToDefaultMessage() {
		new TraceeCxfFeature().initializeProvider(interceptorProvider, bus);
		verify(interceptorProvider.getOutInterceptors());
	}

	@Test
	public void shouldAddOutFaultInterceptorToDefaultMessage() {
		new TraceeCxfFeature().initializeProvider(interceptorProvider, bus);
		verify(interceptorProvider.getOutFaultInterceptors());
	}

}
