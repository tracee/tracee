package de.holisticon.util.tracee.examples.jaxws.service;

import javax.ejb.Stateless;
import javax.jws.HandlerChain;
import javax.jws.WebService;

@Stateless
@WebService(serviceName = "TraceeJaxWsTestService", portName = "TraceeJaxWsTestPort",
        targetNamespace = "https://github.com/holisticon/tracee/examples/jaxws/service/wsdl",
        endpointInterface = "de.holisticon.util.tracee.examples.jaxws.service.TraceeJaxWsTestWS")
@HandlerChain(file = "handlerChain.xml")
public class TraceeJaxWsTestService implements TraceeJaxWsTestWS {

    @Override
    public final int multiply(final int mul1, final int mul2) {
        return mul1 * mul2;
    }

    @Override
    public final int sum(final int add1, final int add2) {
        return add1 + add2;
    }

    @Override
    public final int error(final int a, final int b) {
        throw new NullPointerException("TraceeJaxWsTestService test error");
    }


}
