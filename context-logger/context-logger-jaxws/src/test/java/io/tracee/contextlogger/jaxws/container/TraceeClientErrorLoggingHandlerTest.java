package io.tracee.contextlogger.jaxws.container;

import static io.tracee.contextlogger.jaxws.container.AbstractTraceeErrorLoggingHandler.THREAD_LOCAL_SOAP_MESSAGE_STR;
import static org.mockito.Mockito.*;

import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import io.tracee.NoopTraceeLoggerFactory;
import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.contextlogger.TraceeContextLogger;

/**
 * Test class for {@link io.tracee.contextlogger.jaxws.container.TraceeClientErrorLoggingHandler}.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ TraceeContextLogger.class, Tracee.class })
public class TraceeClientErrorLoggingHandlerTest {

    private final TraceeBackend mockedBackend = mock(TraceeBackend.class);
    private NoopTraceeLoggerFactory loggerFactory = spy(NoopTraceeLoggerFactory.INSTANCE);
    private TraceeContextLogger contextLogger;
    private TraceeClientErrorLoggingHandler unit;

    private SOAPMessageContext contextMock;
    private TraceeClientErrorLoggingHandler handlerSpy;

    @Before
    public void setup() {

        when(mockedBackend.getLoggerFactory()).thenReturn(loggerFactory);
        unit = new TraceeClientErrorLoggingHandler(mockedBackend);
        THREAD_LOCAL_SOAP_MESSAGE_STR.remove();

        contextMock = Mockito.mock(SOAPMessageContext.class);
        handlerSpy = Mockito.spy(unit);

    }

    @Test
    public void shouldCallStoreMessageInThreadLocalForOutgoing() {

        handlerSpy.handleOutgoing(contextMock);
        Mockito.verify(handlerSpy).storeMessageInThreadLocal(contextMock);
    }

    @Test
    public void shouldDoNothingForIncoming() {

        handlerSpy.handleIncoming(contextMock);
        Mockito.verify(handlerSpy, never()).storeMessageInThreadLocal(contextMock);
    }

}
