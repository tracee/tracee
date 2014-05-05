package io.tracee.contextlogger.watchdog;

import org.junit.BeforeClass;

/**
 * Test class to check if watchdog annotation enables watchdog execution
 * Created by Tobias Gindler, Holisticon AG on 07.03.14.
 */
public class WatchdogAspectEnabledOrDisabledViaAnnotationTest {

    @BeforeClass
    public static void init() {
        System.setProperty(Constants.SYSTEM_PROPERTY_IS_ACTIVE, "TRUE");
    }

    @Watchdog(isActive = true)
    public void activeWatchdogMethod() {

    }

    @Watchdog(isActive = false)
    public void inactiveWatchdogMethod() {

    }

    private Watchdog activeWatchdog;
    private Watchdog inactiveWatchdog;

    {
        try {
			activeWatchdog = WatchdogAspectEnabledOrDisabledViaAnnotationTest.class.getMethod("activeWatchdogMethod", new Class[0]).getAnnotation(Watchdog.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        try {
            inactiveWatchdog = WatchdogAspectEnabledOrDisabledViaAnnotationTest.class.getMethod("inactiveWatchdogMethod", new Class[0]).getAnnotation(Watchdog.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /*
    @Test
    public void guard_skip_execution_test () throws Throwable {
        ProceedingJoinPoint proceedingJoinPoint = mock(ProceedingJoinPoint.class);

        when(proceedingJoinPoint.proceed()).thenThrow(new TestException());


        // aspect is not mocked completly, so internally the aspect will silently throw and handle an exception
        // this is ok for the test because we only need to check if getWatchdogAnnotation is called
        WatchdogAspect aspect = spy(new WatchdogAspect());
        doReturn(activeWatchdog).when(aspect).getWatchdogAnnotation(proceedingJoinPoint);

        try {
            aspect.guard(proceedingJoinPoint);
        } catch (TestException e) {

        }

        verify(aspect,times(1)).checkIfMethodThrowsContainsPassedException(any(ProceedingJoinPoint.class), any(TestException.class));

    }

    @Test
    public void guard_do_execution_test () throws Throwable {
        ProceedingJoinPoint proceedingJoinPoint = mock(ProceedingJoinPoint.class);

        when(proceedingJoinPoint.proceed()).thenThrow(new TestException());


        // aspect is not mocked completely, so internally the aspect will silently throw and handle an exception
        // this is ok for the test because we only need to check if getWatchdogAnnotation is called
        WatchdogAspect aspect = spy(new WatchdogAspect());
        doReturn(inactiveWatchdog).when(aspect).getWatchdogAnnotation(proceedingJoinPoint);

        try {
            aspect.guard(proceedingJoinPoint);
        } catch (TestException e) {

        }

        verify(aspect,times(0)).checkIfMethodThrowsContainsPassedException(any(ProceedingJoinPoint.class), any(TestException.class));

    }



*/

}
