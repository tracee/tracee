package io.tracee.binding.springmvc.itests;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

public class TraceeInterceptorSpringApplicationInitializer implements ApplicationContextInitializer<AnnotationConfigWebApplicationContext> {

	@Override
	public void initialize(AnnotationConfigWebApplicationContext applicationContext) {
		final AnnotationConfigWebApplicationContext rootCtx = new AnnotationConfigWebApplicationContext();
		rootCtx.register(TraceeInterceptorSpringConfig.class);
		final ServletContext servletContext = applicationContext.getServletContext();

		// Manage the lifecycle of the root application context
		servletContext.addListener(new ContextLoaderListener(rootCtx));

		// Create the dispatcher servlet's Spring application context
		AnnotationConfigWebApplicationContext dispatcherContext = new AnnotationConfigWebApplicationContext();
		dispatcherContext.register(TraceeInterceptorSpringConfig.class);

		// Register and map the dispatcher servlet
		ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", new DispatcherServlet(dispatcherContext));
		dispatcher.setLoadOnStartup(1);
		dispatcher.addMapping("/");

	}
}
