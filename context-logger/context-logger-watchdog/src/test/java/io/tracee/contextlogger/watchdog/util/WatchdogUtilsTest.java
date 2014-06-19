package io.tracee.contextlogger.watchdog.util;

import io.tracee.contextlogger.watchdog.Watchdog;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Method;

/**
 * Test class for {@link io.tracee.contextlogger.watchdog.util.WatchdogUtils}.
 * Created by Tobias Gindler, holisticon AG on 03.04.14.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(WatchdogUtils.class)
@Watchdog(id = "class")
public class WatchdogUtilsTest {

    private final static Class[] CLASSES = {NullPointerException.class, IllegalArgumentException.class, IllegalStateException.class};

    @Before
    public void init() {

    }

    @Test
    public void checkClassIsDefinedInThrowsException_should_return_true_if_exception_is_found() {

        boolean result = WatchdogUtils.checkClassIsDefinedInThrowsException(CLASSES, new NullPointerException());
        MatcherAssert.assertThat(result, Matchers.equalTo(true));

    }

    @Test
    public void checkClassIsDefinedInThrowsException_should_return_false_if_exception_is_not_found() {

        boolean result = WatchdogUtils.checkClassIsDefinedInThrowsException(CLASSES, new InterruptedException());
        MatcherAssert.assertThat(result, Matchers.equalTo(false));

    }

    @Test
    public void checkClassIsDefinedInThrowsException_should_return_false_if_exception_is_null() {

        boolean result = WatchdogUtils.checkClassIsDefinedInThrowsException(CLASSES, null);
        MatcherAssert.assertThat(result, Matchers.equalTo(false));

    }

    @Test
    public void checkClassIsDefinedInThrowsException_should_return_false_if_exception_types_are_null() {

        boolean result = WatchdogUtils.checkClassIsDefinedInThrowsException(null, new InterruptedException());
        MatcherAssert.assertThat(result, Matchers.equalTo(false));

    }

    @Test
    public void checkIfMethodThrowsDoesNotContainPassedException_should_return_null_if_passed_proceedingjoinpoint_is_null () {

        boolean result = WatchdogUtils.checkIfMethodThrowsContainsPassedException(null, new InterruptedException());
        MatcherAssert.assertThat(result, Matchers.equalTo(false));

    }

    @Test
    public void checkIfMethodThrowsDoesNotContainPassedException_should_return_null_if_passed_throwable_is_null () {

        ProceedingJoinPoint proceedingJoinPoint = PowerMockito.mock(ProceedingJoinPoint.class);

        boolean result = WatchdogUtils.checkIfMethodThrowsContainsPassedException(proceedingJoinPoint, null);
        MatcherAssert.assertThat(result, Matchers.equalTo(false));

    }


    @Test
    public void getDefinedThrowsFromMethodSignature_should_return_empty_array_for_method_without_throws (){
        Method method = null;
        try {
            method = WatchdogUtilsTest.class.getMethod("getDefinedThrowsFromMethodSignature_should_return_empty_array_for_method_without_throws",null);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException();
        }

        ProceedingJoinPoint proceedingJoinPoint = Mockito.mock(ProceedingJoinPoint.class);
        MethodSignature signature = Mockito.mock(MethodSignature.class);
        Mockito.when(proceedingJoinPoint.getSignature()).thenReturn(signature);
        Mockito.when(signature.getMethod()).thenReturn(method);


        Class[] classes = WatchdogUtils.getDefinedThrowsFromMethodSignature(proceedingJoinPoint);

        MatcherAssert.assertThat(classes, Matchers.notNullValue());
        MatcherAssert.assertThat(classes.length, Matchers.equalTo(0));


    }

    @Test
    public void getDefinedThrowsFromMethodSignature_should_return_empty_array_for_method_with_throws () throws NullPointerException,IllegalArgumentException{
        Method method = null;
        try {
            method = WatchdogUtilsTest.class.getMethod("getDefinedThrowsFromMethodSignature_should_return_empty_array_for_method_with_throws",null);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException();
        }

        ProceedingJoinPoint proceedingJoinPoint = Mockito.mock(ProceedingJoinPoint.class);
        MethodSignature signature = Mockito.mock(MethodSignature.class);
        Mockito.when(proceedingJoinPoint.getSignature()).thenReturn(signature);
        Mockito.when(signature.getMethod()).thenReturn(method);


        Class[] classes = WatchdogUtils.getDefinedThrowsFromMethodSignature(proceedingJoinPoint);

        MatcherAssert.assertThat(classes, Matchers.notNullValue());
        MatcherAssert.assertThat(classes.length, Matchers.equalTo(2));
    }

    @Test
    public void getDefinedThrowsFromMethodSignature_should_return_empty_array_if_passed_proceedingjoinpoint_is_null() {
        Class[] classes = WatchdogUtils.getDefinedThrowsFromMethodSignature(null);

        MatcherAssert.assertThat(classes, Matchers.notNullValue());
        MatcherAssert.assertThat(classes.length, Matchers.equalTo(0));
    }




    @Test
    public void checkIfMethodThrowsContainsPassedException_should_return_false_for_method_without_throws (){
        Method method = null;
        try {
            method = WatchdogUtilsTest.class.getMethod("checkIfMethodThrowsContainsPassedException_should_return_false_for_method_without_throws",null);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException();
        }

        ProceedingJoinPoint proceedingJoinPoint = Mockito.mock(ProceedingJoinPoint.class);
        MethodSignature signature = Mockito.mock(MethodSignature.class);
        Mockito.when(proceedingJoinPoint.getSignature()).thenReturn(signature);
        Mockito.when(signature.getMethod()).thenReturn(method);

        boolean result = WatchdogUtils.checkIfMethodThrowsContainsPassedException(proceedingJoinPoint, new NullPointerException());

        MatcherAssert.assertThat(result, Matchers.notNullValue());
        MatcherAssert.assertThat(result, Matchers.equalTo(false));


    }

    @Test
    public void checkIfMethodThrowsContainsPassedException_should_return_false_for_null_valued_proceedingjoinpoint () throws NullPointerException,IllegalArgumentException{

        boolean result = WatchdogUtils.checkIfMethodThrowsContainsPassedException(null, new NullPointerException());

        MatcherAssert.assertThat(result, Matchers.notNullValue());
        MatcherAssert.assertThat(result, Matchers.equalTo(false));
    }

    @Test
    public void checkIfMethodThrowsContainsPassedException_should_return_empty_array_for_null_valued_throwable_instance (){
        Method method = null;
        try {
            method = WatchdogUtilsTest.class.getMethod("checkIfMethodThrowsContainsPassedException_should_return_false_for_null_valued_proceedingjoinpoint",null);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException();
        }

        ProceedingJoinPoint proceedingJoinPoint = Mockito.mock(ProceedingJoinPoint.class);
        MethodSignature signature = Mockito.mock(MethodSignature.class);
        Mockito.when(proceedingJoinPoint.getSignature()).thenReturn(signature);
        Mockito.when(signature.getMethod()).thenReturn(method);

        boolean result = WatchdogUtils.checkIfMethodThrowsContainsPassedException(proceedingJoinPoint, null);

        MatcherAssert.assertThat(result, Matchers.notNullValue());
        MatcherAssert.assertThat(result, Matchers.equalTo(false));


    }

    @Test
    public void checkProcessWatchdog_should_return_false_if_watchdog_annotation_is_null () {

        ProceedingJoinPoint proceedingJoinPoint = Mockito.mock(ProceedingJoinPoint.class);

        boolean result = WatchdogUtils.checkProcessWatchdog(null,proceedingJoinPoint,new NullPointerException());

        MatcherAssert.assertThat(result, Matchers.equalTo(false));

    }

    @Test
    public void checkProcessWatchdog_should_return_true_if_proceedingjoinpoint_is_null () {

        ProceedingJoinPoint proceedingJoinPoint = Mockito.mock(ProceedingJoinPoint.class);
        Watchdog watchdog = Mockito.mock(Watchdog.class);
        Mockito.when(watchdog.isActive()).thenReturn(true);
        Mockito.when(watchdog.suppressThrowsExceptions()).thenReturn(true);

        boolean result = WatchdogUtils.checkProcessWatchdog(watchdog,null,new NullPointerException());

        MatcherAssert.assertThat(result, Matchers.equalTo(true));

    }

    @Test
    public void checkProcessWatchdog_should_return_true_if_throwable_is_null () {
        ProceedingJoinPoint proceedingJoinPoint = Mockito.mock(ProceedingJoinPoint.class);
        Watchdog watchdog = Mockito.mock(Watchdog.class);
        Mockito.when(watchdog.isActive()).thenReturn(true);
        Mockito.when(watchdog.suppressThrowsExceptions()).thenReturn(true);

        boolean result = WatchdogUtils.checkProcessWatchdog(watchdog,proceedingJoinPoint,null);

        MatcherAssert.assertThat(result, Matchers.equalTo(true));

    }


    @Test
    public void checkProcessWatchdog_should_return_true_if_throwable_isnt_part_of_method_signature () {

        Method method = null;
        try {
            method = WatchdogUtilsTest.class.getMethod("checkProcessWatchdog_should_return_true_if_throwable_isnt_part_of_method_signature",null);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException();
        }

        ProceedingJoinPoint proceedingJoinPoint = Mockito.mock(ProceedingJoinPoint.class);
        MethodSignature signature = Mockito.mock(MethodSignature.class);
        Mockito.when(proceedingJoinPoint.getSignature()).thenReturn(signature);
        Mockito.when(signature.getMethod()).thenReturn(method);


        Watchdog watchdog = Mockito.mock(Watchdog.class);
        Mockito.when(watchdog.isActive()).thenReturn(true);
        Mockito.when(watchdog.suppressThrowsExceptions()).thenReturn(true);

        boolean result = WatchdogUtils.checkProcessWatchdog(watchdog,proceedingJoinPoint,null);

        MatcherAssert.assertThat(result, Matchers.equalTo(true));

    }

    @Test
    public void checkProcessWatchdog_should_return_false_if_throwable_is_part_of_method_signature () throws NullPointerException{

        Method method = null;
        try {
            method = WatchdogUtilsTest.class.getMethod("checkProcessWatchdog_should_return_false_if_throwable_is_part_of_method_signature",null);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException();
        }

        ProceedingJoinPoint proceedingJoinPoint = Mockito.mock(ProceedingJoinPoint.class);
        MethodSignature signature = Mockito.mock(MethodSignature.class);
        Mockito.when(proceedingJoinPoint.getSignature()).thenReturn(signature);
        Mockito.when(signature.getMethod()).thenReturn(method);

        Watchdog watchdog = Mockito.mock(Watchdog.class);
        Mockito.when(watchdog.isActive()).thenReturn(true);
        Mockito.when(watchdog.suppressThrowsExceptions()).thenReturn(true);

        boolean result = WatchdogUtils.checkProcessWatchdog(watchdog,proceedingJoinPoint,new NullPointerException());

        MatcherAssert.assertThat(result, Matchers.equalTo(false));

    }

    @Test
    @Watchdog(id = "method")
    public void getWatchdogAnnotation_should_return_watchdog_if_method_has_watchdog_annotation (){
        Method method = null;
        try {
            method = WatchdogUtilsTest.class.getMethod("getWatchdogAnnotation_should_return_watchdog_if_method_has_watchdog_annotation",null);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException();
        }

        ProceedingJoinPoint proceedingJoinPoint = Mockito.mock(ProceedingJoinPoint.class);
        MethodSignature signature = Mockito.mock(MethodSignature.class);
        Mockito.when(proceedingJoinPoint.getSignature()).thenReturn(signature);
        Mockito.when(signature.getMethod()).thenReturn(method);


        Watchdog watchdog = WatchdogUtils.getWatchdogAnnotation(proceedingJoinPoint);

        MatcherAssert.assertThat(watchdog, Matchers.notNullValue());
        MatcherAssert.assertThat(watchdog.id(), Matchers.equalTo("method"));


    }

    @Test
    public void getWatchdogAnnotation_should_return_watchdog_if_class_has_watchdog_annotation (){
        Method method = null;
        try {
            method = WatchdogUtilsTest.class.getMethod("getWatchdogAnnotation_should_return_watchdog_if_class_has_watchdog_annotation",null);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException();
        }

        ProceedingJoinPoint proceedingJoinPoint = Mockito.mock(ProceedingJoinPoint.class);
        MethodSignature signature = Mockito.mock(MethodSignature.class);
        Mockito.when(proceedingJoinPoint.getSignature()).thenReturn(signature);
        Mockito.when(signature.getMethod()).thenReturn(method);
        Mockito.when(signature.getDeclaringType()).thenReturn(WatchdogUtilsTest.class);


        Watchdog watchdog = WatchdogUtils.getWatchdogAnnotation(proceedingJoinPoint);

        MatcherAssert.assertThat(watchdog, Matchers.notNullValue());
        MatcherAssert.assertThat(watchdog.id(), Matchers.equalTo("class"));


    }



}
