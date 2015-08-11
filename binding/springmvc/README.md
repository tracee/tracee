> This document contains documentation for the `tracee-springmvc` module. Check the [TracEE main documentation](/README.md) to get started.

# tracee-springmvc

This tracee module reads the incoming TracEE context for requests to your Spring MVC controllers and generates invocationIds if needed. It adds the TPIC header to your responses as well. - If you don't like to expose the TPIC in your responses (maybe you want to hide it from your users), you should take an individual TracEE configuration.
This module requires Spring 3.1.0 or above.

## Installation

Add `tracee-springmvc` to your application dependencies. That's all! - For example in maven-style projects add the following coordinates to the `pom.xml`:

```xml
<dependencies>
...
    <dependency>
	<groupId>io.tracee.binding</groupId>
	<artifactId>tracee-springmvc</artifactId>
    <version>RELEASE</version> <!-- You should specify a version instead -->
    </dependency>
...
</dependencies>
```

Then you're able to use our `TraceeInterceptor` in two ways:

With Spring JavaConfig:
```java
@Configuration
@EnableWebMvc
public class YourApplicationConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new io.tracee.binding.springmvc.TraceeInterceptor());
    }

}
```

With XML configuration:

```xml
<mvc:interceptors>
	<bean id="traceeInterceptor" class="io.tracee.binding.springmvc.TraceeInterceptor">
		<property name="profileName" value="default"/> <!-- The profile configuration is optional -->
	</bean>
</mvc:interceptors>
...
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