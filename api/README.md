> This document contains documentation for the `tracee-api` module. Check the [TracEE main documentation](/README.md) to get started.

# tracee-api

This module contains the public api for users (applications) and service provider implementations.

## Manipulating the context from your application code

TracEE is designed to keep your application and business code free from the aspect of propagation of contextual information.
But it is still valid if your business code emits context information that you want to follow within further processing.

_tracee-api_ contains a lightweight client api that allows you to attach parameters into the current invocation context.
Use the `TraceeBackend#put(String key, String value)`-method to add a context parameter.

The key shares a namespace with all MDC entries, so make sure it is unique in your business code and does not conflict
with other frameworks MDC entries. It might help to choose a common prefix for your applications context parameters.

The following example shows an EJB that uses a `TraceeBackend` to attach the context information of an _orderNumber_ to the current system
interaction.

```java
@Stateless
public class MyBusinessService {

  private final TraceeBackend backend = Tracee.getBackend();

  @EJB
  OrderService orderService;

  public void makeBigBusiness(Customer customer, Item ... items) {
		final int orderNumber = orderService.getNextOrderNumber();
		backend.set("MyBusiness.orderNumber", orderNumber);
		try {
		  final Order order = orderService.create(customer);
		  order.addItems(items);
		  orderService.processOrder();
		} finally {
		  backend.remove("MyBusiness.orderNumber");
		}
  }

}
```
Be aware, that you _really should_ remove your custom context parameters as soon as the become invalid.

> Please keep in mind that parameters in the TracEE-Context are meant for logging. Do not use it as a _hack_ to pass
> actual business parameters alongside your business method signatures.

## TracEE backends

TracEE already ships with a number of adapters for popular logging frameworks, namely:

* [slf4j](/backend/slf4j)
* [log4j](/backend/log4j)
* [log4j2](/backend/log4j2)
* [jboss-logging](/backend/jboss-logging)

If you have no logging backend present in a component, you can still use the [threadlocal-store](/backend/threadlocal-store) as
backend that supports propagation of parameters (but leaves out the logging part).

### Implementing custom backends

You can make your own backend by implementing the `io.tracee.spi.TraceeBackendProvider` interface
and create a _provider-configuration file_ `classpath:/META-INF/services/io.tracee.spi.TraceeContextProvider`
(see JDKs [ServiceLoader](http://docs.oracle.com/javase/7/docs/api/java/util/ServiceLoader.html)).

The module [tracee-core](/core) contains abstractions over MDC-like logging backends that may help you with your integration.
