package io.tracee.spring.autoconfigure;


import io.tracee.binding.springhttpclient.config.TraceeSpringWebConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestTemplate;

/**
 * @since 2.0
 */
@Configuration
@Import(TraceeSpringWebConfiguration.class)
@ConditionalOnClass(RestTemplate.class)
@ConditionalOnBean(RestTemplate.class)
@AutoConfigureBefore(TraceeContextAutoConfiguration.class)
class TraceeSpringWebAutoConfiguration {



}
