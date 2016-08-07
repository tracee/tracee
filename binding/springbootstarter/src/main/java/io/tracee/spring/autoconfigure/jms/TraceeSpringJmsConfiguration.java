package io.tracee.spring.autoconfigure.jms;


import io.tracee.TraceeBackend;
import io.tracee.binding.jms.TraceeConnectionFactory;
import io.tracee.configuration.TraceeFilterConfiguration;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;

import javax.jms.ConnectionFactory;

/**
 * Proxies jms ConnectionFactories with {@link TraceeConnectionFactory} to get a deep proxy of the JMS api in
 * order to inject send, receive and listener hooks.
 *
 * //TODO move to its own tracee-springjms package so it can be used without
 * @since 2.0
 */
@Configuration
public class TraceeSpringJmsConfiguration {


	@Bean
	@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
	BeanPostProcessor traceeJmsBeanPostProcessor(final TraceeBackend backend, final TraceeFilterConfiguration filterConfiguration) {
		return new BeanPostProcessor() {
			@Override
			public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
				return bean;
			}

			@Override
			public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
				if (bean instanceof TraceeConnectionFactory) {
					return bean;
				} else if (bean instanceof ConnectionFactory) {
					return new TraceeConnectionFactory((ConnectionFactory) bean, backend, filterConfiguration);
				} else {
					return bean;
				}
			}
		};
	}

}
