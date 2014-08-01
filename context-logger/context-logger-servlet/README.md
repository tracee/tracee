> This document contains documentation for the tcontext-logger-servlet module. Click [here](/README.md) to get an overview that TracEE is about.

# context-logger-servlet

The context-logger-servlet project provides a servlet filter that detects uncaught exception and outputs servlet request related context data like invoked url, request parameters, up to session or request attributes. 

## How to use
You have to add the following maven dependencies to your web applications pom.xml:

```xml
<dependencies>

    <!-- tracce dependencies -->
    <dependency>
      <groupId>io.tracee</groupId>
      <artifactId>tracee-api</artifactId>
      <version>0.3.0</version>
    </dependency>

    <!-- Tracee log provider, depending on your logging framework -->
    <dependency>
      <groupId>io.tracee.backend</groupId>
      <artifactId>tracee-slf4j</artifactId>
      <version>0.3.0</version>
    </dependency>
        
    <!-- Recommended, but optinal-->
    <dependency>
      <groupId>io.tracee.inbound</groupId>
      <artifactId>tracee-servlet</artifactId>
      <version>0.3.0</version>
    </dependency>
    <!-- END : tracce dependencies -->

    <!-- context logger dependencies-->    
    <dependency>
      <groupId>io.tracee.contextlogger</groupId>
      <artifactId>tracee-context-logger-impl</artifactId>
      <version>0.3.0</version>
    </dependency>

    <dependency>
      <groupId>io.tracee.contextlogger</groupId>
      <artifactId>tracee-context-logger-servlet</artifactId>
      <version>0.3.0</version>
    </dependency>
</dependencies>
```

Using TraceEEs *tracee-servlet* filter is optional but recommended, because it will handle the creation of request and session ids for you, which helps you aggregate log messages.

If you are using a Servlet 3.0 container you havn't do anything else. The servlet filter configuration will be added automatically via a web fragment.

You have to add the following to your web applications **web.xml** for servlet containers prior to version 3.0.

```xml
  <!-- context logger related configurations -->
	<filter>
		<filter-name>traceeErrorLoggingFilter</filter-name>
		<filter-class>io.tracee.contextlogger.servlet.TraceeErrorLoggingFilter</filter-class>
	</filter>

	<filter-mapping>
		<filter-name>traceeErrorLoggingFilter</filter-name>
		<url-pattern>/*</url-pattern>
	</filter-mapping>
	
	<!-- optional tracee-servlet related configurations -->
	<filter>
		<filter-name>traceeFilter</filter-name>
		<filter-class>io.tracee.servlet.TraceeFilter</filter-class>
	</filter>

	<filter-mapping>
		<filter-name>traceeFilter</filter-name>
		<url-pattern>/*</url-pattern>
	</filter-mapping>

	<listener>
		<listener-class>io.tracee.servlet.TraceeServletRequestListener</listener-class>
	</listener>
	<listener>
		<listener-class>io.tracee.servlet.TraceeSessionListener</listener-class>
	</listener>
```
