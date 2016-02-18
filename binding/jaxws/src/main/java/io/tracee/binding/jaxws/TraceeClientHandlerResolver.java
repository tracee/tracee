package io.tracee.binding.jaxws;

import io.tracee.TraceeBackend;

import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.PortInfo;
import java.util.ArrayList;
import java.util.List;

public class TraceeClientHandlerResolver implements HandlerResolver {

    private final List<Handler> handlerList = new ArrayList<>();

    public TraceeClientHandlerResolver() {
        handlerList.add(new TraceeClientHandler());
    }

	TraceeClientHandlerResolver(TraceeBackend backend) {
		handlerList.add(new TraceeClientHandler(backend));
	}

    @Override
    public final List<Handler> getHandlerChain(PortInfo portInfo) {
        return handlerList;
    }
}
