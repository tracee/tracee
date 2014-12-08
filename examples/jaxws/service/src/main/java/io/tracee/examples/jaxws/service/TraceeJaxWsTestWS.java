package io.tracee.examples.jaxws.service;

import javax.ejb.Remote;
import javax.jws.WebMethod;
import javax.jws.WebService;

@Remote
@WebService(targetNamespace = "https://github.com/tracee/tracee/examples/jaxws/service/wsdl")
public interface TraceeJaxWsTestWS {

	@WebMethod(operationName = "multiply", action = "multiplyAction")
    int multiply(int mul1, int mul2);

	@WebMethod(operationName = "sum", action = "sumAction")
    int sum(int add1, int add2);

	@WebMethod(operationName = "error", action = "errorAction")
    int error(int a, int b);
}

