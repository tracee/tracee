package de.holisticon.util.tracee.jaxws.server;

import de.holisticon.util.tracee.Tracee;
import de.holisticon.util.tracee.jaxws.TraceeWsHandlerConstants;
import org.apache.log4j.Logger;


import javax.ejb.Stateless;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import java.util.*;

@Stateless
@WebService(serviceName = "TraceeJaxWsEndpoint", portName = "TraceeJaxWsEndpointPort",
        targetNamespace = TraceeJaxWsEndpoint.Descriptor.TNS,
        endpointInterface = "de.holisticon.util.tracee.jaxws.server.TraceeJaxWsEndpoint")
@HandlerChain(file = TraceeWsHandlerConstants.TRACEE_WITH_ERROR_LOGGING_HANDLER_CHAIN_URL)
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
