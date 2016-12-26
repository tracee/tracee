package io.tracee.spring.autoconfigure;

import io.tracee.binding.springmvc.TraceeInterceptor;
import io.tracee.binding.springmvc.config.TraceeSpringMvcConfiguration;
import io.tracee.testhelper.FieldAccessUtil;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest(value = {"server.port=0"}, classes = TraceeMvcAutoConfigurationIT.Config.class)
public class TraceeMvcAutoConfigurationIT {

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


	@SuppressWarnings("unchecked")
	@Test
	public void testInterceptorIsConfigured() {
		final Object configurerAdapter = context.getBean(TraceeSpringMvcConfiguration.TRACEE_WEBMVCCONFIGURERADAPTER_INTERNAL);
		assertNotNull(configurerAdapter);
		final WebMvcConfigurationSupport webmvc = context.getBean(WebMvcConfigurationSupport.class);

		final List<Object> listeners = FieldAccessUtil.getFieldVal(webmvc, "interceptors");
		Assert.assertThat(listeners, Matchers.hasItem(Matchers.instanceOf(TraceeInterceptor.class)));

	}
}
