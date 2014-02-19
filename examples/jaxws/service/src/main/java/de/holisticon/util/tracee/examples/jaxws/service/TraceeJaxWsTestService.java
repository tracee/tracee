package de.holisticon.util.tracee.examples.jaxws.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.jws.HandlerChain;
import javax.jws.WebService;

@Stateless
@WebService(serviceName = "TraceeJaxWsTestService", portName = "TraceeJaxWsTestPort",
        targetNamespace = "https://github.com/holisticon/tracee/examples/jaxws/service/wsdl",
        endpointInterface = "de.holisticon.util.tracee.examples.jaxws.service.TraceeJaxWsTestWS")
@HandlerChain(file = "/traceeHandlerChain.xml")
public class TraceeJaxWsTestService implements TraceeJaxWsTestWS {

    private final static Logger LOGGER = LoggerFactory.getLogger(TraceeJaxWsTestService.class);

    @Override
    public final int multiply(final int mul1, final int mul2) {
        LOGGER.info("multiply {} with {}", mul1, mul2);
        return mul1 * mul2;
    }

    @Override
    public final int sum(final int add1, final int add2) {
        LOGGER.info("summarize {} with {}", add1, add2);
        return add1 + add2;
    }

    @Override
    public final int error(final int a, final int b) {
        LOGGER.info("trigger NullPointerException with parameters {} and {}", a, b);
        throw new NullPointerException("JAXWS Tracee Example : Triggered exception with passed parameters '" + a + "' and '" +b + "'");
    }

}
