> This document contains documentation for the tracee context-logger-watchdog module. Click [here](/README.md) to get an overview that TracEE is about.

# context-logger-watchdog

> The TracEE context logger watchdog subproject allows you to gather contextual data of method invocations by using spring-AOP or AspectJ in case of an exception is thrown during the method invocation. 

This can easily achieved by adding the Watchdog annotation to a method of a class or by annotating a class to use the watchdog for all public methods of that class. 

    // Example 1 : Set watchdog annotation at class level to use watchdog for all public methods of the class 
    @Watchdog
    public class ExampleClass() {
    
        public void exampleMethod1() {
            ...
        }
        
        // ...
        
        public void exampleMethodN() {
            ...
        }
    
    }
    
    // Example 2 : Set Watchdog annotations explicitly on methods of the class
    public class ExampleClass() {
    
        @Watchdog
        public void exampleMethod1() {
            ...
        }
        
        // ...
        
        @Watchdog
        private void exampleMethodN() {
            ...
        }
    
    }
    
## Example output
Depending on the selected context logger profile the output consists of invoked method name, deserialized invocation parameters, deserialized target instance and thrown exception.

    Apr 25, 2014 3:43:02 PM de.holisticon.util.tracee.backend.slf4j.Slf4jTraceeLogger error
    Schwerwiegend: TRACEE WATCHDOG ERROR CONTEXT LISTENER :{"common":{"timestamp":"Apr 25, 2014 3:43:02 PM","stage":"DEV","system-name":"localhost","thread-name":"http-bio-8080-exec-4","thread-id":52},"tracee":{},"watchdog":{"aspectj.proceedingJoinPoint":{"class":"de.holisticon.util.tracee.examples.ejb.TestEjbImpl","method":"watchdogError","parameters":["5","6"],"deserialized-instance":"TestEjbImpl@7698b062[]"}},"throwable":{"message":"Tracee local Remote EJB example: Triggered exception with passed parameters \u00275\u0027 and \u00276\u0027","stacktrace":"java.lang.NullPointerException: Tracee local Remote EJB example: Triggered exception with passed parameters \u00275\u0027 and \u00276\u0027\r\n\tat de.holisticon.util.tracee.examples.ejb.TestEjbImpl.watchdogError_aroundBody0(TestEjbImpl.java:44)\r\n\tat de.holisticon.util.tracee.examples.ejb.TestEjbImpl$AjcClosure1.run(TestEjbImpl.java:1)\r\n\tat org.aspectj.runtime.reflect.JoinPointImpl.proceed(JoinPointImpl.java:149)\r\n\tat de.holisticon.util.tracee.contextlogger.watchdog.WatchdogAspect.guard(WatchdogAspect.java:58)\r\n\tat de.holisticon.util.tracee.examples.ejb.TestEjbImpl.watchdogError(TestEjbImpl.java:44)\r\n\tat de.holisticon.util.tracee.examples.ejb.TestEjbImpl.error(TestEjbImpl.java:39)\r\n\tat sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\r\n\tat sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:57)\r\n\tat sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\r\n\tat java.lang.reflect.Method.invoke(Method.java:606)\r\n\tat org.apache.openejb.core.interceptor.ReflectionInvocationContext$Invocation.invoke(ReflectionInvocationContext.java:181)\r\n\tat org.apache.openejb.core.interceptor.ReflectionInvocationContext.proceed(ReflectionInvocationContext.java:163)\r\n\tat de.holisticon.util.tracee.contextlogger.jms.TraceeEjbErrorContextLoggingInterceptor.intercept(TraceeEjbErrorContextLoggingInterceptor.java:32)\r\n\tat sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\r\n\tat sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:57)\r\n\tat sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\r\n\tat java.lang.reflect.Method.invoke(Method.java:606)\r\n\tat org.apache.openejb.core.interceptor.ReflectionInvocationContext$Invocation.invoke(ReflectionInvocationContext.java:181)\r\n\tat org.apache.openejb.core.interceptor.ReflectionInvocationContext.proceed(ReflectionInvocationContext.java:163)\r\n\tat org.apache.openejb.cdi.CdiInterceptor.invoke(CdiInterceptor.java:126)\r\n\tat org.apache.openejb.cdi.CdiInterceptor.access$000(CdiInterceptor.java:42)\r\n\tat org.apache.openejb.cdi.CdiInterceptor$1.call(CdiInterceptor.java:63)\r\n\tat org.apache.openejb.cdi.CdiInterceptor.aroundInvoke(CdiInterceptor.java:69)\r\n\tat sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\r\n\tat sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:57)\r\n\tat sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\r\n\tat java.lang.reflect.Method.invoke(Method.java:606)\r\n\tat org.apache.openejb.core.interceptor.ReflectionInvocationContext$Invocation.invoke(ReflectionInvocationContext.java:181)\r\n\tat org.apache.openejb.core.interceptor.ReflectionInvocationContext.proceed(ReflectionInvocationContext.java:163)\r\n\tat org.apache.openejb.monitoring.StatsInterceptor.record(StatsInterceptor.java:176)\r\n\tat org.apache.openejb.monitoring.StatsInterceptor.invoke(StatsInterceptor.java:95)\r\n\tat sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\r\n\tat sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:57)\r\n\tat sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\r\n\tat java.lang.reflect.Method.invoke(Method.java:606)\r\n\tat org.apache.openejb.core.interceptor.ReflectionInvocationContext$Invocation.invoke(ReflectionInvocationContext.java:181)\r\n\tat org.apache.openejb.core.interceptor.ReflectionInvocationContext.proceed(ReflectionInvocationContext.java:163)\r\n\tat org.apache.openejb.core.interceptor.InterceptorStack.invoke(InterceptorStack.java:138)\r\n\tat org.apache.openejb.core.stateless.StatelessContainer._invoke(StatelessContainer.java:239)\r\n\tat org.apache.openejb.core.stateless.StatelessContainer.invoke(StatelessContainer.java:191)\r\n\tat org.apache.openejb.core.ivm.EjbObjectProxyHandler.synchronizedBusinessMethod(EjbObjectProxyHandler.java:246)\r\n\tat org.apache.openejb.core.ivm.EjbObjectProxyHandler.businessMethod(EjbObjectProxyHandler.java:241)\r\n\tat org.apache.openejb.core.ivm.EjbObjectProxyHandler._invoke(EjbObjectProxyHandler.java:83)\r\n\tat org.apache.openejb.core.ivm.BaseEjbProxyHandler.invoke(BaseEjbProxyHandler.java:279)\r\n\tat com.sun.proxy.$Proxy98.error(Unknown Source)\r\n\tat de.holisticon.util.tracee.examples.webapp.TestWebappEjbController.triggerEjbRemoteError(TestWebappEjbController.java:50)\r\n\tat sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\r\n\tat sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:57)\r\n\tat sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\r\n\tat java.lang.reflect.Method.invoke(Method.java:606)\r\n\tat org.apache.el.parser.AstValue.invoke(AstValue.java:278)\r\n\tat org.apache.el.MethodExpressionImpl.invoke(MethodExpressionImpl.java:274)\r\n\tat org.apache.myfaces.view.facelets.el.ContextAwareTagMethodExpression.invoke(ContextAwareTagMethodExpression.java:96)\r\n\tat org.apache.myfaces.application.ActionListenerImpl.processAction(ActionListenerImpl.java:68)\r\n\tat javax.faces.component.UICommand.broadcast(UICommand.java:120)\r\n\tat javax.faces.component.UIViewRoot._broadcastAll(UIViewRoot.java:1028)\r\n\tat javax.faces.component.UIViewRoot.broadcastEvents(UIViewRoot.java:286)\r\n\tat javax.faces.component.UIViewRoot._process(UIViewRoot.java:1375)\r\n\tat javax.faces.component.UIViewRoot.processApplication(UIViewRoot.java:752)\r\n\tat org.apache.myfaces.lifecycle.InvokeApplicationExecutor.execute(InvokeApplicationExecutor.java:38)\r\n\tat org.apache.myfaces.lifecycle.LifecycleImpl.executePhase(LifecycleImpl.java:170)\r\n\tat org.apache.myfaces.lifecycle.LifecycleImpl.execute(LifecycleImpl.java:117)\r\n\tat javax.faces.webapp.FacesServlet.service(FacesServlet.java:197)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:305)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:210)\r\n\tat de.holisticon.util.tracee.servlet.TraceeFilter.doFilterHttp(TraceeFilter.java:59)\r\n\tat de.holisticon.util.tracee.servlet.TraceeFilter.doFilter(TraceeFilter.java:45)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:243)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:210)\r\n\tat de.holisticon.util.tracee.contextlogger.servlet.TraceeErrorLoggingFilter.doFilter(TraceeErrorLoggingFilter.java:31)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:243)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:210)\r\n\tat org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:222)\r\n\tat org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:123)\r\n\tat org.apache.tomee.catalina.OpenEJBValve.invoke(OpenEJBValve.java:45)\r\n\tat org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:472)\r\n\tat org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:171)\r\n\tat org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:99)\r\n\tat org.apache.catalina.valves.AccessLogValve.invoke(AccessLogValve.java:936)\r\n\tat org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:118)\r\n\tat org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:407)\r\n\tat org.apache.coyote.http11.AbstractHttp11Processor.process(AbstractHttp11Processor.java:1004)\r\n\tat org.apache.coyote.AbstractProtocol$AbstractConnectionHandler.process(AbstractProtocol.java:589)\r\n\tat org.apache.tomcat.util.net.JIoEndpoint$SocketProcessor.run(JIoEndpoint.java:312)\r\n\tat java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1145)\r\n\tat java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:615)\r\n\tat java.lang.Thread.run(Thread.java:744)\r\n"}}


    
