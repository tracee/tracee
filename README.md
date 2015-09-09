# TracEE 1.1.0

[![Gitter](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/tracee/tracee?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.tracee/tracee-parent/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.tracee/tracee-parent)
[![Tasks in progress](https://badge.waffle.io/tracee/tracee.png?label=in+progress&title=In+progress)](https://waffle.io/tracee/tracee)
[![Tasks ready to merge](https://badge.waffle.io/tracee/tracee.png?label=ready+to+merge&title=Ready+to+merge)](https://waffle.io/tracee/tracee)
[![Build Status](https://api.travis-ci.org/tracee/tracee.svg)](https://travis-ci.org/tracee/tracee)
[![Coverage Status](https://img.shields.io/coveralls/tracee/tracee.svg)](https://coveralls.io/r/tracee/tracee)
[![Coverity Scan](https://scan.coverity.com/projects/3882/badge.svg)](https://scan.coverity.com/projects/3882)
[![BSD 3-Clause License](http://img.shields.io/badge/license-BSD-brightgreen.svg)](https://raw.githubusercontent.com/tracee/tracee/master/LICENSE)

__There is now a comprehensive documentation site at [http://www.tracee.io](http://www.tracee.io)!__

> __TracEE makes it easy to track the invocation context throughout your distributed JavaEE and Spring applications logs. It does not force you to manually pass around collaboration-ids but instead uses side-channels on the underlying protocols and therefore encapsulates the aspect of invocation context propagation.__

You may already aggregate all your application logs at a single place (using graylog, logstash+kibana or others) but it is still
complicated to gather relevant log entries that belong to a certain interaction with your system.

*TracEE* is an integration framework that eases this kind of interaction monitoring of JavaEE applications by passing contextual information
through your whole system and makes them visible in your logs. Therefore it contains adapters or interceptors for the most popular JavaEE technologies:

* servlet 2.5+
* jax-ws
* jax-rs2
* jms

Also supported are these common and widespread frameworks:

* Spring MVC
* Spring Web (RestClients)
* Spring AMQP (RabbitMQ)
* Spring Web Services (SpringWS)
* Apache HttpClient 3 / 4
* Apache CXF
* Quartz Scheduler

# Getting started

## The Invocation Context

So what is this all about?

An example:

- A user opens your JavaEE enterprisey hotel booking page in a browser
- Your servlet container renders a calendar and room selection
- The user selects a date and a tasty breakfast and clicks on _book_
- Your servlet starts to chat with your full-blown EE-Server
- _Awesome stuff happens_, EJBs, SOAP- and REST-Services are called, databases are read and written
- Finally the user sees a confirmation page and receives a booking confirmation via email

This is the happy path. But what if something goes wrong? Lets name it: Exception stack trace in the log files.
The stack trace often shows a very narrow scope of what went wrong, namely the current thread.

An __invocation context__ is a temporal or logical boundary in which a system invocation happens.
Since each system invocation may cause further system interactions, every subsequent system invocation logically belongs to the
same context as the invoking interaction.

An example for an invocation context in a servlet based application would be a HTTP-Request. Every system interaction
that is directly or indirectly caused by a browser asking a servlet-container lies within the context of its HTTP-Request.

Another example would be the context of a servlet session in which every request within this session belongs to
the session invocation context.

It can be a great benefit to make those invocation contexts visible in the log files. So if a failure happens, you could
lookup the invocation context in which it occurred and see pretty clearly what happened _in this context_ before the
server gave up with an error. And it does not end here.

Even without errors, the invocation context information empowers you to measure the reaction time
of your application at the level of every single service call.

So how do you implement this in an JavaEE-Application? An obvious way would be to pass an invocation context identifier
around as a parameter of every of your business interfaces (EJB, SOAP, REST, whatever) and write it explicitly into each
log statement. It should also be obvious that this is a dumb idea because it pollutes all of our business interfaces with
unnecessary artificial parameters just for the benefit of making the invocation context explicit. But we can do better!

## The Propagated Invocation Context (PIC)

The [Mapped Diagnostic Context (MDC)](http://logback.qos.ch/manual/mdc.html) is a logging concept that allows printing of contextual information in log messages
without explicitly passing them them to each log statement. A MDC is bound to its executing thread (in fact they are backed by thread locals).

Invocations of JavaEE components are seldom fully processed within a single thread - they might not even be fully processed
on the same JVM. So each time an invocation escapes its original executing thread, the MDC is lost in the nested processing.
We may call these context boundaries MDC gaps. There are different kinds of those gaps:

* Remote calls
    * Remote EJB calls
    * Invoking and accepting arbitrary HTTP requests (Servlet, HttpClient)
    * Invoking and accepting remote web-service calls (JAX-WS, Apache CXF)
    * Invoking and accepting remote rest-services calls (JAX-RS, RestClients / Spring MVC, Apache CXF)
* Asynchronous dispatch
    * Async processing of a servlet request (Servlet3 Async)
    * Async EJB calls
    * JMS messaging
    * Spring AMQP (RabbitMQ)
    * Spring Framework asynchronous methods

TracEE acts as a gap closer for different types of MDC gaps and boosts the concept of the MDC by enabling you to carry your contextual information through
your whole application and beyond. You may easily configure TracEE to map arbitrary third-party correlation and transaction ids to
your logging context without polluting your business logic.

## On the wire

So how does a Propagated Invocation Context look like on the wire? This naturally depends on the transport.
The most prominent transports are native java serialization formats, HTTP and SOAP.

Using Java serialization the Propagated Invocation Context is simply encoded as a Java map of strings.

On HTTP-Transports, like JAX-RS, Servlets, JSPs, or whatever, the invocation context is encoded as a custom HTTP-Header 
__TPIC__. Key and value are URL-Encoded and concatenated with `=` and `,`.
 
```
GET / HTTP/1.1
TPIC: in+Request=yes
User-Agent: Jakarta Commons-HttpClient/3.1
Host: localhost:2000
```
 
In the SOAP-world the invocation context is, regardless of the underlying transport mechanism, encoded as a special header
in the SOAP-Request-Envelope and SOAP-Response-Envelope.
```xml
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
  <soap:Header>
    <TPIC xmlns="http://tracee.io/tpic/1.0">
      <entry key="TPIC.invocationId">ABCDEFG</entry>
    </TPIC>
  </soap:Header>
...
</soap:Envelope>
```

# Integrating TracEE into your application

TracEE is highly modular. What modules actually you need for integration depends on your application, its underlying frameworks and containers.
The following table describes all available TracEE-modules and their usage scenarios.

| Module                                              | Usage |
|----------------------------------------------------:|:-----:|
| __core modules__                                    |       |
| [tracee-api](api/)                                  | API to interact with the TracEE context from within your business code. Use it to write contextual information from your application into the TracEE context.
| [tracee-core](core/)                                | Common utility classes, configuration system and transport serialization mechanisms. *You won't need this module as a direct dependency.* |
| [tracee-bom](bom/) | *BOM - Bill of Material* for maven projects | 
| __binding modules__                     | *These dependencies are needed due compile time* |
| [tracee-httpcomponents](binding/httpcomponents/)    | Adapter for `org.apache.httpcomponents:httpclient`-library (also known as HttpClient 4.x). Use it to make your JAX-RS or raw http clients propagate and receive invocation contexts.
| [tracee-httpclient](binding/httpclient/)            | Adapter for `commons-httpclient`-library (also known as HttpClient 3.x). Use it to make your JAX-RS or raw http clients propagate and receive invocation contexts.
| [tracee-jaxrs2](binding/jaxrs2)                     | Interceptors for JAX-RS2. Use it to traceefy your JAX-RS2 endpoints and clients.
| [tracee-jaxws](binding/jaxws)                       | HandlerChains for JAX-WS endpoints and clients.
| [tracee-jms](binding/jms)                           | EJB-Interceptors and MessageProducers that allow you to pass around your TracEE context with JMS.
| [tracee-servlet](binding/servlet)        		      | Listeners and filters for the servlet spec. Use it to traceefy JAX-RS, Vaadin, JSP or any other servlet based web application.
| [tracee-springmvc](binding/springmvc)               | Provides a HandlerInterceptor for Spring MVC. Use it to traceefy Spring MVC or Spring WebFlow applications.
| [tracee-springhttpclient](binding/springhttpclient) | ClientHttpRequestInterceptor for Springs `RestTemplate`. Simply add an `TraceeClientHttpRequestInterceptor` to traceefy your requests.
| [tracee-springrabbitmq](binding/springrabbitmq)     | Provides a `MessagePropertiesConverter` implementation for  Springs `RabbitTemplate`.
| [tracee-springws](binding/springws)                 | `TraceeClientInterceptor` and `TraceeEndpointInterceptor` to handle SOAP with Spring Web Services.
| [tracee-cxf](binding/cxf)                           | To transfer context information with CXF add the `TraceeCxfFeature` to your Client oder Server.
| [tracee-quartz](binding/quartz)                     | To generate context information before a job starts use `TraceeJobListener`.

Look into our [Bindings](binding/)-Page to get a more detailed binding overview.

All TracEE modules are (hopefully) OSGI compliant.

## Maven artifacts

Tracee is released to maven central via Sonatype OSS Repository Hosting. Just add a maven/gradle/sbt dependency as usual. If you are a crazy one, you could use the very latest SNAPSHOT as well by adding the sonatype snapshot repository:

```xml
<repositories>
  <repository>
    <id>sonatype-nexus-snapshots</id>
    <name>Sonatype Nexus Snapshots</name>
    <url>https://oss.sonatype.org/content/repositories/snapshots/</url>
  </repository>
</repositories>
```

When you use several TracEE modules you should consider using the [BOM](bom/) in your maven project to ease dependency management.

# More

## Shipped context information
TracEE creates the following context identfiers on the fly if not configured otherwise:

    * it generates a pseudo-unique __invocation id__ if it does not exist yet.
    * it generates a __session hash__ based on the servlet session id. Since the servlet session id is a secure item that should not 
    be passed around unnecessarily, we use a hash of it.

## Performance considerations

TracEE is designed with performance in mind. It does not introduce global synchronization and cleans up the MDC after
each invocation lifecycle. _A real benchmark is pending..._

The automatically generated context ids (like invocation- and session-identifiers) are configurable in length and allow you
to choose a tradeoff between the chance of _uniqueness_ in time and data overhead depending on your usage scenario.

## Security considerations

Since you may pass sensitive user information within your TracEE-Context, it is important to cancel the propagation at
trust boundaries (like HTTP-Responses to users or third-party Web-Services). TracEE offers [filter configuration mechanisms](/core/README.md#filter-configuration))
that allow you to selectively decide at which point in your application you want to pass around what contextual identifiers.

## Classloader considerations

You can bundle TracEE with your application or install it as global library to your container.

You just need to understand, that the invocation context identifiers are stored in the MDC of your logging framework (and therefore also share its lifecycle). 
So when you host multiple applications within a container with classloader isolation, an inter-application invocation context 
(like remote EJBs) can only be propagated when they share the same MDC (MDCs in the end just little more than a thread-local per class-loader).
Therefore it is highly recommended to use your containers logging framework because it resides in the container classloader and can be
accessed by all your applications.

# Contributing to TracEE

We welcome any kind of suggestions and pull requests. Please notice that TracEE is an integration framework and we will not support
application specific features. We will rather try find a generic solutions and enhance our api to empower you to tailor TracEE to your needs.

## Building and developing TracEE

TracEE is built using Maven (at least version 3.1.0).
A simple import of the pom in your IDE should get you up and running. To build TracEE on the commandline, just run `mvn clean install`

## Requirements

The likelihood of a pull request being accepted rises with the following properties:

- You have used a feature branch, squashed to a single commit and rebased on the master branch.
- You have included a test that demonstrates the functionality added or fixed.
- You adhered to the [code conventions](http://www.oracle.com/technetwork/java/javase/documentation/codeconvtoc-136057.html).

## Contributions

- (2013) Daniel Wegener (Holisticon AG)
- (2013) Tobias Gindler (Holisticon AG)
- (2014) Sven Bunge (Holisticon AG)

## Sponsoring

This project is sponsored and supported by [Holisticon AG](https://www.holisticon.de/)

# License

This project is released under the revised [BSD License](LICENSE).
