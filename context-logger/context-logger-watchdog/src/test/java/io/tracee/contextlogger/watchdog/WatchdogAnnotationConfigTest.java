package io.tracee.contextlogger.watchdog;

import io.tracee.contextlogger.watchdog.util.WatchdogUtils;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Method;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Test extraction of annotated settings.
 * Created by Tobias Gindler, holisticon AG on 20.02.14.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(WatchdogUtils.class)
public class WatchdogAnnotationConfigTest {


    Method annotatedMethod;
    Method notAnnotatedMethod;

    {
        try {
            annotatedMethod = AnnotatedMethodsTestClass.class.getMethod("test3", new Class[0]);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        try {
            notAnnotatedMethod = AnnotatedTestClass.class.getMethod("test", new Class[]{String.class, int.class});
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }


/*

    @Test
    public void mustSuppressException_should_find_exception() {

        ProceedingJoinPoint proceedingJoinPoint = mock(ProceedingJoinPoint.class);
        MethodSignature methodSignature = mock(MethodSignature.class);

        Throwable thrownException = new IllegalArgumentException();

        WatchdogAspect aspect = new WatchdogAspect();

        when(proceedingJoinPoint.getSignature()).thenReturn(methodSignature);
        when(methodSignature.getMethod()).thenReturn(annotatedMethod);

        boolean result = aspect.checkIfMethodThrowsContainsPassedException(proceedingJoinPoint, thrownException);

        assertThat(result, Matchers.equalTo(true));

    }


    @Test
    public void mustSuppressException_should_not_find_exception() {

        ProceedingJoinPoint proceedingJoinPoint = mock(ProceedingJoinPoint.class);
        MethodSignature methodSignature = mock(MethodSignature.class);

        Throwable thrownException = new IOException();

        WatchdogAspect aspect = new WatchdogAspect();

        when(proceedingJoinPoint.getSignature()).thenReturn(methodSignature);
        when(methodSignature.getMethod()).thenReturn(annotatedMethod);

        boolean result = aspect.checkIfMethodThrowsContainsPassedException(proceedingJoinPoint, thrownException);

        assertThat(result, Matchers.equalTo(false));

    }

    @Test
    public void getWatchdogAnnotation_should_find_method_annotation() {

        ProceedingJoinPoint proceedingJoinPoint = mock(ProceedingJoinPoint.class);
        MethodSignature methodSignature = mock(MethodSignature.class);

        when(proceedingJoinPoint.getSignature()).thenReturn(methodSignature);
        when(methodSignature.getMethod()).thenReturn(annotatedMethod);

        when(methodSignature.getDeclaringType()).thenReturn(AnnotatedMethodsTestClass.class);

        WatchdogAspect aspect = new WatchdogAspect();

        Watchdog watchdog = aspect.getWatchdogAnnotation(proceedingJoinPoint);

        assertThat(watchdog, Matchers.notNullValue());
        assertThat(watchdog.id(), Matchers.equalTo(AnnotatedMethodsTestClass.NAME_3));

    }

    @Test
    public void getWatchdogAnnotation_should_find_class_annotation() {

        ProceedingJoinPoint proceedingJoinPoint = mock(ProceedingJoinPoint.class);
        MethodSignature methodSignature = mock(MethodSignature.class);

        when(proceedingJoinPoint.getSignature()).thenReturn(methodSignature);
        when(methodSignature.getMethod()).thenReturn(notAnnotatedMethod);

        when(methodSignature.getDeclaringType()).thenReturn(AnnotatedTestClass.class);

        WatchdogAspect aspect = new WatchdogAspect();

        Watchdog watchdog = aspect.getWatchdogAnnotation(proceedingJoinPoint);

        assertThat(watchdog, Matchers.notNullValue());
        assertThat(watchdog.id(), Matchers.equalTo(AnnotatedTestClass.NAME));

    }

*/
}
