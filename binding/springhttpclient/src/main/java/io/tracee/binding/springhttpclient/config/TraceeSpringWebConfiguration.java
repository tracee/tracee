package io.tracee.binding.springhttpclient.config;


import io.tracee.TraceeBackend;
import io.tracee.binding.springhttpclient.TraceeClientHttpRequestInterceptor;
import io.tracee.configuration.TraceeFilterConfiguration;
import io.tracee.transport.HttpHeaderTransport;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.web.client.RestTemplate;

@Configuration
public class TraceeSpringWebConfiguration {

	@Autowired
	private TraceeBackend backend;

	@Autowired
	private TraceeFilterConfiguration filterConfiguration;

	@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
	@Bean(name = "io.tracee.binding.springhttpclient.config.RestTemplatePostProcessor")
	BeanPostProcessor restTemplatePostProcessor() {
		return new RestTemplatePostProcessor(traceeClientHttpRequestInterceptor());
	}

	@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
	@Bean(name = "io.tracee.binding.springhttpclient.config.TraceeClientHttpRequestInterceptor")
	TraceeClientHttpRequestInterceptor traceeClientHttpRequestInterceptor() {
		return new TraceeClientHttpRequestInterceptor(backend, new HttpHeaderTransport(), filterConfiguration);
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
