> This document contains documentation for the `tracee-cxf` module.  Check the [TracEE main documentation](/README.md) to get started.

# tracee-cxf

This module is used for CXF implementations on client and/or on server side. It's tested with CXF 2.7.18mvn and should work with all versions above.

## Installation

Add to your CXF dependency for REST or SOAP our additional TracEE part. For example in maven-style projects add to the `pom.xml`:

```xml
<dependencies>
...
    <dependency>
        <groupId>io.tracee.binding</groupId>
        <artifactId>tracee-cxf</artifactId>
        <version>${tracee.version}</version>
    </dependency>
...
</dependencies>
```

Then you're able to use our `TraceeCxfFeature`:

```java
final ClientProxyFactoryBean factoryBean = new ClientProxyFactoryBean();
factoryBean.getFeatures().add(new TraceeCxfFeature());
...
```

## Background

When the client sends a request to the server and he sends an response back, both kinds of message (request and response) have an outgoing and an incoming part. We can count four phases:
* Clients sends the request: RequestOut
* Server receives the request: RequestIn
* Server sends back the response: ResponseOut
* Client receives the response: ResponseIn

A CXF `Feature` gives us the ability to add only two regular and two fault interceptor chains. Due this limitation we have to add two incoming and two outgoing interceptors at the same time. To distinguish between request and response we use the CXF helper Method `MessageUtils.isRequestor(msg)`. This method is called at every invocation and helps us to use one single Feature for the client and server side.
