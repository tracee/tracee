package io.tracee.binding.springmvc.itests;

import io.tracee.Tracee;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.After;
import org.junit.Before;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.net.InetSocketAddress;

public abstract class WebIntegrationIT {

	protected Server server;
	protected String ENDPOINT_URL;

	@Before
	public void startJetty() throws Exception {
		Tracee.getBackend().clear();

		server = new Server(new InetSocketAddress("127.0.0.1", 0));
		ServletContextHandler context = new ServletContextHandler(null, "/", ServletContextHandler.NO_SECURITY);
		final DispatcherServlet dispatcherServlet = new DispatcherServlet();
		dispatcherServlet.setContextClass(AnnotationConfigWebApplicationContext.class);
		dispatcherServlet.setContextInitializerClasses(TraceeInterceptorSpringApplicationInitializer.class.getCanonicalName());
		dispatcherServlet.setContextConfigLocation(TraceeInterceptorSpringConfig.class.getName());
		context.addServlet(new ServletHolder(dispatcherServlet), "/");
		server.setHandler(context);
		server.start();
		ENDPOINT_URL = "http://" + server.getConnectors()[0].getName() + "/";

	}

	@After
	public void stopJetty() throws Exception {
		if (server != null) {
			server.stop();
		}
		Tracee.getBackend().clear();
	}
}
