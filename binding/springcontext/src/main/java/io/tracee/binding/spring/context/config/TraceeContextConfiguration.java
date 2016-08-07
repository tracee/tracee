package io.tracee.binding.spring.context.config;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;

@Role(BeanDefinition.ROLE_SUPPORT)
@Configuration
public class TraceeContextConfiguration {


	@Bean
	TraceeBackend traceeBackend() {
		return Tracee.getBackend();
	}


}
