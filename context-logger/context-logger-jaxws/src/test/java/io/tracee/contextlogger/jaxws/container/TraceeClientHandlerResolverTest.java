package io.tracee.contextlogger.jaxws.container;

import static io.tracee.contextlogger.jaxws.container.AbstractTraceeErrorLoggingHandler.THREAD_LOCAL_SOAP_MESSAGE_STR;
import static org.mockito.Mockito.*;

import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.PortInfo;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import io.tracee.NoopTraceeLoggerFactory;
import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.contextlogger.TraceeContextLogger;

/**
 * Test class for {@link io.tracee.contextlogger.jaxws.container.TraceeClientHandlerResolver} and fluent api (
 * {@link io.tracee.contextlogger.jaxws.container.TraceeClientHandlerResolverBuilder}).
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({ TraceeContextLogger.class, Tracee.class })
public class TraceeClientHandlerResolverTest {

    private final TraceeBackend mockedBackend = mock(TraceeBackend.class);
    private NoopTraceeLoggerFactory loggerFactory = spy(NoopTraceeLoggerFactory.INSTANCE);
    private TraceeContextLogger contextLogger;
    private TraceeClientErrorLoggingHandler unit;

    @Mock
    private PortInfo portInfo;

    @Test
    public void setup() {

        when(mockedBackend.getLoggerFactory()).thenReturn(loggerFactory);
        unit = new TraceeClientErrorLoggingHandler(mockedBackend);
        THREAD_LOCAL_SOAP_MESSAGE_STR.remove();

    }

    @Test
    public void shouldCreateSimpleHandlerResolver() {

        HandlerResolver handlerResolver = TraceeClientHandlerResolver.createSimpleHandlerResolver();

        MatcherAssert.assertThat(handlerResolver, Matchers.notNullValue());
        MatcherAssert.assertThat(handlerResolver.getHandlerChain(portInfo), Matchers.notNullValue());
        MatcherAssert.assertThat(handlerResolver.getHandlerChain(portInfo).size(), Matchers.is(1));
        MatcherAssert.assertThat(handlerResolver.getHandlerChain(portInfo).get(0).getClass(),
                Matchers.typeCompatibleWith(TraceeClientErrorLoggingHandler.class));

    }

    @Test
    public void shouldCreateHandlerResolverWithOtherHandlerByType() {

        HandlerResolver handlerResolver = TraceeClientHandlerResolver.buildHandlerResolver().add(TestHandler.class).build();

        MatcherAssert.assertThat(handlerResolver, Matchers.notNullValue());
        MatcherAssert.assertThat(handlerResolver.getHandlerChain(portInfo), Matchers.notNullValue());
        MatcherAssert.assertThat(handlerResolver.getHandlerChain(portInfo).size(), Matchers.is(2));
        MatcherAssert.assertThat(handlerResolver.getHandlerChain(portInfo).get(0).getClass(), Matchers.typeCompatibleWith(TestHandler.class));
        MatcherAssert.assertThat(handlerResolver.getHandlerChain(portInfo).get(1).getClass(),
                Matchers.typeCompatibleWith(TraceeClientErrorLoggingHandler.class));

    }

    @Test
    public void shouldCreateHandlerResolverWithOtherHandlerByInstance() {

        HandlerResolver handlerResolver = TraceeClientHandlerResolver.buildHandlerResolver().add(new TestHandler()).build();

        MatcherAssert.assertThat(handlerResolver, Matchers.notNullValue());
        MatcherAssert.assertThat(handlerResolver.getHandlerChain(portInfo), Matchers.notNullValue());
        MatcherAssert.assertThat(handlerResolver.getHandlerChain(portInfo).size(), Matchers.is(2));
        MatcherAssert.assertThat(handlerResolver.getHandlerChain(portInfo).get(0).getClass(), Matchers.typeCompatibleWith(TestHandler.class));
        MatcherAssert.assertThat(handlerResolver.getHandlerChain(portInfo).get(1).getClass(),
                Matchers.typeCompatibleWith(TraceeClientErrorLoggingHandler.class));

    }

    @Test
    public void shouldCreateHandlerResolverAndIgnoreHandlerWithoutDefaultConstructor() {

        HandlerResolver handlerResolver = TraceeClientHandlerResolver.buildHandlerResolver().add(TestHandlerWithoutDefaultConstructor.class).build();

        MatcherAssert.assertThat(handlerResolver, Matchers.notNullValue());
        MatcherAssert.assertThat(handlerResolver.getHandlerChain(portInfo), Matchers.notNullValue());
        MatcherAssert.assertThat(handlerResolver.getHandlerChain(portInfo).size(), Matchers.is(1));
        MatcherAssert.assertThat(handlerResolver.getHandlerChain(portInfo).get(0).getClass(),
                Matchers.typeCompatibleWith(TraceeClientErrorLoggingHandler.class));

    }

    @Test
    public void shouldCreateHandlerResolverAndIgnoreNullValuedHandlerInstance() {

        HandlerResolver handlerResolver = TraceeClientHandlerResolver.buildHandlerResolver().add((SOAPHandler)null).build();

        MatcherAssert.assertThat(handlerResolver, Matchers.notNullValue());
        MatcherAssert.assertThat(handlerResolver.getHandlerChain(portInfo), Matchers.notNullValue());
        MatcherAssert.assertThat(handlerResolver.getHandlerChain(portInfo).size(), Matchers.is(1));
        MatcherAssert.assertThat(handlerResolver.getHandlerChain(portInfo).get(0).getClass(),
                Matchers.typeCompatibleWith(TraceeClientErrorLoggingHandler.class));

    }

    @Test
    public void shouldCreateHandlerResolverAndIgnoreNullValuedHandlerType() {

        HandlerResolver handlerResolver = TraceeClientHandlerResolver.buildHandlerResolver().add((Class<SOAPHandler<SOAPMessageContext>>)null).build();

        MatcherAssert.assertThat(handlerResolver, Matchers.notNullValue());
        MatcherAssert.assertThat(handlerResolver.getHandlerChain(portInfo), Matchers.notNullValue());
        MatcherAssert.assertThat(handlerResolver.getHandlerChain(portInfo).size(), Matchers.is(1));
        MatcherAssert.assertThat(handlerResolver.getHandlerChain(portInfo).get(0).getClass(),
                Matchers.typeCompatibleWith(TraceeClientErrorLoggingHandler.class));

    }

}
