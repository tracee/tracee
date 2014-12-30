package io.tracee.binding.springws.itest.testservice;

import io.tracee.Tracee;
import io.tracee.TraceeConstants;
import io.tracee.binding.jaxws.TraceeWsHandlerConstants;

import javax.jws.HandlerChain;
import javax.jws.WebResult;
import javax.jws.WebService;

@WebService(targetNamespace = JaxwsTestservice.TNS, name = JaxwsTestservice.CURRENT_TRACEE_CONTEXT_LOCALPART)
@HandlerChain(file = TraceeWsHandlerConstants.TRACEE_HANDLER_CHAIN_URL)
public class JaxwsTestserviceEndpoint implements JaxwsTestservice {

	@WebResult(name = "remoteInvocationId")
	public String currentTraceeContext() {
		Tracee.getBackend().put("testId", "TestValueFromRemoteEndpoint");
		return Tracee.getBackend().get(TraceeConstants.INVOCATION_ID_KEY);
	}
}
