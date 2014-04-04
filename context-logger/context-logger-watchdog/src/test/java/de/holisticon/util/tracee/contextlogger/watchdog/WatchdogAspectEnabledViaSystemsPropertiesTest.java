package de.holisticon.util.tracee.contextlogger.watchdog;

import de.holisticon.util.tracee.TraceeBackend;
import de.holisticon.util.tracee.contextlogger.watchdog.util.WatchdogUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.*;

/**
 * Test class to check if system property enables watchdog execution
 * Created by Tobias Gindler, Holisticon AG on 07.03.14.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(WatchdogUtils.class)
public class WatchdogAspectEnabledViaSystemsPropertiesTest {


    @Test
    public void guard_skip_execution_test () throws Throwable {
        Watchdog watchdog = PowerMockito.mock(Watchdog.class);
        ProceedingJoinPoint proceedingJoinPoint = mock(ProceedingJoinPoint.class);

        when(watchdog.id()).thenReturn("id");
        when(proceedingJoinPoint.proceed()).thenThrow(new TestException());

        PowerMockito.stub(PowerMockito.method(WatchdogUtils.class,"getWatchdogAnnotation")).toReturn(watchdog);
        PowerMockito.stub(PowerMockito.method(WatchdogUtils.class,"checkProcessWatchdog")).toReturn(true);

        // aspect is not mocked completly, so internally the aspect will silently throw and handle an exception
        // this is ok for the test because we only need to check if getWatchdogAnnotation is called
        WatchdogAspect aspect = spy(new WatchdogAspect(true));
        try {
            aspect.guard(proceedingJoinPoint);
        } catch (TestException e) {

        }

        verify(aspect,times(1)).sendErrorReportToConnectors(Mockito.any(TraceeBackend.class),Mockito.any(ProceedingJoinPoint.class),Mockito.any(String.class),Mockito.any(Throwable.class));


    }

}
