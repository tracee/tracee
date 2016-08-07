package io.tracee.binding.springmvc.config;

import io.tracee.TraceeBackend;
import io.tracee.binding.springmvc.TraceeInterceptor;
import io.tracee.configuration.TraceeFilterConfiguration;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
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
public class TraceeSpringMVCConfiguration extends WebMvcConfigurerAdapter {

	private static final String TRACEE_INTERCEPTOR_INTERNAL = "io.tracee.spring.autoconfigure.traceeInterceptor_internal";

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
