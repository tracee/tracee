package io.tracee.binding.jaxws;

import javax.jws.WebService;
import java.util.List;

@WebService(targetNamespace = TraceeJaxWsEndpoint.Descriptor.TNS)
interface TraceeJaxWsEndpoint {

	interface Descriptor {
		String SERVICE_NAME = "TraceeJaxWsEndpoint";
		String TNS = "https://github.com/tracee/tracee/examples/jaxws/service/wsdl";
	}

    List<String> getCurrentTraceeContext();

}

