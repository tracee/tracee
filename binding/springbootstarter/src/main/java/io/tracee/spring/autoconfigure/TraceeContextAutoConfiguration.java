package io.tracee.spring.autoconfigure;

import io.tracee.TraceeBackend;
import io.tracee.binding.spring.context.config.TraceeContextConfiguration;
import io.tracee.configuration.TraceeFilterConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @since 2.0
 */
@Configuration
class TraceeContextAutoConfiguration {


	@Configuration
	@ConditionalOnMissingBean(TraceeBackend.class)
	@ConditionalOnProperty(value = "tracee.enabled", matchIfMissing = true)
	@Import(TraceeContextConfiguration.class)
	public static class TraceeBackendAutoConfiguration {

	}

	/**
	 * Expose TraceeProperties as TraceeFilterConfiguration if not other FilterConfiguration is in place.
	 */
	@Configuration
	@ConditionalOnMissingBean(TraceeFilterConfiguration.class)
	@ConditionalOnProperty(value = "tracee.enabled", matchIfMissing = true)
	public static class TraceePropertiesAutoConfiguration {

		@Autowired TraceeProperties traceeProperties;

		@Bean
		public TraceeFilterConfiguration filterConfiguration() {
			return traceeProperties.getAsFilterConfiguration();
		}
	}


}
