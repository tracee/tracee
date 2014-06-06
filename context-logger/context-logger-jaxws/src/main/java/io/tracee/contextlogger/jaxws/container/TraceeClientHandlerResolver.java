package io.tracee.contextlogger.jaxws.container;

import io.tracee.Tracee;
import io.tracee.TraceeLogger;

import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.PortInfo;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.util.ArrayList;
import java.util.List;

/**
 * A client handler resolver that provides the {@link io.tracee.contextlogger.jaxws.container.TraceeClientErrorLoggingHandler}.
 * It allows you to add other handlers via a fluent api.
 */
public class TraceeClientHandlerResolver implements HandlerResolver, TraceeClientHandlerResolverBuilder {

    private final TraceeLogger logger = Tracee.getBackend().getLoggerFactory().getLogger(
            TraceeClientHandlerResolver.class);

    private final List<Handler> handlerList = new ArrayList<Handler>();

    public TraceeClientHandlerResolver() {
        //handlerList.add(new ClientHandler();
    }

    @Override
    public final List<Handler> getHandlerChain(final PortInfo portInfo) {
        return handlerList;
    }


    @Override
    public HandlerResolver build() {
        this.handlerList.add(new TraceeClientErrorLoggingHandler());
        return this;
    }

    @Override
    public TraceeClientHandlerResolverBuilder add(SOAPHandler<SOAPMessageContext> handler) {

        if (handler != null) {
            this.handlerList.add(handler);
        }

        return this;
    }

    @Override
    public TraceeClientHandlerResolverBuilder add(Class<? extends SOAPHandler<SOAPMessageContext>> handlerType) {

        if (handlerType != null) {
            try {
                SOAPHandler<SOAPMessageContext> handler = handlerType.newInstance();
                this.handlerList.add(handler);
            } catch (Exception e) {
                logger.error("HandlerResolver of type '" + handlerType.getCanonicalName() + "' couldn't be added to HandlerResolver", e);
            }
        }

        return this;
    }

    /**
     * Allows you to build a handler resolver via the fluent api.
     *
     * @return the client handler resolver builder
     */
    public static TraceeClientHandlerResolverBuilder buildHandlerResolver() {
        return new TraceeClientHandlerResolver();
    }

    /**
     * Creates a handler resolver that only contains the {@link io.tracee.contextlogger.jaxws.container.TraceeClientErrorLoggingHandler}.
     *
     * @return the client handler resolver builder
     */
    public static HandlerResolver createSimpleHandlerResolver() {
        return new TraceeClientHandlerResolver().build();
    }

}
