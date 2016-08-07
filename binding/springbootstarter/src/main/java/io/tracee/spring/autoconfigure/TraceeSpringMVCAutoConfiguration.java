package io.tracee.spring.autoconfigure;


import io.tracee.TraceeBackend;
import io.tracee.binding.springmvc.TraceeInterceptor;
import io.tracee.binding.spring.context.async.PostTpicAsyncBeanPostProcessor;
import io.tracee.binding.spring.context.async.PreTpicAsyncBeanPostProcessor;
import io.tracee.binding.springmvc.config.TraceeSpringMVCConfiguration;
import io.tracee.configuration.TraceeFilterConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @since 2.0
 */
@Configuration
@ConditionalOnWebApplication
@AutoConfigureBefore(TraceeContextAutoConfiguration.class)
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
@Import(TraceeSpringMVCConfiguration.class)
class TraceeSpringMVCAutoConfiguration extends WebMvcConfigurerAdapter {

	static final String TRACEE_INTERCEPTOR_INTERNAL = "io.tracee.spring.autoconfigure.traceeInterceptor_internal";

	@Autowired
	private TraceeBackend traceeBackend;

	@Autowired
	private TraceeFilterConfiguration traceeFilterConfiguration;

	@Bean(name = TRACEE_INTERCEPTOR_INTERNAL)
	TraceeInterceptor traceeInterceptor() {
		return new TraceeInterceptor(traceeBackend, traceeFilterConfiguration);
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(traceeInterceptor());
	}

}
