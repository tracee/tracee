# TracEE 0.1.0

[![Build Status](https://secure.travis-ci.org/holisticon/tracee.png)](https://travis-ci.org/holisticon/tracee)
[![Coverage Status](https://coveralls.io/repos/holisticon/tracee/badge.png)](https://coveralls.io/r/holisticon/tracee)

TL;DR, watch our [5 minutes slide deck](http://holisticon.github.io/tracee/docs/slides/)

You may already aggregate all your application logs at a single place (using graylog, logstash+kibana or others) but it is still
complicated to gather relevant log entries that belong to a certain interaction with your system.

*TracEE* is an integration framework that eases this kind of interaction monitoring of JavaEE applications by passing contextual information
through your whole system and makes them visible in your logs. Therefore if contains adapters or interceptors for the most popular JavaEE technologies:

* servlet 2.5
* jax-ws
* jax-rs2
* jms

The following logging frameworks are supported as _backends_

* slf4j
* log4j
* jboss-logging

This project is sill in early experimental alpha stage and the whole api may change during further development.

## Getting started

The steps to get TracEE up and running pretty much depends on your application scenario. The most common use case would be to
propagate context information from a servlet container based frontend to an ejb based backend.

### Maven artifacts

Just add a maven/gradle/sbt dependency. For example _tracee-servlet_:
```xml
<dependencies>
    <dependency>
        <groupId>de.holisticon.util.tracee</groupId>
        <artifactId>tracee-servlet</artifactId>
        <version>0.1.0</version>
    </dependency>
</dependencies>
```

... or use the latest SNAPSHOT

```xml
<repositories>
    <repository>
        <id>sonatype-nexus-snapshots</id>
        <name>Sonatype Nexus Snapshots</name>
        <url>https://oss.sonatype.org/content/repositories/snapshots/</url>
    </repository>
</repositories>
<dependencies>
    <dependency>
        <groupId>de.holisticon.util.tracee</groupId>
        <artifactId>tracee-servlet</artifactId>
        <version>0.2.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```

All tracEE artifacts are (hopefully) OSGI compliant.

# Integration manual

## The logging MDC

The Mapped Diagnosis Context (MDC) is a logging concept that allows printing of contextual information in log messages
without explicitly passing them them to each log statement. A MDC is bound to its executing thread (in fact they are backed by thread locals).

Invocation of JavaEE components are seldom fully processed within a single thread - they might even not be fully processed
on the same JVM. So each time an invocation escapes its original executing thread, the MDC is lost in the nested processing.
We call these context boundaries MDC gaps. There are different kinds of those gaps:

* Remote calls
    * Remote EJB calls
    * Invoking and accepting arbitrary HTTP requests (Servlet, HttpClient)
    * Invoking and accepting remote web-service calls (JAX-WS)
    * Invoking and accepting remote rest-services calls(JAX-RS)
* Asynchronous
    * Async processing of a servlet request (Servlet3 Async)
    * Async EJB calls
    * JMS messaging

TracEE acts as a gap closer for different types of MDC gaps and allows you to carry your contextual information through
your whole application and beyond. You can even configure TracEE to map third-party correlation and transaction ids to
your logging context without polluting your business logic.

## Integrating TracEE into your application

## Shipped context information
TracEE creates the following context identfiers on the fly if not configured otherwise:

    * it generates a pseudo-unique request id (configurable length)
    * it generates a session id base on the servlet session id. Since the servlet session id is a secure


## Performance considerations

TracEE is designed with performance in mind. It does not introduce global synchronization and cleans up the MDC after
an invocation context lifecycle. A real benchmark is pending...

These automatically generated context ids (like request- and session-identifiers) are configurable in length and allow you
to choose a tradeoff between the chance of _uniqueness_ in time and data overhead depending on your load scenario.

## Security considerations

Since you may pass sensitive user information within your TracEE-Context, it is important to cancel the propagation at
trust boundaries (like HTTP-Responses to users or third-party Web-Services). TracEE offers filter configuration mechanisms
that allow you to selectively decide at which point in your application you want to pass around what contextual information.

## Integration scenarios

| Framework  | Client | Container |
| ----------:|:------:|:---------:|
| Servlet    | - | Use [tracee-servlet](servlet) as a servlet filter. |
| Spring MVC | - | Use [tracee-springmvc](springmvc)'s `TraceeInterceptor`. |
| JAX-RS     | Configure [tracee-httpclient](httpclient) as Executor | Use [tracee-servlet](servlet) as a servlet filter. |
| JAX-RS2    | Configure [tracee-jaxrs2](jaxrs2)'s `TraceeClientRequestFilter` and `TraceeClientResponseFilter` | Use [tracee-jaxrs2](jaxrs2)'s `TraceeContainerRequestFilter` and `TraceeContainerResponseFilter`. |
| JAX-WS     | Use [tracee-jaxws](jaxws)'s `TraceeClientHandlerResolver` | Use [tracee-jaxws](jaxws)'s `TraceeHandlerChain.xml` as `@HandlerChain`. |
| JMS        | Producer: Use [tracee-jms](jms)'s `TraceeMessageWriter.wrap` on your `MessageWriter` | MDB: Use [trace-jms](jms)'s `TraceeMessageListener` as EJB interceptor. |
| ApacheHttpClient | Use [tracee-httpclient](httpclient)'s `TraceeHttpRequestInterceptor` and `TraceeHttpResponseInterceptor` | - |
| EJB3 remote  | - | - |

### Classloader considerations

You can bundle TracEE with your application or install it as global library to your container.


# Contributing

We welcome any kind of suggestions and pull requests. Please notice that TracEE is an integration framework and we will not support
application specific features. We will rather try to enhance our api and empower you to tailor TracEE to your needs.

## Building TracEE

TracEE is built using Maven (at least version 3.0.4).
A simple import of the pom in your IDE should get you up and running. To build TracEE on the commandline, just run `mvn clean install`

### Requirements

The likelihood of a pull request being used rises with the following properties:

- You have used a feature branch.
- You have included a test that demonstrates the functionality added or fixed.
- You adhered to the [code conventions](http://www.oracle.com/technetwork/java/javase/documentation/codeconvtoc-136057.html).

## Contributions

- (2013) Daniel Wegener (Holisticon AG)
- (2013) Tobias Gindler (Holisticon AG)
- (2014) Sven Bunge (Holisticon AG)

## Sponsoring

This project is sponsored and supported by [holisticon AG](http://www.holisticon.de/)

# License

This project is released under the revised [BSD License](LICENSE).
