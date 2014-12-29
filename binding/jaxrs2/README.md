> This document contains documentation for the `tracee-jaxrs2` module. Check the [TracEE main documentation](/README.md) to get started.

# tracee-jaxrs2

> This module can be used to add TracEE context propagation support to JAX-RS 2 webservices and JAX-RS 2 webservice clients.

Please add the following dependencies to enable TracEE JAX-WS support. For example in maven-style projects add to the pom.xml:

```xml
<dependencies>
    ...
    <dependency>
        <groupId>io.tracee</groupId>
        <artifactId>tracee-jaxrs2</artifactId>
        <version>RELEASE</version>
    </dependency>
    ...
</dependencies>
```



## Container : Implcit vs. Application config
If you don't use an Application config and you have the tracee-jaxrs2 artifact to your dependencies, all filters will be applied implicitely to your JAX-RS services. 

In case if you want to use an Application config class, you will have to add TracEE's TraceeContainerRequestFilter and TraceeContainerResponseFilter classes to your Application config class.

```java
@ApplicationPath("/basepath")
public class ApplicationConfig extends Application {

    public Set<Class<?>> getClasses() {
        return new HashSet<Class<?>>(Arrays.asList(TraceeContainerRequestFilter.class,TraceeContainerResponseFilter.class,...);
    }
}
```

## Using JAX-RS client
You have to add register the TraceeClientResponseFilter and TraceeClientRequestFilter classes during the Client creation to enable TracEE support for JAX-RS clients. 

```java
final Client client = ClientBuilder.newClient()
    .register(TraceeClientRequestFilter.class)
    .register(TraceeClientResponseFilter.class);
final Response response = client.target(ENDPOINT_URL).request().get();
```


