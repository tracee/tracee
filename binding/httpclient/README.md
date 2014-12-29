> This document contains documentation for the tracee-httpclient module. Click [here](/README.md) to get an overview that TracEE is about.

# tracee-httpclient

TODO Wrapper for [apache http client 3](http://hc.apache.org/httpclient-3.x/)

## Installation

Add the `tracee-httpclient` module to your `pom.xml` dependencies:
```xml
<dependencies>
...
    <dependency>
        <groupId>io.tracee.inbound</groupId>
   		<artifactId>tracee-httpclient</artifactId>
        <version>RELEASE</version>
    </dependency>
...
</dependencies>
```

Then simply replace the pure instantiation of the HttpClient in your code with:
```java
final HttpClient client = new HttpClient();
```
with the `TraceeHttpClientDecorator`:
```java
final HttpClient client = TraceeHttpClientDecorator.wrap(new HttpClient());
```
