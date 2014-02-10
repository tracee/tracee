package de.holisticon.util.tracee.jaxws.client;

import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.PortInfo;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tobias Gindler (Holisticon AG)
 */
public class TraceeClientHandlerResolver implements HandlerResolver {

    private final List<Handler> handlerList = new ArrayList<Handler>();

    public TraceeClientHandlerResolver() {
        handlerList.add(new TraceeClientHandler());
    }

    @Override
    public final List<Handler> getHandlerChain(PortInfo portInfo) {
        return handlerList;
    }


}
