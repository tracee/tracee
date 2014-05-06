package io.tracee.contextlogger.watchdog;

/**
 * Test class to check if system property disables watchdog execution
 * Created by Tobias Gindler, Holisticon AG on 07.03.14.
 */
public class WatchdogAspectDisableViaSystemPropertiesTest {

/*

    @Test
    public void guard_skip_execution_test () throws Throwable {
        ProceedingJoinPoint proceedingJoinPoint = mock(ProceedingJoinPoint.class);

        when(proceedingJoinPoint.proceed()).thenThrow(new TestException());

        WatchdogAspect aspect = spy(new WatchdogAspect(false));
        try {
            aspect.guard(proceedingJoinPoint);
        } catch (TestException e) {

        }
        verify(aspect,never()).getWatchdogAnnotation(proceedingJoinPoint);

    }
*/
}
