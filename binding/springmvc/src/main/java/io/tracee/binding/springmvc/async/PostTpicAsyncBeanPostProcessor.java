package io.tracee.binding.springmvc.async;

import io.tracee.Tracee;
import io.tracee.TraceeConstants;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.AbstractAdvisingBeanPostProcessor;
import org.springframework.aop.framework.ReflectiveMethodInvocation;
import org.springframework.scheduling.annotation.AnnotationAsyncExecutionInterceptor;
import org.springframework.scheduling.annotation.AsyncAnnotationAdvisor;

import java.util.Map;
import java.util.concurrent.Executor;

public class PostTpicAsyncBeanPostProcessor extends AbstractAdvisingBeanPostProcessor {

	public PostTpicAsyncBeanPostProcessor(Executor executor) {
		advisor = new TpicPostAdvisor(executor);
	}

	@Override
	public int getOrder() {
		return 0;
	}

	static class TpicPostAdvisor extends AsyncAnnotationAdvisor {

		private final Executor executor;

		public TpicPostAdvisor(Executor executor) {
			super();
			this.executor = executor;
			setTaskExecutor(executor); // compatible with spring 4
		}

		// use getAdvice instead of buildAdvice to be compatible with Spring 4
		@Override
		public Advice getAdvice() {
			return new DelegateTpicToThreadInterceptor(executor);
		}
	}

	static class DelegateTpicToThreadInterceptor extends AnnotationAsyncExecutionInterceptor {

		DelegateTpicToThreadInterceptor(Executor defaultExecutor) {
			super(defaultExecutor);
		}

		@Override
		public Object invoke(MethodInvocation invocation) throws Throwable {
			if (invocation instanceof ReflectiveMethodInvocation) {
				final ReflectiveMethodInvocation methodInvocation = (ReflectiveMethodInvocation) invocation;
				final Object tpicObj = methodInvocation.getUserAttribute(TraceeConstants.TPIC_HEADER);
				if (tpicObj instanceof Map) {
					@SuppressWarnings("unchecked")
					final Map<? extends String, ? extends String> tpic = (Map<? extends String, ? extends String>) tpicObj;
					Tracee.getBackend().putAll(tpic);
				}
			}

			try {
				return invocation.proceed();
			} finally {
				Tracee.getBackend().clear();
			}
		}
	}
}
