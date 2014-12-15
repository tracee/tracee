> This document contains documentation for the tracee-httpclient module. Click [here](/README.md) to get an overview that TracEE is about.

# tracee-httpclient

TODO Wrapper for [apache http client 3](http://hc.apache.org/httpclient-3.x/)

## Installation

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

In your code replace the the pure instantiation of the HttpClient
```java
final HttpClient client = new HttpClient();
```
with the `TraceeHttpClientDecorator`:
```java
final HttpClient client = TraceeHttpClientDecorator.wrap(new HttpClient());
```
