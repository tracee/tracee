package de.holisticon.util.tracee.jaxws.server;

import javax.jws.WebService;
import java.util.Map;

@WebService(targetNamespace = TraceeJaxWsEndpoint.Descriptor.TNS)
public interface TraceeJaxWsEndpoint {

    interface Descriptor {
        String TNS = "https://github.com/holisticon/tracee/examples/jaxws/service/wsdl";
    }

    Map<String,String> getCurrentTraceeContext();

}

