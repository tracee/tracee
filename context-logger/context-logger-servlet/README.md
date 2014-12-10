> This document contains documentation for the tracee context-logger-servlet module. Click [here](/README.md) to get an overview that TracEE is about.

# context-logger-servlet

> The TracEE context-logger-servlet project offers a Servlet Filter that outputs contextual data of a servlet request if an uncaught exception is detected by the filter. 

## Example output
Depending on the selected context logger profile the output consists of servlet request related data like request parameters, url, thrown exception, up to cookies and session attributes.
This is a an example for an output with ENHANCED context logger profile.

    Sep 05, 2014 4:29:42 PM io.tracee.backend.slf4j.Slf4jTraceeLogger error
    {
    	"common": {
    		"timestamp": "Sep 5, 2014 4:39:40 PM",
    		"stage": "DEV",
    		"system-name": "localhost",
    		"thread-name": "http-bio-8080-exec-2",
    		"thread-id": 66
    	},
    	"tracee": {
    		"TracEE-sessionId": "030f1790ce0ac0fc08e677f0920f3450",
    		"TracEE-requestId": "SHX5FB9C7PAMSDOJWJPHZIBFKATX63NB"
    	},
    	"servletRequest": {
    		"url": "http://localhost:8080/traceeTestWebapp/index.jsf",
    		"http-method": "POST",
    		"http-parameters": [{
    			"name": "j_id_6:j_id_a",
    			"value": ""
    		},
    		{
    			"name": "j_id_6:j_id_s",
    			"value": "Remote EJB Trigger Exception"
    		},
    		{
    			"name": "j_id_6_SUBMIT",
    			"value": "1"
    		},
    		{
    			"name": "j_id_6:j_id_8",
    			"value": ""
    		},
    		{
    			"name": "javax.faces.ViewState",
    			"value": "4f4Ixoh8roSGvgAr5nd4cO1M10dLOfU22xBA6G7zkztLXqY2"
    		}],
    		"http-request-headers": [{
    			"name": "host",
    			"value": "localhost:8080"
    		},
    		{
    			"name": "user-agent",
    			"value": "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0"
    		},
    		{
    			"name": "accept",
    			"value": "text/html,application/xhtml+xml,application/xml;q\u003d0.9,*/*;q\u003d0.8"
    		},
    		{
    			"name": "accept-language",
    			"value": "de,en-US;q\u003d0.7,en;q\u003d0.3"
    		},
    		{
    			"name": "accept-encoding",
    			"value": "gzip, deflate"
    		},
    		{
    			"name": "dnt",
    			"value": "1"
    		},
    		{
    			"name": "referer",
    			"value": "http://localhost:8080/traceeTestWebapp/index.jsf"
    		},
    		{
    			"name": "cookie",
    			"value": "JSESSIONID\u003d0D3E05677F88C98D20FE52A000153266; oam.Flash.RENDERMAP.TOKEN\u003d-gy3iegekb"
    		},
    		{
    			"name": "connection",
    			"value": "keep-alive"
    		},
    		{
    			"name": "content-type",
    			"value": "application/x-www-form-urlencoded"
    		},
    		{
    			"name": "content-length",
    			"value": "165"
    		}],
    		"http-request-attributes": [{
    			"name": "oam.Flash.EXECUTEMAP",
    			"value": {
    				
    			}
    		},
    		{
    			"name": "oam.Flash.EXECUTEMAP.TOKEN",
    			"value": "-gy3iegekb"
    		},
    		{
    			"name": "oam.Flash.RENDERMAP.TOKEN",
    			"value": "-gy3iegeka"
    		},
    		{
    			"name": "payload",
    			"value": {
    				"firstArgument": 0,
    				"secondArgument": 0
    			}
    		},
    		{
    			"name": "testWebappEjbController",
    			"value": {
    				"testEjb": {
    					"h": {
    						"registryId": "TestEjbImplDefault Stateless Container",
    						"strategy": "CLASSLOADER_COPY",
    						"deploymentID": "TestEjbImpl",
    						"inProxyMap": false,
    						"isInvalidReference": false,
    						"doIntraVmCopy": true,
    						"doCrossClassLoaderCopy": true,
    						"interfaceType": "BUSINESS_REMOTE"
    					}
    				},
    				"payload": {
    					"firstArgument": 0,
    					"secondArgument": 0
    				}
    			}
    		}],
    		"cookies": [{
    			
    		},
    		{
    			
    		}],
    		"http-remote-address": "0:0:0:0:0:0:0:1",
    		"http-remote-host": "0:0:0:0:0:0:0:1",
    		"http-remote-port": 58855,
    		"scheme": "http",
    		"is-secure": false,
    		"content-type": "application/x-www-form-urlencoded",
    		"content-length": 165,
    		"locale": "de"
    	},
    	"servletResponse": {
    		"http-status-code": 500,
    		"http-header": [{
    			"name": "TPIC",
    			"value": "{\"traceeRequestId\":\"SHX5FB9C7PAMSDOJWJPHZIBFKATX63NB\",\"traceeSessionId\":\"030f1790ce0ac0fc08e677f0920f3450\"}"
    		},
    		{
    			"name": "Set-Cookie",
    			"value": "oam.Flash.RENDERMAP.TOKEN\u003d-gy3iegeka; Path\u003d/traceeTestWebapp"
    		}]
    	},
    	"throwable": {
    		"message": "javax.el.ELException: javax.ejb.EJBException: The bean encountered a non-application exception; nested exception is: \n\tjava.lang.NullPointerException: Tracee local Remote EJB example: Triggered exception with passed parameters \u00270\u0027 and \u00270\u0027",
    		"stacktrace": "javax.servlet.ServletException: javax.el.ELException: javax.ejb.EJBException: The bean encountered a non-application exception; nested exception is: \n\tjava.lang.NullPointerException: Tracee local Remote EJB example: Triggered exception with passed parameters \u00270\u0027 and \u00270\u0027\r\n\tat javax.faces.webapp.FacesServlet.service(FacesServlet.java:229)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:305)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:210)\r\n\tat io.tracee.servlet.TraceeFilter.doFilterHttp(TraceeFilter.java:56)\r\n\tat io.tracee.servlet.TraceeFilter.doFilter(TraceeFilter.java:42)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:243)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:210)\r\n\tat io.tracee.contextlogger.servlet.TraceeErrorLoggingFilter.doFilter(TraceeErrorLoggingFilter.java:31)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:243)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:210)\r\n\tat org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:222)\r\n\tat org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:123)\r\n\tat org.apache.tomee.catalina.OpenEJBValve.invoke(OpenEJBValve.java:45)\r\n\tat org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:472)\r\n\tat org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:171)\r\n\tat org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:99)\r\n\tat org.apache.catalina.valves.AccessLogValve.invoke(AccessLogValve.java:936)\r\n\tat org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:118)\r\n\tat org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:407)\r\n\tat org.apache.coyote.http11.AbstractHttp11Processor.process(AbstractHttp11Processor.java:1004)\r\n\tat org.apache.coyote.AbstractProtocol$AbstractConnectionHandler.process(AbstractProtocol.java:589)\r\n\tat org.apache.tomcat.util.net.JIoEndpoint$SocketProcessor.run(JIoEndpoint.java:312)\r\n\tat java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1145)\r\n\tat java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:615)\r\n\tat java.lang.Thread.run(Thread.java:744)\r\nCaused by: org.apache.myfaces.view.facelets.el.ContextAwareELException: javax.el.ELException: javax.ejb.EJBException: The bean encountered a non-application exception; nested exception is: \n\tjava.lang.NullPointerException: Tracee local Remote EJB example: Triggered exception with passed parameters \u00270\u0027 and \u00270\u0027\r\n\tat org.apache.myfaces.view.facelets.el.ContextAwareTagMethodExpression.invoke(ContextAwareTagMethodExpression.java:108)\r\n\tat org.apache.myfaces.application.ActionListenerImpl.processAction(ActionListenerImpl.java:68)\r\n\tat javax.faces.component.UICommand.broadcast(UICommand.java:120)\r\n\tat javax.faces.component.UIViewRoot._broadcastAll(UIViewRoot.java:1028)\r\n\tat javax.faces.component.UIViewRoot.broadcastEvents(UIViewRoot.java:286)\r\n\tat javax.faces.component.UIViewRoot._process(UIViewRoot.java:1375)\r\n\tat javax.faces.component.UIViewRoot.processApplication(UIViewRoot.java:752)\r\n\tat org.apache.myfaces.lifecycle.InvokeApplicationExecutor.execute(InvokeApplicationExecutor.java:38)\r\n\tat org.apache.myfaces.lifecycle.LifecycleImpl.executePhase(LifecycleImpl.java:170)\r\n\tat org.apache.myfaces.lifecycle.LifecycleImpl.execute(LifecycleImpl.java:117)\r\n\tat javax.faces.webapp.FacesServlet.service(FacesServlet.java:197)\r\n\t... 24 more\r\nCaused by: javax.el.ELException: javax.ejb.EJBException: The bean encountered a non-application exception; nested exception is: \n\tjava.lang.NullPointerException: Tracee local Remote EJB example: Triggered exception with passed parameters \u00270\u0027 and \u00270\u0027\r\n\tat org.apache.el.parser.AstValue.invoke(AstValue.java:291)\r\n\tat org.apache.el.MethodExpressionImpl.invoke(MethodExpressionImpl.java:274)\r\n\tat org.apache.myfaces.view.facelets.el.ContextAwareTagMethodExpression.invoke(ContextAwareTagMethodExpression.java:96)\r\n\t... 34 more\r\nCaused by: javax.ejb.EJBException: The bean encountered a non-application exception; nested exception is: \n\tjava.lang.NullPointerException: Tracee local Remote EJB example: Triggered exception with passed parameters \u00270\u0027 and \u00270\u0027\r\n\tat org.apache.openejb.core.ivm.BaseEjbProxyHandler.convertException(BaseEjbProxyHandler.java:363)\r\n\tat org.apache.openejb.core.ivm.BaseEjbProxyHandler.invoke(BaseEjbProxyHandler.java:283)\r\n\tat com.sun.proxy.$Proxy110.error(Unknown Source)\r\n\tat io.tracee.examples.webapp.TestWebappEjbController.triggerEjbRemoteError(TestWebappEjbController.java:50)\r\n\tat sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\r\n\tat sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:57)\r\n\tat sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\r\n\tat java.lang.reflect.Method.invoke(Method.java:606)\r\n\tat org.apache.el.parser.AstValue.invoke(AstValue.java:278)\r\n\t... 36 more\r\nCaused by: java.lang.NullPointerException: Tracee local Remote EJB example: Triggered exception with passed parameters \u00270\u0027 and \u00270\u0027\r\n\tat io.tracee.examples.ejb.TestEjbImpl.watchdogError_aroundBody0(TestEjbImpl.java:38)\r\n\tat io.tracee.examples.ejb.TestEjbImpl$AjcClosure1.run(TestEjbImpl.java:1)\r\n\tat org.aspectj.runtime.reflect.JoinPointImpl.proceed(JoinPointImpl.java:149)\r\n\tat io.tracee.contextlogger.watchdog.WatchdogAspect.guard(WatchdogAspect.java:58)\r\n\tat io.tracee.examples.ejb.TestEjbImpl.watchdogError(TestEjbImpl.java:38)\r\n\tat io.tracee.examples.ejb.TestEjbImpl.error(TestEjbImpl.java:33)\r\n\tat sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\r\n\tat sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:57)\r\n\tat sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\r\n\tat java.lang.reflect.Method.invoke(Method.java:606)\r\n\tat org.apache.openejb.core.interceptor.ReflectionInvocationContext$Invocation.invoke(ReflectionInvocationContext.java:181)\r\n\tat org.apache.openejb.core.interceptor.ReflectionInvocationContext.proceed(ReflectionInvocationContext.java:163)\r\n\tat io.tracee.contextlogger.jms.TraceeEjbErrorContextLoggingInterceptor.intercept(TraceeEjbErrorContextLoggingInterceptor.java:32)\r\n\tat sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\r\n\tat sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:57)\r\n\tat sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\r\n\tat java.lang.reflect.Method.invoke(Method.java:606)\r\n\tat org.apache.openejb.core.interceptor.ReflectionInvocationContext$Invocation.invoke(ReflectionInvocationContext.java:181)\r\n\tat org.apache.openejb.core.interceptor.ReflectionInvocationContext.proceed(ReflectionInvocationContext.java:163)\r\n\tat org.apache.openejb.cdi.CdiInterceptor.invoke(CdiInterceptor.java:126)\r\n\tat org.apache.openejb.cdi.CdiInterceptor.access$000(CdiInterceptor.java:42)\r\n\tat org.apache.openejb.cdi.CdiInterceptor$1.call(CdiInterceptor.java:63)\r\n\tat org.apache.openejb.cdi.CdiInterceptor.aroundInvoke(CdiInterceptor.java:69)\r\n\tat sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\r\n\tat sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:57)\r\n\tat sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\r\n\tat java.lang.reflect.Method.invoke(Method.java:606)\r\n\tat org.apache.openejb.core.interceptor.ReflectionInvocationContext$Invocation.invoke(ReflectionInvocationContext.java:181)\r\n\tat org.apache.openejb.core.interceptor.ReflectionInvocationContext.proceed(ReflectionInvocationContext.java:163)\r\n\tat org.apache.openejb.monitoring.StatsInterceptor.record(StatsInterceptor.java:176)\r\n\tat org.apache.openejb.monitoring.StatsInterceptor.invoke(StatsInterceptor.java:95)\r\n\tat sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\r\n\tat sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:57)\r\n\tat sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\r\n\tat java.lang.reflect.Method.invoke(Method.java:606)\r\n\tat org.apache.openejb.core.interceptor.ReflectionInvocationContext$Invocation.invoke(ReflectionInvocationContext.java:181)\r\n\tat org.apache.openejb.core.interceptor.ReflectionInvocationContext.proceed(ReflectionInvocationContext.java:163)\r\n\tat org.apache.openejb.core.interceptor.InterceptorStack.invoke(InterceptorStack.java:138)\r\n\tat org.apache.openejb.core.stateless.StatelessContainer._invoke(StatelessContainer.java:239)\r\n\tat org.apache.openejb.core.stateless.StatelessContainer.invoke(StatelessContainer.java:191)\r\n\tat org.apache.openejb.core.ivm.EjbObjectProxyHandler.synchronizedBusinessMethod(EjbObjectProxyHandler.java:246)\r\n\tat org.apache.openejb.core.ivm.EjbObjectProxyHandler.businessMethod(EjbObjectProxyHandler.java:241)\r\n\tat org.apache.openejb.core.ivm.EjbObjectProxyHandler._invoke(EjbObjectProxyHandler.java:83)\r\n\tat org.apache.openejb.core.ivm.BaseEjbProxyHandler.invoke(BaseEjbProxyHandler.java:279)\r\n\t... 43 more\r\n"
    	}
    }
    
