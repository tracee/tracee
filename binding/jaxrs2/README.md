> This document contains documentation for the `tracee-jaxrs2` module. Check the [TracEE main documentation](/README.md) to get started.

# tracee-jaxrs2

> This module can be used to add TracEE context propagation support to JAX-RS 2 webservices and JAX-RS 2 webservice clients.

Please add the following dependencies to enable TracEE JAX-WS support. For example in maven-style projects add to the pom.xml:

```xml
<dependencies>
    ...
    <dependency>
        <groupId>io.tracee.binding</groupId>
        <artifactId>tracee-jaxrs2</artifactId>
        <version>RELEASE</version>
    </dependency>
    ...
</dependencies>
```



## Container: Implicit vs. application config

If you don't use an explicit application config and have the `tracee-jaxrs2` module on your classpath, 
all filters will be automatically applied to your JAX-RS services.

If you want to use an explicit `Application` class, you have to `TraceeContainerRequestFilter`
and `TraceeContainerResponseFilter` as provider classes.

```java
@ApplicationPath("/basepath")
public class ApplicationConfig extends Application {

    public Set<Class<?>> getClasses() {
        return new HashSet<Class<?>>(Arrays.asList(io.tracee.jaxrs.container.TraceeContainerRequestFilter.class,
        io.tracee.jaxrs.container.TraceeContainerResponseFilter.class, ...);
    }
}
```

## Using JAX-RS 2 client
You have to register the `TraceeClientResponseFilter` and `TraceeClientRequestFilter` as provider classes during 
client creation to enable TracEE support for JAX-RS2 clients. 

```java
final Client client = ClientBuilder.newClient()
    .register(io.tracee.jaxrs.client.TraceeClientRequestFilter.class)
    .register(io.tracee.jaxrs.client.TraceeClientResponseFilter.class);
final Response response = client.target(ENDPOINT_URL).request().get();
```


