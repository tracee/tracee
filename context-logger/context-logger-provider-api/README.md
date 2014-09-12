> This document contains documentation for the context-logger-provider-api module. Click [here](/README.md) to get an overview that TracEE is about.

# context-logger-provider-api

> The TracEE context-logger-servlet project offers all interfaces and annotations needed to define custom context data provider. 


## Example: Cookie context data provider 

    package io.tracee.contextlogger.contextprovider.servlet;
    
    import io.tracee.contextlogger.api.TraceeContextProvider;
    import io.tracee.contextlogger.api.TraceeContextProviderMethod;
    import io.tracee.contextlogger.api.WrappedContextData;
    import io.tracee.contextlogger.contextprovider.Order;
    import io.tracee.contextlogger.profile.ProfilePropertyNames;
    
    import javax.servlet.http.Cookie;
    
    /**
     * Context provider for ServletCookieContextProvider.
     * Created by Tobias Gindler, holisticon AG on 24.01.14.
     */
    @TraceeContextProvider(displayName = "servletCookies", order = Order.SERVLET)
    public final class ServletCookieContextProvider implements WrappedContextData<Cookie> {
    
        private Cookie cookie;
    
        public ServletCookieContextProvider() {
        }
    
        public ServletCookieContextProvider(Cookie cookie) {
            this.cookie = cookie;
        }
    
        @Override
        public void setContextData(Object instance) throws ClassCastException {
            this.cookie = (Cookie) instance;
        }
    
        @Override
        public Class<Cookie> getWrappedType() {
            return Cookie.class;
        }
    
        @SuppressWarnings("unused")
        @TraceeContextProviderMethod(
                displayName = "name",
                propertyName = ProfilePropertyNames.COOKIE_NAME,
                order = 10)
        public String getName() {
            if (cookie != null) {
                return cookie.getName();
            }
            return null;
        }
        
        @SuppressWarnings("unused")
        @TraceeContextProviderMethod(
                displayName = "value",
                propertyName = ProfilePropertyNames.COOKIE_VALUE,
                order = 20)
        public String getValue() {
            if (cookie != null) {
                return cookie.getValue();
            }
            return null;
        }
    
        // ...
    }

## How to create a custom data provider
A custom data provider class must have the following characteristics

+ The class must beu annotated with @TraceeContextProvider annotation at class
+ The class must implement the WrappedContextData<YOUR_WRAPPED_CLASS>
+ The class should offer methods that provide the context data. Those methods should be annotated with @TraceeContextProviderMethod method and must be public and return a value of type String

