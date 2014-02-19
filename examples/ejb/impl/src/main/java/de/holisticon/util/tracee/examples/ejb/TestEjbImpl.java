package de.holisticon.util.tracee.examples.ejb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;

/**
 * Created by Tobias Gindler, holisticon AG on 16.01.14.
 */
@Stateless
public class TestEjbImpl implements TestEjb{

    private final static Logger LOGGER = LoggerFactory.getLogger(TestEjbImpl.class);


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
        throw new NullPointerException("Tracee local Remote EJB example: Triggered exception with passed parameters '" + a + "' and '" +b + "'");
    }

}
