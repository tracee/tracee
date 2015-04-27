> This document contains documentation for the tracee-springws module. Check the [TracEE main documentation](/README.md) to get started.

# tracee-springws

This binding module handles outgoing and incoming SOAP-Requests handled by [Spring WS / Spring Web Services](http://projects.spring.io/spring-ws/). 

## Installation

Add the dependency to your classpath. If you're using Apache Maven, you could use this snipped and replace the version:

```xml
<deendencies>
...
	<dependency>
		<groupId>io.tracee.binding</groupId>
		<artifactId>tracee-springws</artifactId>
		<version>RELEASE</version> <!-- You should specify a version instead -->
	</dependency>
...
</dependencies>
```

### Client-Interceptor - Add context information to outgoing requests

Use the class `org.springframework.ws.client.core.support.WebServiceGatewaySupport` to implement your Spring-WS client. 
This helper template provides the ability to call client interceptors upon request.

```xml
<bean class="your.package.TheClassImplementingWebServiceGatewaySupport">
	<property name="interceptors">
		<list>
			<bean class="io.tracee.binding.springws.TraceeClientInterceptor"/>
		</list>
	</property>
	...
</bean>
```

For a java based configuration use:

```java
@Configuration
public MyApplicationConfig {

	public TheClassImplementingWebServiceGatewaySupport createClassImplementingWebServiceGateway() {
		TheClassImplementingWebServiceGatewaySupport gateway = new TheClassImplementingWebServiceGatewaySupport();
		gateway.setInterceptors(new ClientInterceptor[] {new TraceeClientInterceptor()});
		... // Add other required stuff to WebServiceGatewayTemplate
	}

...
}
```

### Server Interceptor - Extract context information from incoming requests

With a XML based configuration you add the `TraceeEndpointInterceptor` to all your soap services by attaching it to the `sws:interceptors` xml node. 
(Namespace: `http://www.springframework.org/schema/web-services`)

```xml
<sws:interceptors>
	<bean class="io.tracee.binding.springws.TraceeEndpointInterceptor" />
</sws:interceptors>
```

You could add the tracee interceptor with a java based configuration as well. (Offered by Spring-WS 2.2 and above).
After you enabled SpringWS with `@EnableWs` annotation on one of your application configurations, you've to extend the 
abstract class `WsConfigurerAdapter` to add the endpoint interceptor for all of your requests. 

```java
@Configuration
@EnableWs
public MyApplicationConfig extends WsConfigurerAdapter {

	@Override
	public void addInterceptors(List<EndpointInterceptor> interceptors) {
		interceptors.add(new TraceeEndpointInterceptor());
	}
...
}
```
