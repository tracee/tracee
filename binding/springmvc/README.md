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

Then you're able to use our `TraceeInterceptor` and the `TraceeResponseBodyAdvice`. In your Java configuration please extend `WebMvcConfigurationSupport`:

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

When you're using asynchronous methods annotated with `@Async` you have to delegate the TPIC to the asynchronous invocation. Otherwise
the MDC of the async threads in the threadpool are not clean and log statements contains wrong TPIC metadata.
To enable the delegation add following bean post processors to your configuration:

```java
@Bean
public PreTpicAsyncBeanPostProcessor preTpicAsyncBeanPostProcessor(AsyncTaskExecutor executor) {
	return new PreTpicAsyncBeanPostProcessor(executor);
}

@Bean
public PostTpicAsyncBeanPostProcessor postTpicAsyncBeanPostProcessor(AsyncTaskExecutor executor) {
	return new PostTpicAsyncBeanPostProcessor(executor);
}
```

This enables TracEE to delegate the TPIC to your asynchronous method calls and delete the TPIC after the processing from the thread.