## Maven artifacts
You have to add the following Maven dependencies to your project:

    <dependency>
        <groupId>io.tracee</groupId>
        <artifactId>tracee-api</artifactId>
        <version>${tracee.version}</version>
    </dependency>
    
    <!-- tracee log framework provider - depending on your project log configuration -->
    <dependency>
        <groupId>io.tracee.backend</groupId>
        <artifactId>tracee-slf4j</artifactId>
        <version>${tracee.version}</version>
    </dependency>

    <dependency>
        <groupId>io.tracee.contextlogger</groupId>
        <artifactId>tracee-context-logger-servlet</artifactId>
        <version>${tracee.version}</version>
    </dependency>

## How to use
In servlet 3.0 environments you only have to add the maven dependencies. The Filter will be configured via a web fragment and will be applied to all servlet requests.
If you use older servlet environment prior to version 3.0 then you will have to add the following configurations to your web.xml manually:

    <filter>
		<filter-name>traceeErrorLoggingFilter</filter-name>
		<filter-class>io.tracee.contextlogger.servlet.TraceeErrorLoggingFilter</filter-class>
	</filter>

	<filter-mapping>
		<filter-name>traceeErrorLoggingFilter</filter-name>
		<url-pattern>/*</url-pattern>
	</filter-mapping>

Keep in mind that if you use another servlet filter that gracefully catches uncaught exceptions, then you have to ensure that the context logger filter is executed after that filter in the servlet filter chain.
Otherwise no output will be generated by the context logger servlet filter.

## Remarks
Please consider the usage of tracee tracee-servlet filter. It will help you to aggregate log entries by adding request and session ids to your loggers MDC.
