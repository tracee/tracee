package io.tracee.binding.spring.context.async;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import io.tracee.spi.TraceeBackendProvider;
import io.tracee.testhelper.SimpleTraceeBackend;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.aop.framework.ReflectiveMethodInvocation;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyMapOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Tracee.class, TraceeBackendProvider.class})
public class PostTpicAsyncBeanPostProcessorTest {

	private final TraceeBackend backend = spy(SimpleTraceeBackend.createNonLoggingAllPermittingBackend());

	@Before
	public void before() {
		PowerMockito.mockStatic(Tracee.class);
		when(Tracee.getBackend()).thenReturn(backend);
	}

	@After
	public void after() {
		Tracee.getBackend().clear();
		PowerMockito.doCallRealMethod().when(Tracee.class);
	}

	@Test
	public void ensureThatThePriorityIsNotHighestToRunInAsyncMode() {
		final PostTpicAsyncBeanPostProcessor postProcessor = new PostTpicAsyncBeanPostProcessor(mock(Executor.class), backend);

		assertThat(postProcessor.getOrder(), is(0));
	}

	@Test
	public void advisorShouldReturnInterceptor() {
		final PostTpicAsyncBeanPostProcessor.TpicPostAdvisor advisor = new PostTpicAsyncBeanPostProcessor.TpicPostAdvisor(mock(Executor.class), backend);
		assertThat(advisor.getAdvice(), instanceOf(PostTpicAsyncBeanPostProcessor.DelegateTpicToThreadInterceptor.class));
	}

	@Test
	public void shouldRestoreTpicFromMetadataWhenSet() throws Throwable {
		final PostTpicAsyncBeanPostProcessor.DelegateTpicToThreadInterceptor interceptor = new PostTpicAsyncBeanPostProcessor.DelegateTpicToThreadInterceptor(mock(Executor.class), backend);
		final ReflectiveMethodInvocation mockedInvocation = mock(ReflectiveMethodInvocation.class);
		final Map<String, String> tpic = new HashMap<>();
		tpic.put("myInvoc", "storeThisToAsync");
		when(mockedInvocation.getUserAttribute(TraceeConstants.TPIC_HEADER)).thenReturn(tpic);

		interceptor.invoke(mockedInvocation);
		verify(mockedInvocation).proceed();
		verify(backend).putAll(eq(tpic));
	}

	@Test
	public void shouldSkipTpicRestoreFromMetadataWhenMetadataIsNull() throws Throwable {
		final PostTpicAsyncBeanPostProcessor.DelegateTpicToThreadInterceptor interceptor = new PostTpicAsyncBeanPostProcessor.DelegateTpicToThreadInterceptor(mock(Executor.class), backend);
		final ReflectiveMethodInvocation mockedInvocation = mock(ReflectiveMethodInvocation.class);
		when(mockedInvocation.getUserAttribute(TraceeConstants.TPIC_HEADER)).thenReturn(null);

		interceptor.invoke(mockedInvocation);
		verify(mockedInvocation).proceed();
		verify(backend, never()).putAll(anyMapOf(String.class, String.class));
	}

	@Test
	public void shouldClearTpicAfterInvocation() throws Throwable {
		final PostTpicAsyncBeanPostProcessor.DelegateTpicToThreadInterceptor interceptor = new PostTpicAsyncBeanPostProcessor.DelegateTpicToThreadInterceptor(mock(Executor.class), backend);
		final ReflectiveMethodInvocation mockedInvocation = mock(ReflectiveMethodInvocation.class);
		when(mockedInvocation.getUserAttribute(TraceeConstants.TPIC_HEADER)).thenReturn(null);

		interceptor.invoke(mockedInvocation);
		verify(backend).clear();
	}
}
