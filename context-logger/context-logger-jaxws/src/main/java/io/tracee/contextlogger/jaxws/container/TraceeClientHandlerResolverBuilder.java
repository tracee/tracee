package io.tracee.contextlogger.jaxws.container;

import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

/**
 * Fluent interface to build client handler resolver
 */
public interface TraceeClientHandlerResolverBuilder {

    /**
     * Finishes creation of handler resolver and returns a {@link HandlerResolver} instance.
     * @return a reference to a handler resolver (this in most cases)
     */
    HandlerResolver build();

    /**
     * Adds an existing {@link javax.xml.ws.handler.soap.SOAPHandler} instance to the HandlerResolver.
     * @param handler the instance to add
     * @return this builder instance
     */
    TraceeClientHandlerResolverBuilder add(final SOAPHandler<SOAPMessageContext> handler);

    /**
     * Creates a {@link javax.xml.ws.handler.soap.SOAPHandler} instance via reflection and adds it to the HandlerResolver.
     * @param handlerType the instance to add
     * @return this builder instance
     */
    TraceeClientHandlerResolverBuilder add(final Class<? extends SOAPHandler<SOAPMessageContext>> handlerType);


}
