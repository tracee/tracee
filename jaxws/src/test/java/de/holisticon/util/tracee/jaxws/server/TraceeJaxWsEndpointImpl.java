package de.holisticon.util.tracee.jaxws.server;

import de.holisticon.util.tracee.Tracee;
import de.holisticon.util.tracee.jaxws.TraceeWsHandlerConstants;

import javax.ejb.Stateless;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import java.util.Map;
import java.util.TreeMap;

@Stateless
@WebService(serviceName = "TraceeJaxWsEndpoint", portName = "TraceeJaxWsEndpointPort",
        targetNamespace = TraceeJaxWsEndpoint.Descriptor.TNS,
        endpointInterface = "de.holisticon.util.tracee.jaxws.server.TraceeJaxWsEndpoint")
@HandlerChain(file = TraceeWsHandlerConstants.TRACEE_WITH_ERROR_LOGGING_HANDLER_CHAIN_URL)
public class TraceeJaxWsEndpointImpl implements TraceeJaxWsEndpoint {

    @Override
    public Map<String, String> getCurrentTraceeContext() {
        return new TreeMap<String, String>(Tracee.getBackend().extractContext());
    }
}
