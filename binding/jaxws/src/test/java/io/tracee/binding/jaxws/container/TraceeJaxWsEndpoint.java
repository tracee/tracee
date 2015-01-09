package io.tracee.binding.jaxws.container;

import javax.jws.WebService;
import java.util.List;

@WebService(targetNamespace = TraceeJaxWsEndpoint.Descriptor.TNS)
public interface TraceeJaxWsEndpoint {

	interface Descriptor {
		public static final String SERVICE_NAME = "TraceeJaxWsEndpoint";
		String TNS = "https://github.com/tracee/tracee/examples/jaxws/service/wsdl";
	}

    List<String> getCurrentTraceeContext();

}

