package io.tracee.spring.autoconfigure;

import io.tracee.Tracee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @since 2.0
 */
@Configuration
@EnableConfigurationProperties(TraceeProperties.class)
@ConditionalOnClass(Tracee.class)
public class TraceePropertiesAutoConfiguration {

	/**
	 * Provide spring Tracee configuration
	 */
	@Autowired TraceeProperties traceeProperties;


}
