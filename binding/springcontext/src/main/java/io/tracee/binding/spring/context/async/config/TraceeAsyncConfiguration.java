package io.tracee.binding.spring.context.async.config;


import io.tracee.TraceeBackend;
import io.tracee.binding.spring.context.async.PostTpicAsyncBeanPostProcessor;
import io.tracee.binding.spring.context.async.PreTpicAsyncBeanPostProcessor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.core.task.AsyncTaskExecutor;


@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Configuration
public class TraceeAsyncConfiguration {

	@Bean
	public PreTpicAsyncBeanPostProcessor preTpicAsyncBeanPostProcessor(AsyncTaskExecutor executor, TraceeBackend backend) {
		return new PreTpicAsyncBeanPostProcessor(executor, backend);
	}

	@Bean
	public PostTpicAsyncBeanPostProcessor postTpicAsyncBeanPostProcessor(AsyncTaskExecutor executor, TraceeBackend backend) {
		return new PostTpicAsyncBeanPostProcessor(executor, backend);
	}
}
