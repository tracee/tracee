> This document contains documentation for the `tracee-springcontext` module. Check the [TracEE main documentation](/README.md) to get started.

# tracee-springcontext

This module provides spring configurations that provide a `TraceeBackend` as spring bean.



## Delegate to asynchronous methods

When you're using asynchronous methods annotated with `@Async` you have to delegate the TPIC to the asynchronous invocation. Otherwise the MDC of the async threads in the threadpool are not clean and log statements contains wrong TPIC metadata.
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
