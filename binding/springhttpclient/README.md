> This document contains documentation for the `tracee-springhttpclient` module. Check the [TracEE main documentation](/README.md) to get started.

# tracee-springhttpclient

> This module contains all necessary classes to transmit the MDC within your Spring `RestTemplate`-Request and extract the MDC from server response.

Requires Spring-Web in version 3.1 or above.

 * __TraceeClientHttpRequestInterceptor__: An interceptor attachable to the Spring-Web `RestTemplate`.
 
## Installation
 
If you're already use `RestTemplate` for your REST remote calls, it is pretty certain that you've already added `spring-web` as a dependency. To use TracEE with your `RestTemplate` you need the `tracee-springhttpclient` dependency as well.

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

Import `TraceeSpringWebConfiguration` to your configuration. All RestTemplate beans that are managed by spring (XML- or JavaConfig) will automatically receive an `TraceeClientHttpRequestInterceptor`. This mechanism uses a `BeanPostProcessor`.

```java
package myapp;
import org.springframework.web.client.RestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Bean;
import io.tracee.binding.springhttpclient.config.TraceeSpringWebConfiguration;

@Configuration
@Import(TraceeSpringWebConfiguration.class)
public class MyConfiguration {

    @Bean
    public RestTemplate myRestTemplate() {
        return new RestTemplate();
    }
}
```

### Inject your RestTemplate

Alternative you can add the `TraceeClientHttpRequestInterceptor` manually to your `RestTemplate` bean.

Go to your Spring Java configuration file and add:

```java
package myapp;
import org.springframework.web.client.RestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import io.tracee.binding.springhttpclient.TraceeClientHttpRequestInterceptor;

@Configuration
public class MyConfiguration {
    @Bean
    public RestTemplate restTemplate() {
        final RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(Collections.singletonList(new TraceeClientHttpRequestInterceptor()));
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
