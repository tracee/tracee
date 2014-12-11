package io.tracee.jaxws.container;

import io.tracee.Tracee;
import io.tracee.jaxws.TraceeWsHandlerConstants;
import org.apache.log4j.Logger;


import javax.ejb.Stateless;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import java.util.*;

@Stateless
@WebService(serviceName = TraceeJaxWsEndpointImpl.Descriptor.SERVICE_NAME, portName = "TraceeJaxWsEndpointPort",
        targetNamespace = TraceeJaxWsEndpoint.Descriptor.TNS,
        endpointInterface = "io.tracee.jaxws.container.TraceeJaxWsEndpoint")
@HandlerChain(file = TraceeWsHandlerConstants.TRACEE_HANDLER_CHAIN_URL)
public class TraceeJaxWsEndpointImpl implements TraceeJaxWsEndpoint {

    private final Logger LOGGER = Logger.getLogger(TraceeJaxWsEndpointImpl.class);

    @Override
    public List<String> getCurrentTraceeContext() {

        final List<String> entries = new ArrayList<String>();
        LOGGER.info("Hello from Endpoint");
        for (Map.Entry<String, String> entry : Tracee.getBackend().entrySet()) {
            entries.add(entry.getKey());
            entries.add(entry.getValue());
        }

        return entries;
    }
}
