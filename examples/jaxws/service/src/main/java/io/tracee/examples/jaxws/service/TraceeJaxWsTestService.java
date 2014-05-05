package io.tracee.examples.jaxws.service;

import io.tracee.examples.jms.MessageProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.HandlerChain;
import javax.jws.WebService;

@Stateless
@WebService(serviceName = "TraceeJaxWsTestService", portName = "TraceeJaxWsTestPort",
        targetNamespace = "https://github.com/holisticon/tracee/examples/jaxws/service/wsdl")
@HandlerChain(file = "/traceeHandlerChain.xml")
public class TraceeJaxWsTestService implements TraceeJaxWsTestWS {

    private final static Logger LOGGER = LoggerFactory.getLogger(TraceeJaxWsTestService.class);

	@EJB
	private MessageProducer messageProducer;


    @Override
    public int multiply(final int mul1, final int mul2) {
        LOGGER.info("multiply {} with {}", mul1, mul2);
		messageProducer.sendToQueue("Someone wants to multiply " + mul1 +  " with " + mul2 + ". Please text him later!");
        return mul1 * mul2;
    }

    @Override
    public int sum(final int add1, final int add2) {
        LOGGER.info("summarize {} with {}", add1, add2);
		messageProducer.sendToQueue("Someone wants to sum " + add1 +  " with " + add2 + ". Please send him an email later!");
        return add1 + add2;
    }

    @Override
    public int error(final int a, final int b) {
        LOGGER.info("trigger NullPointerException with parameters {} and {}", a, b);
        throw new NullPointerException("JAXWS Tracee Example : Triggered exception with passed parameters '" + a + "' and '" +b + "'");
    }

}
