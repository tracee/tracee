package io.tracee.binding.springmvc.itests;

import io.tracee.binding.spring.context.config.TraceeContextConfiguration;
import io.tracee.binding.springmvc.config.TraceeSpringMvcConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@Import({TraceeContextConfiguration.class, TraceeSpringMvcConfiguration.class})
@Configuration
@ComponentScan(basePackageClasses = TraceeInterceptorSpringConfig.class)
public class TraceeInterceptorSpringConfig  {

}