## Configuring the Watchdog annotation
The behavior and output of the Watchdog annotation can be configured by defining the following annotation attributes:

| Attribute name | Type | Description | Default |
|---------------:|:----:|:-----------:|: ----- :|
| id | String | the id will be part of the contextual information output | empty String - will be ignored |
| suppressThrowsExceptions | boolean | Defines if exceptions defined in the throws block of the method signature should be ignored | false |
| isActive | boolean | Defines if the watchdog is active | true |


## Configuration of AOP frameworks
Currently the AspectJ and spring-AOP frameworks are supported by this project.

### Using AspectJ
You have to start your application server / application by using the following java options / agent to enable AspectJ runtime weaving.
   -Daj.weaving.verbose=true -javaagent:<path to your aspectj library home>\aspectjweaver.jar


### Using Spring-AOP
Using spring-AOP, aspects will only be applied at method invocations on spring proxy instances. Therefore the following spring-MVC example will not work.

    @Controller
    public class Example {
    
        @RequestMapping(value = "/token", method = RequestMethod.GET)
        public void exampleMethodCalled (Model model) {
        
            // watchdog of exampleMethod will not work because method is not called via a spring proxy
            this.exampleMethod(1,"a");
            
            // watchdog of exampleMethod will work because method is called via a spring proxy
            ((Example) AopContext.currentProxy()).exampleMethod(1,"a");
        
        }
                
        @Watchdog
        public void exampleMethod (int a, String b) {
            // ...
        }
    
    }

#### Configuration of Spring-AOP

You must add the following to your dispatcher servlet configuration:

    <beans xmlns="http://www.springframework.org/schema/beans"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xmlns:context="http://www.springframework.org/schema/context"
           xmlns:mvc="http://www.springframework.org/schema/mvc"
           xmlns:aop="http://www.springframework.org/schema/aop"
           xsi:schemaLocation="http://www.springframework.org/schema/beans
            http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
            http://www.springframework.org/schema/context
            http://www.springframework.org/schema/context/spring-context.xsd
            http://www.springframework.org/schema/mvc
            http://www.springframework.org/schema/mvc/spring-mvc-3.0.xsd
            http://www.springframework.org/schema/aop
            http://www.springframework.org/schema/aop/spring-aop-4.0.xsd"
    >
    
        <!-- Enable AspectJ style of Spring AOP -->
        <aop:aspectj-autoproxy/>
        
        <!-- ...  -->
    
        <!-- Configure Aspect Beans, without this Aspects advices wont execute -->
        <bean id="watchdog" class="io.tracee.contextlogger.watchdog.WatchdogAspect"/>
        
    </beans>
