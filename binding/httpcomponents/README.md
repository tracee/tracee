> This document contains documentation for the `tracee-httpcomponents` module.  Check the [TracEE main documentation](/README.md) to get started.

# tracee-httpcomponents

TODO Wrapper for [apache http client 4+](http://hc.apache.org/httpcomponents-client-ga/)

This module contains two interceptors:
* __TraceeHttpRequestInterceptor__: Implements the `HttpRequestInterceptor` interface to add the tracee header to the request.
* __TraceeHttpResponseInterceptor__: Implements the `HttpResponseInterceptor` to extract the tracee header from the response.
 

## Installation

```xml
<dependencies>
...
    <dependency>
        <groupId>io.tracee.binding</groupId>
   		<artifactId>tracee-httpcomponents</artifactId>
        <version>${tracee.version}</version>
    </dependency>
...
</dependencies>
```

Then add simply two interceptors to your HttpClient of the httpcomponents module:

```java
DefaultHttpClient httpClient = new DefaultHttpClient();
httpClient.addRequestInterceptor(new TraceeHttpRequestInterceptor());
httpClient.addResponseInterceptor(new TraceeHttpResponseInterceptor());
```
