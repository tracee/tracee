package de.holisticon.util.tracee.jaxws.container;

import javax.jws.WebService;
import java.util.List;

@WebService(targetNamespace = TraceeJaxWsEndpoint.Descriptor.TNS)
public interface TraceeJaxWsEndpoint {

    interface Descriptor {
        String TNS = "https://github.com/holisticon/tracee/examples/jaxws/service/wsdl";
    }

    List<String> getCurrentTraceeContext();

}

