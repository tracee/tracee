package de.holisticon.util.tracee.contextlogger;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.mockito.Mockito.*;

/**
 * Test class to check if system property disables watchdog execution
 * Created by Tobias Gindler, Holisticon AG on 07.03.14.
 */
public class WatchdogAspectDisableViaSystemPropertiesTest {

    @BeforeClass
    public static void init() {
        System.setProperty(Constants.SYSTEM_PROPERTY_IS_ACTIVE, "FALSE");
    }


    @Test
    public void guard_skip_execution_test () throws Throwable {
        ProceedingJoinPoint proceedingJoinPoint = mock(ProceedingJoinPoint.class);

        when(proceedingJoinPoint.proceed()).thenThrow(new TestException());

        WatchdogAspect aspect = spy(new WatchdogAspect());
        try {
            aspect.guard(proceedingJoinPoint);
        } catch (TestException e) {

        }
        verify(aspect,times(1)).getWatchdogAnnotation(proceedingJoinPoint);

    }

}
