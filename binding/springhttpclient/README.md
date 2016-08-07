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

### Use TraceeSpringWebConfiguration

Add `TraceeSpringWebConfiguration` to your configuration. All RestTemplate beans that you create via XML- or JavaConfig will automatically receive an `TraceeClientHttpRequestInterceptor`.

```java
package myapp;
import org.springframework.web.client.RestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import io.tracee.binding.springhttpclient.config.TraceeSpringWebConfiguration;

@Configuration
@Import(TraceeSpringWebConfiguration.class)
public class MyConfiguration {

    public RestTemplate myRestTemplate() {
        return new RestTemplate();
    }
}

```

### Inject your RestTemplate

Go to your Spring Java configuration file and add:

```java
package myapp;
import org.springframework.web.client.RestTemplate;

public class MyConfiguration {
    @Bean
    public RestTemplate restTemplate() {
        final RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(Collections.singletonList(traceeClientHttpRequestInterceptor()));
        return restTemplate;
    }
}
```

Now you can inject the RestTemplate with the Tracee interceptor to all necessary spring components.

### Manual usage

If you're creating a new version of the `RestTemplate` in your code, simple add the `TraceeClientHttpRequestInterceptor` by adding it as interceptor:

```java
package myapp;
public class MyService {
    private final RestTemplate restTemplate = new RestTemplate();
    {
    restTemplate.setInterceptors(Collections.singletonList(traceeClientHttpRequestInterceptor()));
    }
}

```
