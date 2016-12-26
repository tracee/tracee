package io.tracee.binding.spring.context.async;

import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import io.tracee.binding.spring.context.async.PreTpicAsyncBeanPostProcessor.DelegateTpicToAsyncInterceptor;
import io.tracee.binding.spring.context.async.PreTpicAsyncBeanPostProcessor.TpicPreAdvisor;
import io.tracee.testhelper.SimpleTraceeBackend;
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

	private final TraceeBackend backend = SimpleTraceeBackend.createNonLoggingAllPermittingBackend();

	@Test
	public void postProcessorShouldComeWithLowestPriorityToEnsureThatTheInterceptorIsReallyTheFirstInTheChain() {
		final PreTpicAsyncBeanPostProcessor beanPostProcessor = new PreTpicAsyncBeanPostProcessor(mock(Executor.class), backend);

		assertThat(beanPostProcessor.getOrder(), is(Ordered.LOWEST_PRECEDENCE));
	}

	@Test
	public void ensureThisBeanPostProcessorIsTheFirstInTheRow() {
		final PreTpicAsyncBeanPostProcessor beanPostProcessor = new PreTpicAsyncBeanPostProcessor(mock(Executor.class), backend);

		assertThat(beanPostProcessor.isBeforeExistingAdvisors(), is(true));
	}

	@Test
	public void ensureAdvisorIsRunningWithMaximumPriority() {
		final TpicPreAdvisor advisor = new TpicPreAdvisor(mock(Executor.class), backend);
		assertThat(advisor.getOrder(), is(Ordered.HIGHEST_PRECEDENCE));
	}

	@Test
	public void advisorShouldReturnInterceptor() {
		final TpicPreAdvisor advisor = new TpicPreAdvisor(mock(Executor.class), backend);
		assertThat(advisor.getAdvice(), instanceOf(DelegateTpicToAsyncInterceptor.class));
	}

	@Test
	public void shouldStoreTpicToInvocationMetadata() throws Throwable {
		final DelegateTpicToAsyncInterceptor interceptor = new DelegateTpicToAsyncInterceptor(mock(Executor.class), backend);
		final ReflectiveMethodInvocation mockedInvocation = mock(ReflectiveMethodInvocation.class);
		final Map<String, String> tpic = new HashMap<>();
		tpic.put("myInvoc", "storeThisToAsync");
		backend.putAll(tpic);
		interceptor.invoke(mockedInvocation);
		verify(mockedInvocation).setUserAttribute(eq(TraceeConstants.TPIC_HEADER), eq(tpic));
	}
}
