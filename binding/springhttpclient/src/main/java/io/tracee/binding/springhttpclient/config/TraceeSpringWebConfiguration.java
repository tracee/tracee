package io.tracee.binding.springhttpclient.config;


import io.tracee.TraceeBackend;
import io.tracee.binding.springhttpclient.TraceeClientHttpRequestInterceptor;
import io.tracee.configuration.TraceeFilterConfiguration;
import io.tracee.transport.HttpHeaderTransport;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.web.client.RestTemplate;

@Configuration
public class TraceeSpringWebConfiguration {

	@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
	@Bean
	TraceeClientHttpRequestInterceptor traceeClientHttpRequestInterceptor(TraceeBackend traceeBackend) {
		return new TraceeClientHttpRequestInterceptor(traceeBackend, new HttpHeaderTransport(), TraceeFilterConfiguration.Profile.DEFAULT);
	}

	@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
	@Bean
	BeanPostProcessor restTemplatePostProcessor(TraceeClientHttpRequestInterceptor traceeClientHttpRequestInterceptor) {
		return new RestTemplatePostProcessor(traceeClientHttpRequestInterceptor);
	}

	private static class RestTemplatePostProcessor implements BeanPostProcessor {

		private final TraceeClientHttpRequestInterceptor interceptor;

		private RestTemplatePostProcessor(TraceeClientHttpRequestInterceptor interceptor) {
			this.interceptor = interceptor;
		}

		@Override
		public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
			if (bean instanceof RestTemplate) {
				((RestTemplate) bean).getInterceptors().add(interceptor);
			}
			return bean;
		}

		@Override
		public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
			return bean;
		}
	}
}
