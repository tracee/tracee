package de.holisticon.util.tracee.contextlogger;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.Test;

import static org.mockito.Mockito.*;

/**
 * Test class to check if system property enables watchdog execution
 * Created by Tobias Gindler, Holisticon AG on 07.03.14.
 */
public class WatchdogAspectEnabledViaSystemsPropertiesTest {


    @Test
    public void guard_skip_execution_test () throws Throwable {
        ProceedingJoinPoint proceedingJoinPoint = mock(ProceedingJoinPoint.class);

        when(proceedingJoinPoint.proceed()).thenThrow(new TestException());


        // aspect is not mocked completly, so internally the aspect will silently throw and handle an exception
        // this is ok for the test because we only need to check if getWatchdogAnnotation is called
        WatchdogAspect aspect = spy(new WatchdogAspect(true));
        try {
            aspect.guard(proceedingJoinPoint);
        } catch (TestException e) {

        }

        verify(aspect,times(1)).getWatchdogAnnotation(proceedingJoinPoint);

    }


}
