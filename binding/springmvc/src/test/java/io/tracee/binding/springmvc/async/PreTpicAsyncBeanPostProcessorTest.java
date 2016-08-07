package io.tracee.binding.springmvc.async;

import io.tracee.Tracee;
import io.tracee.TraceeConstants;
import io.tracee.binding.springmvc.async.PreTpicAsyncBeanPostProcessor.DelegateTpicToAsyncInterceptor;
import io.tracee.binding.springmvc.async.PreTpicAsyncBeanPostProcessor.TpicPreAdvisor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.aop.framework.ReflectiveMethodInvocation;
import org.springframework.core.Ordered;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class PreTpicAsyncBeanPostProcessorTest {

	@Before
	@After
	public void beforeAndAfter() {
		Tracee.getBackend().clear();
	}

	@Test
	public void postProcessorShouldComeWithLowestPriorityToEnsureThatTheInterceptorIsReallyTheFirstInTheChain() {
		final PreTpicAsyncBeanPostProcessor beanPostProcessor = new PreTpicAsyncBeanPostProcessor(mock(Executor.class));

		assertThat(beanPostProcessor.getOrder(), is(Ordered.LOWEST_PRECEDENCE));
	}

	@Test
	public void ensureThisBeanPostProcessorIsTheFirstInTheRow() {
		final PreTpicAsyncBeanPostProcessor beanPostProcessor = new PreTpicAsyncBeanPostProcessor(mock(Executor.class));

		assertThat(beanPostProcessor.isBeforeExistingAdvisors(), is(true));
	}

	@Test
	public void ensureAdvisorIsRunningWithMaximumPriority() {
		final TpicPreAdvisor advisor = new TpicPreAdvisor(mock(Executor.class));
		assertThat(advisor.getOrder(), is(Ordered.HIGHEST_PRECEDENCE));
	}

	@Test
	public void advisorShouldReturnInterceptor() {
		final TpicPreAdvisor advisor = new TpicPreAdvisor(mock(Executor.class));
		assertThat(advisor.getAdvice(), instanceOf(DelegateTpicToAsyncInterceptor.class));
	}

	@Test
	public void shouldStoreTpicToInvocationMetadata() throws Throwable {
		final DelegateTpicToAsyncInterceptor interceptor = new DelegateTpicToAsyncInterceptor(mock(Executor.class));
		final ReflectiveMethodInvocation mockedInvocation = mock(ReflectiveMethodInvocation.class);
		final Map<String, String> tpic = new HashMap<>();
		tpic.put("myInvoc", "storeThisToAsync");
		Tracee.getBackend().putAll(tpic);
		interceptor.invoke(mockedInvocation);
		verify(mockedInvocation).setUserAttribute(eq(TraceeConstants.TPIC_HEADER), eq(tpic));
	}
}
