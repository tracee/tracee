package io.tracee.binding.springws.config;

import io.tracee.TraceeBackend;
import io.tracee.binding.springws.TraceeClientInterceptor;
import io.tracee.binding.springws.TraceeEndpointInterceptor;
import io.tracee.configuration.TraceeFilterConfiguration;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.server.EndpointInterceptor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Configuration
public class TraceeSpringWsConfiguration {

	@Autowired
	TraceeBackend backend;

	@Bean
	TraceeEndpointInterceptor traceeEndpointInterceptor() {
		return new TraceeEndpointInterceptor(backend, TraceeFilterConfiguration.Profile.DEFAULT);
	}

	@Bean
	TraceeClientInterceptor traceeClientInterceptor() {
		return new TraceeClientInterceptor(backend, TraceeFilterConfiguration.Profile.DEFAULT);
	}

	/**
	 * Provides a WsConfigurerAdapter that adds the traceeEndpointInterceptor to the list of EndpointInterceptors.
	 */
	@Bean
	WsConfigurerAdapter traceeWsConfigurerAdapter(final TraceeEndpointInterceptor traceeEndpointInterceptor) {
		return new WsConfigurerAdapter() {
			@Override
			public void addInterceptors(List<EndpointInterceptor> interceptors) {
				super.addInterceptors(Collections.<EndpointInterceptor>singletonList(traceeEndpointInterceptor));
			}
		};
	}

	/**
	 * Provides a BeanPostProcessor that attaches the traceeClientInterceptor to all created WebServiceTemplates.
	 */
	@Bean(name = "io.tracee.binding.springhttpclient.config.RestTemplatePostProcessor")
	WebServiceTemplatePostProcessor restTemplatePostProcessor(TraceeClientInterceptor traceeClientInterceptor) {
		return new WebServiceTemplatePostProcessor(traceeClientInterceptor);
	}

	static class WebServiceTemplatePostProcessor implements BeanPostProcessor {

		final TraceeClientInterceptor interceptor;

		WebServiceTemplatePostProcessor(TraceeClientInterceptor interceptor) {
			this.interceptor = interceptor;
		}

		@Override
		public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
			if (bean instanceof WebServiceTemplate) {
				final WebServiceTemplate webServiceTemplate = (WebServiceTemplate)bean;
				final ClientInterceptor[] interceptors = webServiceTemplate.getInterceptors();
				final ClientInterceptor[] newInterceptors;
				if (interceptors != null) {
					newInterceptors = Arrays.copyOf(interceptors, interceptors.length+1);
				} else {
					newInterceptors = new ClientInterceptor[1];
				}
				newInterceptors[newInterceptors.length-1] = interceptor;
				webServiceTemplate.setInterceptors(newInterceptors);
			}
			return bean;
		}
		@Override
		public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
			return bean;
		}
	}

}
