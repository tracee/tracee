package io.tracee.contextlogger.jaxws.container;

import io.tracee.NoopTraceeLoggerFactory;
import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.contextlogger.TraceeContextLogger;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.xml.ws.handler.PortInfo;

import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import static io.tracee.contextlogger.jaxws.container.AbstractTraceeErrorLoggingHandler.THREAD_LOCAL_SOAP_MESSAGE_STR;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.typeCompatibleWith;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link io.tracee.contextlogger.jaxws.container.TraceeClientHandlerResolver}
 * and fluent api ({@link io.tracee.contextlogger.jaxws.container.TraceeClientHandlerResolverBuilder}).
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({TraceeContextLogger.class, Tracee.class})
public class TraceeClientHandlerResolverTest {

    private final TraceeBackend mockedBackend = mock(TraceeBackend.class);
    private NoopTraceeLoggerFactory loggerFactory = spy(NoopTraceeLoggerFactory.INSTANCE);
    private TraceeContextLogger contextLogger;
    private TraceeClientErrorLoggingHandler unit;

    @Mock
    private PortInfo portInfo;


    @Test
    public void setup () {
        when(mockedBackend.getLoggerFactory()).thenReturn(loggerFactory);
        unit = new TraceeClientErrorLoggingHandler(mockedBackend);
        THREAD_LOCAL_SOAP_MESSAGE_STR.remove();
    }

    @Test
    public void shouldCreateSimpleHandlerResolver () {

        final HandlerResolver handlerResolver = TraceeClientHandlerResolver.createSimpleHandlerResolver();

        assertThat(handlerResolver, Matchers.notNullValue());
        assertThat(handlerResolver.getHandlerChain(portInfo), Matchers.notNullValue());
        assertThat(handlerResolver.getHandlerChain(portInfo).size(), Matchers.is(1));
        assertThat(handlerResolver.getHandlerChain(portInfo).get(0).getClass(), typeCompatibleWith(TraceeClientErrorLoggingHandler.class));
    }

    @Test
    public void shouldCreateHandlerResolverWithOtherHandlerByType() {

        final HandlerResolver handlerResolver = TraceeClientHandlerResolver.buildHandlerResolver().add(TestHandler.class).build();

        assertThat(handlerResolver, Matchers.notNullValue());
        assertThat(handlerResolver.getHandlerChain(portInfo), Matchers.notNullValue());
        assertThat(handlerResolver.getHandlerChain(portInfo).size(), Matchers.is(2));
        assertThat(handlerResolver.getHandlerChain(portInfo).get(0).getClass(), typeCompatibleWith(TestHandler.class));
        assertThat(handlerResolver.getHandlerChain(portInfo).get(1).getClass(), typeCompatibleWith(TraceeClientErrorLoggingHandler.class));
    }

    @Test
    public void shouldCreateHandlerResolverWithOtherHandlerByInstance() {

        final HandlerResolver handlerResolver = TraceeClientHandlerResolver.buildHandlerResolver().add(new TestHandler()).build();

        assertThat(handlerResolver, Matchers.notNullValue());
        assertThat(handlerResolver.getHandlerChain(portInfo), Matchers.notNullValue());
        assertThat(handlerResolver.getHandlerChain(portInfo).size(), Matchers.is(2));
        assertThat(handlerResolver.getHandlerChain(portInfo).get(0).getClass(), typeCompatibleWith(TestHandler.class));
        assertThat(handlerResolver.getHandlerChain(portInfo).get(1).getClass(), typeCompatibleWith(TraceeClientErrorLoggingHandler.class));
    }

    @Test
    public void shouldCreateHandlerResolverAndIgnoreHandlerWithoutDefaultConstructor() {

        final HandlerResolver handlerResolver = TraceeClientHandlerResolver.buildHandlerResolver().add(TestHandlerWithoutDefaultConstructor.class).build();

        assertThat(handlerResolver, Matchers.notNullValue());
        assertThat(handlerResolver.getHandlerChain(portInfo), Matchers.notNullValue());
        assertThat(handlerResolver.getHandlerChain(portInfo).size(), Matchers.is(1));
        assertThat(handlerResolver.getHandlerChain(portInfo).get(0).getClass(), typeCompatibleWith(TraceeClientErrorLoggingHandler.class));
    }

    @Test
    public void shouldCreateHandlerResolverAndIgnoreNullValuedHandlerInstance() {

        final HandlerResolver handlerResolver = TraceeClientHandlerResolver.buildHandlerResolver().add((SOAPHandler) null).build();

        assertThat(handlerResolver, Matchers.notNullValue());
        assertThat(handlerResolver.getHandlerChain(portInfo), Matchers.notNullValue());
        assertThat(handlerResolver.getHandlerChain(portInfo).size(), Matchers.is(1));
        assertThat(handlerResolver.getHandlerChain(portInfo).get(0).getClass(), typeCompatibleWith(TraceeClientErrorLoggingHandler.class));
    }

    @Test
    public void shouldCreateHandlerResolverAndIgnoreNullValuedHandlerType() {

        final HandlerResolver handlerResolver = TraceeClientHandlerResolver.buildHandlerResolver().add((Class<SOAPHandler<SOAPMessageContext>>) null).build();

        assertThat(handlerResolver, Matchers.notNullValue());
        assertThat(handlerResolver.getHandlerChain(portInfo), Matchers.notNullValue());
        assertThat(handlerResolver.getHandlerChain(portInfo).size(), Matchers.is(1));
        assertThat(handlerResolver.getHandlerChain(portInfo).get(0).getClass(), typeCompatibleWith(TraceeClientErrorLoggingHandler.class));
    }

}
