> This document contains documentation for the tracee context-logger-jaxws module. Click [here](/README.md) to get an overview that TracEE jaxws context logger is about.

# context-logger-jaxws

> __The TracEE JAX-WS context logger provides contextual information if a jax-ws related error ocurred at your system.

Therefore the context-logger-jaxws module provides SOAPHandlers that detect exceptions and log contextual infomation to your log system.



# Getting started

## Maven Dependencies

You need to add the following Dependencies to your projects pom.xml:

```xml
    <dependencies>
        
    <!-- Binds the TracEE api -->
    <dependency>
        <groupId>io.tracee</groupId>
        <artifactId>tracee-api</artifactId>
        <version>0.4.0-SNAPSHOT</version>
    </dependency>

    <!-- Log Backend depending on your logging configuration-->
    <dependency>
        <groupId>io.tracee.backend</groupId>
        <artifactId>tracee-slf4j</artifactId>
        <version>0.4.0-SNAPSHOT</version>
    </dependency>

    <!-- Optional - used for context data propagation -->
    <dependency>
        <groupId>io.tracee</groupId>
        <artifactId>tracee-jaxws</artifactId>
        <version>0.4.0-SNAPSHOT</version>
    </dependency>
        
    <!-- Binds context logging -->
    <dependency>
        <groupId>io.tracee.contextlogger</groupId>
        <artifactId>tracee-context-logger-jaxws</artifactId>
        <version>0.4.0-SNAPSHOT</version>
    </dependency>
        

```

## Server Side
You can use the context logger by annotating your jax-ws webservice with the @HandlerChain annotation.
```java
@Stateless
@WebService(serviceName = "TraceeJaxWsTestService", portName = "TraceeJaxWsTestPort",
        targetNamespace = "https://github.com/holisticon/tracee/examples/jaxws/service/wsdl")
@HandlerChain(file = "/traceeHandlerChain.xml")
public class TraceeJaxWsTestService implements TraceeJaxWsTestWS {
    ...
}
```

Therefore you have to add the referenced xml file to your classpath (i.e. /src/main/resources). The file must have the following content:
```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes" ?>
<javaee:handler-chains xmlns:javaee="http://java.sun.com/xml/ns/javaee">
	<javaee:handler-chain>
	    <!-- enables the context logging handler -->
		<javaee:handler>
			<javaee:handler-class>io.tracee.contextlogger.jaxws.container.TraceeServerErrorLoggingHandler</javaee:handler-class>
		</javaee:handler>
		
		<!-- enables context data propagation if you want to pass context information using the TracEE main project -->
        <javaee:handler>
            <javaee:handler-class>io.tracee.jaxws.container.TraceeServerHandler</javaee:handler-class>
        </javaee:handler>
	</javaee:handler-chain>
</javaee:handler-chains>
```


## Client Side Integration
First you have to create the client stub classes for the webservice wsdl. 
Then you are able to bind the error context logger by using the services handler resolver mechanism:

```java

    // Example 1 : Use solely the context logger service handler
    final TraceeJaxWsTestService testWebservice = new TraceeJaxWsTestService(
        new URL("http://localhost:8080/yourTestService/webservices/YourTestService?wsdl"));
    testWebservice.setHandlerResolver(TraceeClientHandlerResolver.createSimpleHandlerResolver());
    final YourTestWS ws = testWebservice.getPort(YourTestWS.class);

    // Example 2 : Use error context logger next to tracee context propagation (or with any other service handler)
    final TraceeJaxWsTestService testWebservice = new TraceeJaxWsTestService(
        new URL("http://localhost:8080/yourTestService/webservices/YourTestService?wsdl"));
    testWebservice.setHandlerResolver(TraceeClientHandlerResolver.buildHandlerResolver().add(TraceeClientHandler.class).build());
    final YourTestWS ws = testWebservice.getPort(YourTestWS.class);
```

