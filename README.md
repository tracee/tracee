# tracee   0.1-SNAPSHOT

[![Build Status](https://secure.travis-ci.org/holisticon/tracee.png)](https://travis-ci.org/holisticon/tracee)

## Introduction

Debugging distributed enterprise applications is difficult.

...

You may already aggregate all your machine logs in a single logging database (using logstash, elasticsearch or others) but it is still
complicated to find all log entries that belong to a certain interaction with the system.

*TracEE* is a framework that tries to ease this kind of interaction diagnosis by passing contextual information through your system and
makes them visible in your logs. Therefore if contains adapters or interceptors for the most popular JavaEE technologies:

* servlet 2.5
* jax-ws
* jax-rs
* jms

The following logging frameworks are supported as backends

* slf4j
* log4j
* jboss-logging

This project is sill in early experimental alpha stage and the whole api may change during further development.

## Getting started
*Just some lines of description in prose and code that gets the user up and running with this library.*


The following context information are visible out of the box:
- for each new incoming



## Contribution
- (2013) Daniel Wegener (Holisticon AG)
- (2013) Tobias Gindler (Holisticon AG)

### Setup a development environment
tracee is built using Maven (at least version 3.0.4).
A simple import of the pom in your IDE should get you up and running:

``mvn clean install``

### Requirements
The likelihood of a pull request being used rises with the following properties:

- You have used a feature branch.
- You have included a test that demonstrates the functionality added or fixed.
- You adhered to the [code conventions](http://www.oracle.com/technetwork/java/javase/documentation/codeconvtoc-136057.html).

## TODO
- Add connectors to error logging servlet filter and jaxws service handler to send error jsons to external systems.
- Documentation


# The logging MDC
The Mapped Diagnosis Context is a logging concept that allows printing of contextual information in log messages
without explicitly naming them on each log statement. A MDC is bound to its executing thread (in fact they are backed by thread locals).

Invocation of JavaEE components are seldom fully processed within a single thread - they might even not be fully processed
on the same JVM. Each time an invocation escapes its original executing thread, the MDC is lost in the nested processing.
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


## Shipped context information
TracEE creates the following context identfiers on the fly if not configured otherwise:

    * TODO: check if MDCIntertingServletFilter does the job.
    * TraceeServlet generates a pseudo-unique request id (configurable length)
    * sessionid ... continue here


## Performance considerations

TracEE is designed with performance in mind. It does not introduce global synchronization and cleans up the MDC after
an invocation context lifecycle. Benchmarking is pending...

The automatically generated context ids (like request- and session-identifiers) are configurable in length and allow you
to choose a tradeoff between _uniqueness_ in time and data overhead depending on your load scenario.

## Security considerations

Since you may pass sensitive user information within your tracEE-Context, it is important to cancel the propagation at
trust boundaries (like HTTP-Responses to users or third-party Web-Services). TracEE offers filter configuration mechanisms
that allow you to selectively decide at which point in your application you want to pass around what contextual information.


## Use cases

| Framework | Client | Container |
| ---------:|:------:|:---------:|
| Servlet   | x  | Use [tracee-servlet](servlet) as a servlet filter. |
| JAX-RS    | Configure [tracee-httpclient](httpclient) as Executor | Use [tracee-servlet](servlet) as a servlet filter. |
| JAX-RS2   | Configure [tracee-jaxrs2](jaxrs2)'s `TraceeClientRequestFilter` and `TraceeContainerResponseFilter` | Use [tracee-jaxrs2](jaxrs2)'s `TraceeContainerRequestFilter` and `TraceeContainerResponseFilter`. |
| JAX-WS    | Use [tracee-jaxws](jaxws)'s `TraceeClientHandlerResolver` | Use [tracee-jaxws](jaxws)'s `TraceeHandlerChain.xml` as `@HandlerChain`. |
| JMS       | Producer: Use [tracee-jms](jms)'s `TraceeMessageWriter.wrap` on your `MessageWriter` | MDB: Use [trace-jms](jms)'s `TraceeMessageListener` as EJB interceptor. |
| ApacheHttpClient | Use [tracee-httpclient](httpclient)'s `TraceeHttpRequestInterceptor` and `TraceeHttpResponseInterceptor` | - |

## Slides
[to be completed](docs/slides/index.html)

## Sponsoring
This project is sponsored and supported by [holisticon AG](http://www.holisticon.de/)

## License
This project is released under the revised [BSD License](LICENSE).