package de.holisticon.util.tracee.examples.jaxws.service;

import javax.ejb.Remote;
import javax.jws.WebService;

@Remote
@WebService(targetNamespace = "https://github.com/holisticon/tracee/examples/jaxws/service/wsdl")
public interface TraceeJaxWsTestService {

    int multiply(int mul1, int mul2);

    int sum(int add1, int add2);

    int error(int a, int b);
}

