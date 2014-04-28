# TracEE 0.2.0

[![Build Status](https://secure.travis-ci.org/holisticon/tracee.png)](https://travis-ci.org/holisticon/tracee)
[![Coverage Status](https://coveralls.io/repos/holisticon/tracee/badge.png)](https://coveralls.io/r/holisticon/tracee)

TL;DR, watch our [5 minutes slide deck](http://holisticon.github.io/tracee/docs/slides/)

> __TracEE makes it easy to track the invocation context throughout your distributed JavaEE-Application logs. It does not force
> you to pass around collaboration-ids but instead uses side-channels on the underlying protocols. It encapsulates the aspect
> of invocation context propagation and makes it visible in your log files.__

You may already aggregate all your application logs at a single place (using graylog, logstash+kibana or others) but it is still
complicated to gather relevant log entries that belong to a certain interaction with your system.

*TracEE* is an integration framework that eases this kind of interaction monitoring of JavaEE applications by passing contextual information
through your whole system and makes them visible in your logs. Therefore if contains adapters or interceptors for the most popular JavaEE technologies:

* servlet 2.5+
* jax-ws
* jax-rs2
* jms

This project is still in experimental stage and the api may change during further development.

# Getting started

## The invocation context

So what is this all about?

An example:

- A user opens your JavaEE enterprisy hotel booking page in a browser
- Your servlet container renders a calendar and room selection
- The users selects a date and a tasty breakfast and clicks on _book_
- Your servlet starts to chat with your full-blown EE-Server
- _Awesome stuff happens_, EJBs, SOAP- and REST-Services are called, Databases are read and written
- Finally the user sees a confirmation page and receives a booking confirmation via email

This is the happy path. But what if something goes wrong? Lets name it: Exception Stack-Trace in the log files.
The stack-trace often shows a very narrow scope of what went wrong, namely the current thread.

An __invocation context__ is a temporal or logical boundary in which a system invocation happens.
Since each system invocation may cause further system interactions, every subsequent system invocation belongs to the
same context as the invoking interaction.

An example for an invocation context in a servlet based application would be a HTTP-Request. Every system interaction
that is directly or indirectly caused by a browser asking a servlet-container lies within the context of its HTTP-Request.

Another example would be the invocation context of a servlet session in which every request within this session belongs
the session invocation context.

It can be a great benefit to make those invocation contexts visible in the log files. So if a failure happens, you could
lookup the invocation context in which it occurred and see pretty clearly what happened _in this context_ before the
server gave up with an error. And it does not end here.

Even without errors, the invocation context information empower you to measure the reaction time
of your application at the level of every single service.

So how do you implement this in an JavaEE-Application? An obvious way would be to pass an invocation context identifier
around as parameter of every of your business interfaces (EBJ, SOAP, REST, whatsoever) and write it explicitly into each
log statement. It should also be obvious that this is a dumb idea because it pollutes all of our business interfaces with
unnecessary dependencies. We can do better.

## The propagated Mapped Diagnosis Context

The [Mapped Diagnostic Context (MDC)](http://logback.qos.ch/manual/mdc.html) is a logging concept that allows printing of contextual information in log messages
without explicitly passing them them to each log statement. A MDC is bound to its executing thread (in fact they are backed by thread locals).

Invocations of JavaEE components are seldom fully processed within a single thread - they might even not be fully processed
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

TracEE acts as a gap closer for different types of MDC gaps and enables you to carry your contextual information through
your whole application and beyond. You can even configure TracEE to map third-party correlation and transaction ids to
your logging context without polluting your business logic.


# Integrating TracEE into your application

The steps to get TracEE up and running pretty much depends on your application scenario. The most common use case would be to
propagate context information from a servlet container based frontend to an ejb based backend.

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

## Modules

TracEE is built highly modular. The modules you need depends on your application and the underlying frameworks and containers.
The following table describes all available TracEE-modules and their usage scenarios.

| Module                                | Usage |
|--------------------------------------:|:-----:|
| __core modules__                      |       |
| [tracee-api](api/)                    | Contains Objects to interact with the TracEE context. Use it to write contextual information from your application into the TracEE context.
| [tracee-core](core/)                  | Common utility classes, configuration system and serialization helpers. You will not need to use this module directly.
| __connector modules__                 |
| [tracee-httpclient](httpclient/)      | Adapter for `org.apache.httpcomponents:httpclient`-library. Use it to traceefy your JAX-RS or raw http clients.
| [tracee-jaxrs2](jaxrs2)               | Interceptors for JAX-RS2. Use it to traceefy your JAX-RS2 endpoints and clients.
| [tracee-jaxws](jaxws)                 | HandlerChains for JAX-WS endpoints and clients.
| [tracee-jms](jms)                     | EJB-Interceptors and MessageProducers that allow you to pass around your TracEE context with JMS.
| [tracee-servlet](servlet)        		| Listeners and filters for the servlet spec. Use it to traceefy JAX-RS, Vaadin, JSP or any other servlet based web application.
| [tracee-springmvc](springmvc)         | Provides a HandlerInterceptor for Spring MVC. Use it to traceefy Spring MVC or Spring WebFlow applications.
| __backends__                          |
| [tracee-slf4j](slf4j)                 | Backend implementation for containers using slf4j. You may use this for Logback-Backend or on top of a java util logging containers like tomcat6 together with slf4j-jcl.
| [tracee-log4j](log4j)                 | Backend implementation for containers using log4j for logging.
| [tracee-log4j2](log4j2)               | Backend implementation for containers using log4j2 for logging.
| [tracee-jboss-logging](jboss-logging) | Backend implementation for containers using jboss-logging like JBoss EAP5/AS6.
| [threadlocal-store](threadlocal-store)| Backend implementation for containers that use no common logging framework. Use it in scenarios where you just want to propagate the context information.

All TracEE modules are (hopefully) OSGI compliant.

## Maven artifacts

Tracee is released to maven central via Sonatype OSS Repository Hosting.

Just add a maven/gradle/sbt dependency. For example _tracee-servlet_:
```xml
<dependencies>
    <dependency>
        <groupId>de.holisticon.util.tracee</groupId>
        <artifactId>tracee-servlet</artifactId>
        <version>0.2.0</version>
    </dependency>
</dependencies>
```

... or use the very latest SNAPSHOT

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
        <version>0.3.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```


# More

## Shipped context information
TracEE creates the following context identfiers on the fly if not configured otherwise:

    * it generates a pseudo-unique request id (configurable length)
    * it generates a session id base on the servlet session id. Since the servlet session id is a secure


## Performance considerations

TracEE is designed with performance in mind. It does not introduce global synchronization and cleans up the MDC after
each invocation lifecycle. A real benchmark is pending...

These automatically generated context ids (like request- and session-identifiers) are configurable in length and allow you
to choose a tradeoff between the chance of _uniqueness_ in time and data overhead depending on your load scenario.

## Security considerations

Since you may pass sensitive user information within your TracEE-Context, it is important to cancel the propagation at
trust boundaries (like HTTP-Responses to users or third-party Web-Services). TracEE offers filter configuration mechanisms
that allow you to selectively decide at which point in your application you want to pass around what contextual information.

## Classloader considerations

You can bundle TracEE with your application or install it as global library to your container.


# Contributing to TracEE

We welcome any kind of suggestions and pull requests. Please notice that TracEE is an integration framework and we will not support
application specific features. We will rather try to enhance our api and empower you to tailor TracEE to your needs.

## Building and developing TracEE

TracEE is built using Maven (at least version 3.0.4).
A simple import of the pom in your IDE should get you up and running. To build TracEE on the commandline, just run `mvn clean install`

## Requirements

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
