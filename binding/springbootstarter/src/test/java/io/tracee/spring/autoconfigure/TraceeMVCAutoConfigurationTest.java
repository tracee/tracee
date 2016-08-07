package io.tracee.spring.autoconfigure;

import io.tracee.binding.springmvc.TraceeInterceptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import static org.junit.Assert.assertNotNull;

@RunWith( SpringJUnit4ClassRunner.class )
@SpringApplicationConfiguration( classes = TraceeMVCAutoConfigurationTest.Config.class )
@WebAppConfiguration
@IntegrationTest( {"server.port=0"} )
public class TraceeMVCAutoConfigurationTest
{

	@Autowired
	ApplicationContext context;

	@Configuration
	@EnableAutoConfiguration
	@EnableWebMvc
	public static class Config {


		@Controller
		static class CRTL {
			@RequestMapping(value = "/ctrl", method = RequestMethod.GET)
			public void ok() {
			}
		}
	}



	@Test
	public void testHalloString()
	{
		final Object interceptorByName = context.getBean(TraceeSpringMVCAutoConfiguration.TRACEE_INTERCEPTOR_INTERNAL);
		final TraceeInterceptor interceptor = context.getBean(TraceeInterceptor.class);
		assertNotNull(interceptorByName);
		assertNotNull(interceptor);
		final WebMvcConfigurationSupport webmvc = context.getBean(WebMvcConfigurationSupport.class);
		// TODO check if we can extract the registered listener from the WebMvcConfigurationSupport

	}
}
