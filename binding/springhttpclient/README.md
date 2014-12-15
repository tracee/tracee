> This document contains documentation for the tracee-servlet module. Click [here](/README.md) to get an overview that TracEE is about.

# tracee-springhttpclient

This module contains all necessary classes to transmit the MDC within your Spring `RestTemplate`-Request and extract the MDC from server response.

Requires Spring-Web in version 3.1 or above.

 * __TraceeClientHttpRequestInterceptor__: An interceptor attachable to the Spring-Web `RestTemplate`.
 
## Installation
 
If you're already use `RestTemplate` for your REST remote calls I'm pretty sure that you've added a dependency to `spring-web`. To use TracEE you need the `tracee-springhttpclient` dependency as well.

For maven you've to add following dependency to your `pom.xml`:

```xml
...
<dependency>
	<groupId>io.tracee.inbound</groupId>
    <artifactId>tracee-springhttpclient</artifactId>
    <version>RELEASE</version> <!-- You should specify a version instead -->
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
