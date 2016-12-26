package io.tracee.spring.autoconfigure;

import io.tracee.TraceeBackend;
import io.tracee.binding.springhttpclient.TraceeClientHttpRequestInterceptor;
import io.tracee.configuration.TraceeFilterConfiguration;
import io.tracee.configuration.TraceeFilterConfiguration.Channel;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.EnvironmentTestUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;

public class TraceeAutoConfigurationTest {

	private static final TraceeBackend backendMock = Mockito.mock(TraceeBackend.class);
	private static final TraceeFilterConfiguration filterConfigurationMock = Mockito.mock(TraceeFilterConfiguration.class);

	private GenericApplicationContext context;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@After
	public void tearDown() {
		if (this.context != null) {
			this.context.close();
		}
	}
	@Configuration
	@EnableAutoConfiguration
	static class RequiresBackendConfiguration {
		@Autowired
		TraceeBackend backend;

	}

	@Test
	public void providesBackend() {
		load(RequiresBackendConfiguration.class);
		assertNotNull(this.context.getBean(TraceeBackend.class));
	}
	@Configuration
	@EnableAutoConfiguration
	static class EmptyConfiguration {

	}

	@Test
	public void emptyConfigurationProducesNoTraceeSpringAsyncAutoConfiguration() {
		load(EmptyConfiguration.class);
		thrown.expect(NoSuchBeanDefinitionException.class);
		this.context.getBean(TraceeSpringAsyncAutoConfiguration.class);
	}

	@Test
	public void emptyConfigurationProducesNoTraceeSpringMvcAutoConfiguration() {
		load(EmptyConfiguration.class);
		thrown.expect(NoSuchBeanDefinitionException.class);
		this.context.getBean(TraceeSpringMvcAutoConfiguration.class);
	}


	@Test
	public void emptyConfigurationProducesNoTraceeSpringWebAutoConfiguration() {
		load(EmptyConfiguration.class);
		thrown.expect(NoSuchBeanDefinitionException.class);
		this.context.getBean(TraceeSpringWebAutoConfiguration.class);
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
	@Configuration
	@EnableAutoConfiguration
	static class ConfigurationWithPredefinedBackend {
		@Bean
		TraceeBackend traceeBackend() {
			return backendMock;
		}

	}

	@Test
	public void doesNotOverrideExistingBackend() {
		load(ConfigurationWithPredefinedBackend.class);
		assertSame(backendMock, this.context.getBean(TraceeBackend.class));
	}

	@Configuration
	@EnableAutoConfiguration
	static class ConfigurationWithPredefinedFilterConfiguration {
		@Bean
		TraceeFilterConfiguration traceeFilterConfiguration() {
			return filterConfigurationMock;
		}
	}

	@Test
	public void doesNotOverrideExistingFilterConfiguration() {
		load(ConfigurationWithPredefinedFilterConfiguration.class);
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
		load(RestTemplateConfiguration.class);
		final RestTemplate restTemplate = this.context.getBean(RestTemplate.class);
		assertNotNull(restTemplate);
		assertThat(restTemplate.getInterceptors(), Matchers.<ClientHttpRequestInterceptor>hasItem(instanceOf(TraceeClientHttpRequestInterceptor.class)));
	}

	private void load(Class<?> config, String... environment) {
		final AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();

		EnvironmentTestUtils.addEnvironment(applicationContext, environment);
		applicationContext.register(config);
		applicationContext.refresh();
		this.context = applicationContext;
	}
}
