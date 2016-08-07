package io.tracee.binding.springmvc.config;

import io.tracee.TraceeBackend;
import io.tracee.binding.springmvc.TraceeInterceptor;
import io.tracee.binding.springmvc.TraceeResponseBodyAdvice;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @since 2.0
 */
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Configuration
public class TraceeSpringMvcConfiguration {

	public static final String TRACEE_WEBMVCCONFIGURERADAPTER_INTERNAL = "io.tracee.binding.springmvc.config.WebMvcConfigurerAdapter_internal";

	@Bean
	TraceeInterceptor traceeInterceptor(final TraceeBackend backend) {
		return new TraceeInterceptor(backend);
	}

	@Bean(name = TRACEE_WEBMVCCONFIGURERADAPTER_INTERNAL)
	WebMvcConfigurerAdapter traceSpringMvcWebMvcConfigurerAdapter(final TraceeInterceptor traceeInterceptor) {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addInterceptors(InterceptorRegistry registry) {
				registry.addInterceptor(traceeInterceptor);
			}
		};
	}

	@Bean
	TraceeResponseBodyAdvice traceeSpringMvcResponseBodyAdvice() {
		return new TraceeResponseBodyAdvice();
	}
}
