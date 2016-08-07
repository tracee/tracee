package io.tracee.spring.autoconfigure;

import io.tracee.Tracee;
import io.tracee.binding.springmvc.config.TraceeSpringMvcConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @since 2.0
 */
@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass(Tracee.class)
@AutoConfigureBefore(TraceeContextAutoConfiguration.class)
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
@Import(TraceeSpringMvcConfiguration.class)
public class TraceeSpringMvcAutoConfiguration {}
