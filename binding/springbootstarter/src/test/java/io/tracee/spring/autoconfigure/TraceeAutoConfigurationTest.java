package io.tracee.spring.autoconfigure;


import io.tracee.TraceeBackend;
import io.tracee.binding.jms.TraceeConnectionFactory;
import io.tracee.binding.springhttpclient.TraceeClientHttpRequestInterceptor;
import io.tracee.configuration.TraceeFilterConfiguration;
import io.tracee.configuration.TraceeFilterConfiguration.Channel;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.AnnotationConfigEmbeddedWebApplicationContext;
import org.springframework.boot.test.EnvironmentTestUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.client.RestTemplate;

import javax.jms.ConnectionFactory;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.*;

public class TraceeAutoConfigurationTest {

	private GenericApplicationContext context;

	@After
	public void tearDown() {
		if (this.context != null) {
			this.context.close();
			//ApplicationContextTestUtils.closeAll(this.context);
		}
	}

	@Configuration
	@EnableAutoConfiguration
	static class EmptyConfiguration {}

	@Test
	public void providesBackend() {
		load(EmptyConfiguration.class, "foo=Bar");
		assertNotNull(this.context.getBean(TraceeBackend.class));
	}

	@Test
	public void providesBackendDoesNotProvideSpecializedBeans() {
		load(EmptyConfiguration.class, "foo=Bar");
		assertNull(this.context.getBean(TraceeSpringAsyncAutoConfiguration.class));
		assertNull(this.context.getBean(TraceeSpringJmsAutoConfiguration.class));
	}

	@Test
	public void providesConfiguration() {
		load(EmptyConfiguration.class,
		"spring.debug=true",
		"tracee.filter.IncomingRequest=.*",
		"tracee.filter.OutgoingResponse=.*",
		"tracee.filter.OutgoingRequest=.*",
		"tracee.filter.IncomingResponse=.*",
		"tracee.filter.AsyncDispatch=.*",
		"tracee.profile.DDDANGER.filter.AsyncProcess=abc",
		"tracee.invocationIdLength=32",
		"tracee.sessionIdLength=32");
		//todo: we should check here that the properties are actually loaded!
		final TraceeProperties props = this.context.getBean(TraceeProperties.class);

		assertNotNull(props);
		assertThat(props.getSessionIdLength(), equalTo(32));
		assertThat(props.getProfile().get("DDDANGER").getFilter().get(Channel.AsyncProcess).toString(), equalTo("abc"));
		assertThat(props.getFilter().get(Channel.IncomingRequest).toString(), equalTo(".*"));
		assertThat(props.getInvocationIdLength(), equalTo(32));
		assertThat(props.getSessionIdLength(), equalTo(32));
	}



	@Configuration
	@EnableAutoConfiguration
	static class ProvidedFilterConfigurationConfiguration {

		static final TraceeFilterConfiguration filterConfigurationMock = Mockito.mock(TraceeFilterConfiguration.class);

		@Bean
		TraceeFilterConfiguration filterConfiguration() {
			return filterConfigurationMock;
		}
	}

	@Test
	public void providesNoFilterConfigurationCustomIsDefined() {
		load(ProvidedFilterConfigurationConfiguration.class,
			"tracee.invocationIdLength=32");
		final TraceeProperties props = this.context.getBean(TraceeProperties.class);
		final TraceeFilterConfiguration filterConfiguration = this.context.getBean(TraceeFilterConfiguration.class);
		assertThat(props.getInvocationIdLength(), equalTo(32));
		assertThat(filterConfiguration, sameInstance(ProvidedFilterConfigurationConfiguration.filterConfigurationMock));

	}


	private static final TraceeBackend backendMock = Mockito.mock(TraceeBackend.class);

	@Configuration
	@EnableAutoConfiguration
	static class ConfigurationWithPredefinedBackend {
		@Bean TraceeBackend traceeBackend() {
			return backendMock;
		}
	}

	@Test
	public void doesNotOverrideExistingBackend() {
		load(ConfigurationWithPredefinedBackend.class, "foo=Bar");
		assertSame(backendMock, this.context.getBean(TraceeBackend.class));
	}

	private static final TraceeFilterConfiguration filterConfigurationMock = Mockito.mock(TraceeFilterConfiguration.class);

	@Configuration
	@EnableAutoConfiguration
	static class ConfigurationWithPredefinedFilterConfiguration {
		@Bean TraceeFilterConfiguration traceeFilterConfiguration() {
			return filterConfigurationMock;
		}
	}

	@Test
	public void doesNotOverrideExistingFilterConfiguration() {
		load(ConfigurationWithPredefinedFilterConfiguration.class, "foo=Bar");
		assertSame(filterConfigurationMock, this.context.getBean(TraceeFilterConfiguration.class));
	}

	@Configuration
	@EnableAutoConfiguration
	static class RestTemplateConfiguration {
		@Bean
		RestTemplate restTemplate() {
			return new RestTemplate();
		}
	}

	@Test
	public void providesRestTemplateInterceptor() {
		load(RestTemplateConfiguration.class, "foo=bar");
		final RestTemplate restTemplate = this.context.getBean(RestTemplate.class);
		assertNotNull(restTemplate);
		Assert.assertThat(restTemplate.getInterceptors(), Matchers.<ClientHttpRequestInterceptor>hasItem(instanceOf(TraceeClientHttpRequestInterceptor.class))  );
	}


	@EnableJms
	@EnableAutoConfiguration
	static class JMSConfiguration {

		@Autowired
		ConnectionFactory connectionFactory;

		@Autowired
		JmsTemplate jmsTemplate;

	}

	@Test
	public void providesTraceeConnectionFactory() {

		load(JMSConfiguration.class, "debug=true","logging.level.=DEBUG");
		Object connectionFactory = context.getBean(ConnectionFactory.class);
		assertThat(connectionFactory, Matchers.instanceOf(TraceeConnectionFactory.class));
		JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
		assertThat(jmsTemplate.getConnectionFactory(), Matchers.instanceOf(TraceeConnectionFactory.class));
	}

	private void loadWeb(Class<?> config, String... environment) {
		AnnotationConfigEmbeddedWebApplicationContext applicationContext = new AnnotationConfigEmbeddedWebApplicationContext();
		EnvironmentTestUtils.addEnvironment(applicationContext, environment);
		applicationContext.register(config);
		applicationContext.refresh();
		this.context = applicationContext;
	}

	private void load(Class<?> config, String... environment) {
		final AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();

		EnvironmentTestUtils.addEnvironment(applicationContext, environment);
		applicationContext.register(config);
		applicationContext.refresh();
		this.context = applicationContext;
	}

}
