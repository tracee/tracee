package io.tracee.spring;


import io.tracee.TraceeBackend;
import io.tracee.TraceeLogger;
import io.tracee.spi.TraceeBackendProvider;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/test-config.xml"})
public class LogContextOnExceptionAspectTest {
    @Resource
    private MySpringService service;

    @After
    public void resetLoggerMock() {
        MockBackendProvider.mockedLogger = mock(TraceeLogger.class);
    }


    @Test
    public void should_not_log_if_there_is_NO_exception() {
        service.everythingIsAlright();
        verifyZeroInteractions(MockBackendProvider.mockedLogger);
    }

    @Test
    public void should_log_if_exception_occurs() {
        try {
            service.somethingBadHappens();
        } catch (RuntimeException e) {
            verify(MockBackendProvider.mockedLogger).error(Mockito.startsWith("TRACEE SPRING CONTEXT LOGGING LISTENER : "));
        }
    }

    public static class MockBackendProvider implements TraceeBackendProvider {
        static TraceeLogger mockedLogger = mock(TraceeLogger.class);

        @Override
        public TraceeBackend provideBackend() {
            final TraceeBackend mock = mock(TraceeBackend.class, Mockito.RETURNS_DEEP_STUBS);
            when(mock.getLoggerFactory().getLogger(Matchers.<Class<Object>>any())).thenReturn(mockedLogger);
            return mock;
        }
    }


}