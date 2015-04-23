> Check the [TracEE main documentation](/README.md) to get started.

# The Bindings


| Framework                   | Client | Server / Container |
| ---------------------------:|:------:|:---------:|
| Servlet                     | - | Use [tracee-servlet](servlet/) as a servlet filter. |
| Spring MVC                  | - | Use [tracee-springmvc](springmvc/)'s `TraceeInterceptor`. |
| Spring Web (Rest-Clients)   | Use [tracee-springhttpclient](springhttpclient/)'s `TraceeClientHttpRequestInterceptor`. | - |
| Spring Web Services         | Add [tracee-springws](springws/)'s `TraceeClientInterceptor`. | Add [tracee-springws](springws/)'s `TraceeEndpointInterceptor`. |
| JAX-RS                      | Configure [tracee-httpclient](httpclient/) as Executor | Use [tracee-servlet](servlet/) as a servlet filter. |
| JAX-RS2                     | Configure [tracee-jaxrs2](jaxrs2/)'s `TraceeClientRequestFilter` and `TraceeClientResponseFilter` | Use [tracee-jaxrs2](jaxrs2/)'s `TraceeContainerRequestFilter` and `TraceeContainerResponseFilter`. |
| JAX-WS                      | Use [tracee-jaxws](jaxws/)'s `TraceeClientHandlerResolver` | Use [tracee-jaxws](jaxws/)'s `TraceeHandlerChain.xml` as `@HandlerChain`. |
| JMS                         | Producer: Use [tracee-jms](jms/)'s `TraceeMessageWriter.wrap` on your `MessageWriter` | MDB: Use [trace-jms](jms/)'s `TraceeMessageListener` as EJB interceptor. |
| ApacheHttpClient 3          | Use [tracee-httpclient](httpclient/)'s `TraceeHttpClientDecorator` | - |
| ApacheHttpClient 4          | Use [tracee-components](httpcomponents/)'s `TraceeHttpRequestInterceptor` and `TraceeHttpResponseInterceptor` | - |
| Apache CXF                  | Use [tracee-cxf](cxf/)'s `TraceeCxfFeature` | Use [tracee-cxf](cxf/)'s `TraceeCxfFeature` |
| Quartz Scheduler            | - | Use [tracee-quartz](quartz/)'s `TraceeJobListener` to generate context before the job starts |
