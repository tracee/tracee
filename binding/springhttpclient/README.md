> This document contains documentation for the `tracee-servlet` module. Check the [TracEE main documentation](/README.md) to get started.

# tracee-springhttpclient

> This module contains all necessary classes to transmit the MDC within your Spring `RestTemplate`-Request and extract the MDC from server response.

Requires Spring-Web in version 3.1 or above.

 * __TraceeClientHttpRequestInterceptor__: An interceptor attachable to the Spring-Web `RestTemplate`.
 
## Installation
 
If you're already use `RestTemplate` for your REST remote calls I'm pretty sure that you've added a dependency to `spring-web`. To use TracEE you need the `tracee-springhttpclient` dependency as well.

For maven you've to add following dependency to your `pom.xml`:

```xml
...
<dependency>
	<groupId>io.tracee.binding</groupId>
    <artifactId>tracee-springhttpclient</artifactId>
    <version>${tracee.version}</version>
</dependency>
...
```

After that you've to add the `TraceeClientHttpRequestInterceptor` to your `RestTemplate`:

```java
...
final RestTemplate restTemplate = new RestTemplate();
restTemplate.setInterceptors(Arrays.<ClientHttpRequestInterceptor>asList(new TraceeClientHttpRequestInterceptor()));
restTemplate.getForObject(serverEndpoint, ...);
...
```
