package io.tracee.spring.autoconfigure;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.binding.spring.context.config.TraceeContextConfiguration;
import io.tracee.configuration.TraceeFilterConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;

/**
 * @since 2.0
 */
@Configuration
@ConditionalOnClass(Tracee.class)
@EnableConfigurationProperties(TraceeProperties.class)
public class TraceeContextAutoConfiguration {

	/**
	 * Provide spring Tracee configuration
	 */
	@Autowired
	TraceeProperties traceeProperties;

	@Configuration
	@ConditionalOnMissingBean(TraceeBackend.class)
	public static class TraceeBackendAutoConfiguration extends TraceeContextConfiguration {
	}

	/**
	 * Expose TraceeProperties as TraceeFilterConfiguration if not other FilterConfiguration is in place.
	 */
	@Configuration
	@ConditionalOnMissingBean(TraceeFilterConfiguration.class)
	public static class TraceePropertiesAutoConfiguration {

		@Role(BeanDefinition.ROLE_SUPPORT)
		@Bean
		public TraceeFilterConfiguration filterConfiguration(TraceeProperties traceeProperties) {
			return traceeProperties.getAsFilterConfiguration();
		}
	}


}
