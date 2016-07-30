> This document contains documentation for the `tracee-httpcomponents5` module.  Check the [TracEE main documentation](/README.md) to get started.

# tracee-httpcomponents5

Wrapper for [apache http client 5+](https://hc.apache.org/httpcomponents-client-5.0.x/)

This module contains two interceptors:
* __TraceeHttpRequestInterceptor__: Implements the `HttpRequestInterceptor` interface to add the tracee header to the request.
* __TraceeHttpResponseInterceptor__: Implements the `HttpResponseInterceptor` to extract the tracee header from the response.

## Installation

```xml
<dependencies>
...
    <dependency>
        <groupId>io.tracee.binding</groupId>
   		<artifactId>tracee-httpcomponents5</artifactId>
        <version>${tracee.version}</version>
    </dependency>
...
</dependencies>
```

Then add simply two interceptors to your HttpClient of the httpcomponents module:

```java
HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
httpClientBuilder.addInterceptorLast(new TraceeHttpRequestInterceptor());
httpClientBuilder.addInterceptorFirst(new TraceeHttpResponseInterceptor());
CloseableHttpClient httpClient = httpClientBuilder.build();
```
