> This document contains documentation for the `tracee-springmvc` module. Check the [TracEE main documentation](/README.md) to get started.

# tracee-springmvc

This tracee module reads the incoming TracEE context for requests to your Spring MVC controllers and generates invocationIds if needed. It adds the TPIC header to your responses as well. - If you don't like to expose the TPIC in your responses (maybe you want to hide it from your users), you should take an individual TracEE configuration.
This module requires Spring 4.1.0 or above.

 * __TraceeInterceptor__: Parses TracEE-Context from the request and writes the received context immediatly to the response. When the request handled it tries to replace the written context (when `HttpResponse` is not commited) and clears the TracEE context.
 * __TraceeResponseBodyAdvice__: Large bodies or specific view mapper like JSON views encoded with jackson closes the response immediatly. This advice writes the TracEE context before the view starts with the encoding. 

## Installation

Add `tracee-springmvc` to your application dependencies. That's all! - For example in maven-style projects add the following coordinates to the `pom.xml`:

```xml
<dependencies>
...
    <dependency>
	<groupId>io.tracee.binding</groupId>
	<artifactId>tracee-springmvc</artifactId>
    <version>${tracee.version}</version>
    </dependency>
...
</dependencies>
```

There are several mechanisms to configure _tracee-springmvc_. The following sections demonstrate these mechanisms, ordered by preference.

### Use TraceeSpringMvcConfiguration

This mechanism is especially preferred when you use springs `@EnableWebMvc` (auto-provided `DelegatingWebMvcConfiguration`) on your application configuration.
All you need to do is to import `TraceeSpringMvcConfiguration` to your configuration. All MVC controllers will receive a `TraceeInterceptor` and a `TraceeResponseBodyAdvice`.


```java
package myapp;
import org.springframework.web.client.RestTemplate;
import org.springframework.context.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Bean;
import io.tracee.binding.springhttpclient.config.TraceeSpringWebConfiguration;


@EnableWebMvc
@Configuration
@Import({TraceeContextConfiguration.class, TraceeSpringMvcConfiguration.class})
public class MyConfiguration {

    @Autowired
    MyMvcController myMvcController();

}
```

### Use TraceeSpringMvcConfiguration

In some scenarios your application configuration extends Springs `WebMvcConfigurationSupport`.

The following example demonstrates how you can plug _tracee-springmvc_ into your custom `WebMvcConfigurationSupport`:

```java
@Configuration
public class TraceeInterceptorSpringConfig extends DelegatingWebMvcConfiguration {

	@Bean
	public TraceeInterceptor traceeInterceptor() {
		return new TraceeInterceptor();
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
}
```

To define the tracee profile specify it at the bean definition:
```java
@Bean
public TraceeInterceptor traceeInterceptor() {
	final TraceeInterceptor interceptor = new TraceeInterceptor();
	interceptor.setProfileName("default");
	return interceptor;
}
```

## Delegate to asynchronous methods

Please refer to [TracEE spring context bindings](../springcontext/README.md)
