package io.tracee.binding.jaxws;

import io.tracee.Tracee;

import javax.jws.HandlerChain;
import javax.jws.WebService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebService(serviceName = TraceeJaxWsEndpointImpl.Descriptor.SERVICE_NAME, portName = "TraceeJaxWsEndpointPort",
        targetNamespace = TraceeJaxWsEndpoint.Descriptor.TNS,
        endpointInterface = "io.tracee.binding.jaxws.TraceeJaxWsEndpoint")
@HandlerChain(file = TraceeWsHandlerConstants.TRACEE_HANDLER_CHAIN_URL)
public class TraceeJaxWsEndpointImpl implements TraceeJaxWsEndpoint {

    @Override
    public List<String> getCurrentTraceeContext() {

        final List<String> entries = new ArrayList<>();
        for (Map.Entry<String, String> entry : Tracee.getBackend().copyToMap().entrySet()) {
            entries.add(entry.getKey());
            entries.add(entry.getValue());
        }

		Tracee.getBackend().put("called", "yes");

        return entries;
    }
}
