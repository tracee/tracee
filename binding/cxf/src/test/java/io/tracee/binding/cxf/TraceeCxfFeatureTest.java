package io.tracee.binding.cxf;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.configuration.TraceeFilterConfiguration.Profile;
import io.tracee.testhelper.FieldAccessUtil;
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
		new TraceeCxfFeature(backend, Profile.DEFAULT).initializeProvider(interceptorProvider, bus);
		verify(interceptorProvider, times(2)).getInInterceptors();
	}

	@Test
	public void shouldAddInFaultInterceptorToDefaultMessage() {
		new TraceeCxfFeature(backend, Profile.DEFAULT).initializeProvider(interceptorProvider, bus);
		verify(interceptorProvider, times(1)).getInFaultInterceptors();
	}

	@Test
	public void shouldAddOutInterceptorToDefaultMessage() {
		new TraceeCxfFeature(backend, Profile.DEFAULT).initializeProvider(interceptorProvider, bus);
		verify(interceptorProvider, times(2)).getOutInterceptors();
	}

	@Test
	public void shouldAddOutFaultInterceptorToDefaultMessage() {
		new TraceeCxfFeature(backend, Profile.DEFAULT).initializeProvider(interceptorProvider, bus);
		verify(interceptorProvider, times(1)).getOutFaultInterceptors();
	}

	@Test
	public void defaultConstructorUsesDefaultProfile() {
		final TraceeCxfFeature feature = new TraceeCxfFeature();
		assertThat((String) FieldAccessUtil.getFieldVal(feature, "profile"), is(Profile.DEFAULT));
	}

	@Test
	public void defaultConstructorUsesTraceeBackend() {
		final TraceeCxfFeature feature = new TraceeCxfFeature();
		assertThat((TraceeBackend) FieldAccessUtil.getFieldVal(feature, "backend"), is(Tracee.getBackend()));
	}

	@Test
	public void constructorStoresProfileNameInternal() {
		final TraceeCxfFeature feature = new TraceeCxfFeature("testProf");
		assertThat((String) FieldAccessUtil.getFieldVal(feature, "profile"), is("testProf"));
	}
}
