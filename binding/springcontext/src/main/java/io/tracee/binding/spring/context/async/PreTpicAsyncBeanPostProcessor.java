package io.tracee.binding.spring.context.async;

import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ReflectiveMethodInvocation;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.Ordered;
import org.springframework.scheduling.annotation.AnnotationAsyncExecutionInterceptor;
import org.springframework.scheduling.annotation.AsyncAnnotationAdvisor;
import org.springframework.scheduling.annotation.AsyncAnnotationBeanPostProcessor;

import java.util.Map;
import java.util.concurrent.Executor;

public class PreTpicAsyncBeanPostProcessor extends AsyncAnnotationBeanPostProcessor {

	public PreTpicAsyncBeanPostProcessor(Executor executor, TraceeBackend backend) {
		advisor = new TpicPreAdvisor(executor, backend);
		setBeforeExistingAdvisors(true);
	}

	boolean isBeforeExistingAdvisors() {
		return beforeExistingAdvisors;
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) {
	}

	static class TpicPreAdvisor extends AsyncAnnotationAdvisor {

		private final Executor executor;
		private final TraceeBackend backend;

		TpicPreAdvisor(Executor executor, TraceeBackend backend) {
			super();
			this.executor = executor;
			this.backend = backend;
			setTaskExecutor(executor); // compatible with spring 4
		}

		// use getAdvice instead of buildAdvice to be compatible with Spring 4
		@Override
		public Advice getAdvice() {
			return new DelegateTpicToAsyncInterceptor(executor, backend);
		}
	}

	static class DelegateTpicToAsyncInterceptor extends AnnotationAsyncExecutionInterceptor {

		DelegateTpicToAsyncInterceptor(Executor defaultExecutor, TraceeBackend backend) {
			super(defaultExecutor);
			this.backend = backend;
		}

		private final TraceeBackend backend;

		@Override
		public Object invoke(MethodInvocation invocation) throws Throwable {
			if (invocation instanceof ReflectiveMethodInvocation) {
				final Map<String, String> tpic = backend.copyToMap();
				((ReflectiveMethodInvocation) invocation).setUserAttribute(TraceeConstants.TPIC_HEADER, tpic);
			}

			return invocation.proceed();
		}

		@Override
		public int getOrder() {
			return Ordered.HIGHEST_PRECEDENCE;
		}
	}
}
