package io.tracee.binding.springmvc;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.binding.springmvc.config.TraceeSpringMVCConfiguration;
import io.tracee.configuration.TraceeFilterConfiguration;
import io.tracee.testhelper.PermitAllTraceeFilterConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@Configuration
@EnableWebMvc
@Import({TraceeSpringMVCConfiguration.class, TraceeInterceptorJavaConfigIT.SillyController.class})
public class TraceeInterceptorJavaConfigITConfig {


	// Just another configurer to demonstrate that
	@Bean
	WebMvcConfigurer anotherConfigurer() {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addInterceptors(InterceptorRegistry registry) {
			}
		};
	}

	@Autowired
	TraceeInterceptorJavaConfigIT.SillyController sillyController;

	@Bean
	TraceeBackend traceeBackend() {
		return Tracee.getBackend();
	}

	@Bean
	TraceeFilterConfiguration filterConfiguration() {
		return PermitAllTraceeFilterConfiguration.INSTANCE;
	}

}
