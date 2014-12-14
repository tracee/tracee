package io.tracee.cxf.client;

import io.tracee.SimpleTraceeBackend;
import org.apache.cxf.Bus;
import org.apache.cxf.interceptor.InterceptorProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TraceeCxfFeatureTest {

	@Mock
	private InterceptorProvider interceptorProvider;

	@Mock
	private Bus bus;

	private final SimpleTraceeBackend backend = SimpleTraceeBackend.createNonLoggingAllPermittingBackend();

	@Before
	public void onSetup() throws Exception {
		when(interceptorProvider.getInInterceptors()).thenReturn(mock(List.class));
		when(interceptorProvider.getInFaultInterceptors()).thenReturn(mock(List.class));
		when(interceptorProvider.getOutInterceptors()).thenReturn(mock(List.class));
		when(interceptorProvider.getOutFaultInterceptors()).thenReturn(mock(List.class));
	}

	@Test
	public void shouldAddInInterceptorToDefaultMessage() {
		new TraceeCxfFeature(backend).initializeProvider(interceptorProvider, bus);
		verify(interceptorProvider, times(2)).getInInterceptors();
	}

	@Test
	public void shouldAddInFaultInterceptorToDefaultMessage() {
		new TraceeCxfFeature(backend).initializeProvider(interceptorProvider, bus);
		verify(interceptorProvider, times(1)).getInFaultInterceptors();
	}

	@Test
	public void shouldAddOutInterceptorToDefaultMessage() {
		new TraceeCxfFeature(backend).initializeProvider(interceptorProvider, bus);
		verify(interceptorProvider, times(2)).getOutInterceptors();
	}

	@Test
	public void shouldAddOutFaultInterceptorToDefaultMessage() {
		new TraceeCxfFeature(backend).initializeProvider(interceptorProvider, bus);
		verify(interceptorProvider, times(1)).getOutFaultInterceptors();
	}
}
