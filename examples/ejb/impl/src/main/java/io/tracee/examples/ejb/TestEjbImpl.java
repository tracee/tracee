package io.tracee.examples.ejb;

import io.tracee.contextlogger.javaee.TraceeErrorContextLoggingInterceptor;
import io.tracee.contextlogger.watchdog.Watchdog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

@Stateless
public class TestEjbImpl implements TestEjb {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestEjbImpl.class);


    @Override
    public int multiply(final int mul1, final int mul2) {
        LOGGER.info("multiply {} with {}", mul1, mul2);
        return mul1 * mul2;
    }

    @Override
    public int sum(final int add1, final int add2) {
        LOGGER.info("summarize {} with {}", add1, add2);
        return add1 + add2;
    }

    @Override
    @Interceptors({ TraceeErrorContextLoggingInterceptor.class })
    public int error(final int a, final int b) {
        LOGGER.info("trigger NullPointerException with parameters {} and {}", a, b);
        return watchdogError(a, b);
    }

    @Watchdog
    private int watchdogError(final int a, final int b) {
        throw new NullPointerException("Tracee local Remote EJB example: Triggered exception with passed parameters '" + a + "' and '" + b + "'");
    }

}
