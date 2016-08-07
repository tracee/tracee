package io.tracee.binding.springws.config;


import io.tracee.binding.springws.TraceeClientInterceptor;
import io.tracee.configuration.TraceeFilterConfiguration;
import io.tracee.testhelper.SimpleTraceeBackend;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verifyZeroInteractions;

public class TraceeSpringWsConfigurationTest {


	final TraceeClientInterceptor traceeClientInterceptor = new TraceeClientInterceptor(SimpleTraceeBackend.createNonLoggingAllPermittingBackend(), TraceeFilterConfiguration.Profile.DEFAULT);
	final TraceeSpringWsConfiguration.WebServiceTemplatePostProcessor unit = new TraceeSpringWsConfiguration.WebServiceTemplatePostProcessor(traceeClientInterceptor);
	final WebServiceTemplate webServiceTemplate = spy(WebServiceTemplate.class);

	@Test
	public void webServiceTemplatePostProcessorAddsTraceeClientInterceptorToWebServiceTemplateInterceptors() {
		unit.postProcessBeforeInitialization(webServiceTemplate, "");
		Assert.assertThat(webServiceTemplate.getInterceptors(), Matchers.arrayContaining((ClientInterceptor)traceeClientInterceptor));
	}

	@Test
	public void webServiceTemplatePostProcessorDoesNotTouchAnyOtherBean() {
		final Object untouched = Mockito.mock(Object.class);
		unit.postProcessBeforeInitialization(new Object(), "");
		verifyZeroInteractions(untouched);
	}

}
