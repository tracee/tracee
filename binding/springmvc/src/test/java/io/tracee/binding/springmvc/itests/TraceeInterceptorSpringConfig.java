package io.tracee.binding.springmvc.itests;

import io.tracee.binding.springmvc.TraceeInterceptor;
import io.tracee.binding.springmvc.TraceeResponseBodyAdvice;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.util.Collections;

@Configuration
@ComponentScan(basePackageClasses = TraceeInterceptorSpringConfig.class,
		excludeFilters = @ComponentScan.Filter(value = TraceeResponseBodyAdvice.class, type = FilterType.ASSIGNABLE_TYPE))
public class TraceeInterceptorSpringConfig extends DelegatingWebMvcConfiguration implements WebApplicationInitializer {

	@Bean
	public TraceeInterceptor traceeInterceptor() {
		final TraceeInterceptor interceptor = new TraceeInterceptor();
		interceptor.setProfileName("default");
		return interceptor;
	}

	@Bean
	public TraceeResponseBodyAdvice responseBodyAdvice() {
		return new TraceeResponseBodyAdvice();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(traceeInterceptor());
	}

	@Override
	public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
		final RequestMappingHandlerAdapter handlerAdapter = super.requestMappingHandlerAdapter();
		handlerAdapter.setResponseBodyAdvice(Collections.<ResponseBodyAdvice<?>>singletonList(responseBodyAdvice()));
		return handlerAdapter;
	}

	@Override
	public void onStartup(ServletContext servletCtx) throws ServletException {
		AnnotationConfigWebApplicationContext rootCtx = new AnnotationConfigWebApplicationContext();
		rootCtx.register(TraceeInterceptorSpringConfig.class);

		// Manage the lifecycle of the root application context
		servletCtx.addListener(new ContextLoaderListener(rootCtx));

		// Create the dispatcher servlet's Spring application context
		AnnotationConfigWebApplicationContext dispatcherContext = new AnnotationConfigWebApplicationContext();
		dispatcherContext.register(TraceeInterceptorSpringConfig.class);

		// Register and map the dispatcher servlet
		ServletRegistration.Dynamic dispatcher = servletCtx.addServlet("dispatcher", new DispatcherServlet(dispatcherContext));
		dispatcher.setLoadOnStartup(1);
		dispatcher.addMapping("/");
	}
}
