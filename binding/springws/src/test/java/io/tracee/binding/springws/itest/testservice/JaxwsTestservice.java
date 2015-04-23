package io.tracee.binding.springws.itest.testservice;

public interface JaxwsTestservice {

	String TNS = "https://github.com/tracee/tracee/examples/jaxws/service/wsdl/";

	String CURRENT_TRACEE_CONTEXT_LOCALPART = "currentTraceeContext";

	String currentTraceeContext();
}
