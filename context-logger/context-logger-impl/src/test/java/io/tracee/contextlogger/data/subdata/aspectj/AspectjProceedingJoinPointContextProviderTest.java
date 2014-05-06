package io.tracee.contextlogger.data.subdata.aspectj;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Test class for {@link io.tracee.contextlogger.data.subdata.aspectj.AspectjProceedingJoinPointContextProvider}.
 * Created by Tobias Gindler, holisticon AG on 31.03.14.
 */
public class AspectjProceedingJoinPointContextProviderTest {

    private static Method METHOD;

    public void test() {

    }

    {
        try {
            METHOD = AspectjProceedingJoinPointContextProviderTest.class.getMethod("test", new Class[0]);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void should_return_wrapped_type() {

        AspectjProceedingJoinPointContextProvider givenAspectjProceedingJoinPointContextProvider = new AspectjProceedingJoinPointContextProvider();

        Class result = givenAspectjProceedingJoinPointContextProvider.getWrappedType();

        MatcherAssert.assertThat(result.equals(ProceedingJoinPoint.class), Matchers.equalTo(true));

    }


    @Test
    public void should_return_null_for_class_for_null_valued_proceedingoinpoint() {
        AspectjProceedingJoinPointContextProvider givenAspectjProceedingJoinPointContextProvider = new AspectjProceedingJoinPointContextProvider();
        givenAspectjProceedingJoinPointContextProvider.setContextData(null);

        String result = givenAspectjProceedingJoinPointContextProvider.getClazz();

        MatcherAssert.assertThat(result, Matchers.nullValue());
    }


    @Test
    public void should_return_null_for_method_for_null_valued_proceedingoinpoint() {
        AspectjProceedingJoinPointContextProvider givenAspectjProceedingJoinPointContextProvider = new AspectjProceedingJoinPointContextProvider();
        givenAspectjProceedingJoinPointContextProvider.setContextData(null);

        String result = givenAspectjProceedingJoinPointContextProvider.getMethod();

        MatcherAssert.assertThat(result, Matchers.nullValue());
    }


    @Test
    public void should_return_null_for_parameters_for_null_valued_proceedingoinpoint() {
        AspectjProceedingJoinPointContextProvider givenAspectjProceedingJoinPointContextProvider = new AspectjProceedingJoinPointContextProvider();
        givenAspectjProceedingJoinPointContextProvider.setContextData(null);

        List<String> result = givenAspectjProceedingJoinPointContextProvider.getParameters();

        MatcherAssert.assertThat(result, Matchers.nullValue());
    }


    @Test
    public void should_return_null_for_instance_for_null_valued_proceedingoinpoint() {
        AspectjProceedingJoinPointContextProvider givenAspectjProceedingJoinPointContextProvider = new AspectjProceedingJoinPointContextProvider();
        givenAspectjProceedingJoinPointContextProvider.setContextData(null);

        String result = givenAspectjProceedingJoinPointContextProvider.getDeserializedInstance();

        MatcherAssert.assertThat(result, Matchers.nullValue());
    }


    @Test
    public void should_return_class_for_null_valid_proceedingoinpoint() {

        ProceedingJoinPoint proceedingJoinPoint = Mockito.mock(ProceedingJoinPoint.class);
        Signature signature = Mockito.mock(Signature.class);
        Mockito.when(proceedingJoinPoint.getSignature()).thenReturn(signature);
        Mockito.when(signature.getDeclaringTypeName()).thenReturn(AspectjProceedingJoinPointContextProviderTest.class.getCanonicalName());

        AspectjProceedingJoinPointContextProvider givenAspectjProceedingJoinPointContextProvider = new AspectjProceedingJoinPointContextProvider();
        givenAspectjProceedingJoinPointContextProvider.setContextData(proceedingJoinPoint);

        String result = givenAspectjProceedingJoinPointContextProvider.getClazz();

        MatcherAssert.assertThat(result, Matchers.equalTo(AspectjProceedingJoinPointContextProviderTest.class.getCanonicalName()));
    }


    @Test
    public void should_return_for_method_for_valid_proceedingoinpoint() {

        ProceedingJoinPoint proceedingJoinPoint = Mockito.mock(ProceedingJoinPoint.class);
        Signature signature = Mockito.mock(Signature.class);
        Mockito.when(proceedingJoinPoint.getSignature()).thenReturn(signature);
        Mockito.when(signature.getName()).thenReturn(METHOD.getName());

        AspectjProceedingJoinPointContextProvider givenAspectjProceedingJoinPointContextProvider = new AspectjProceedingJoinPointContextProvider();
        givenAspectjProceedingJoinPointContextProvider.setContextData(proceedingJoinPoint);

        String result = givenAspectjProceedingJoinPointContextProvider.getMethod();

        MatcherAssert.assertThat(result, Matchers.notNullValue());
        MatcherAssert.assertThat(result, Matchers.equalTo(METHOD.getName()));
    }


    @Test
    public void should_return_parameters_for_valid_proceedingoinpoint() {

        final Object[] PARAMETERS = {"a", 2};

        ProceedingJoinPoint proceedingJoinPoint = Mockito.mock(ProceedingJoinPoint.class);
        Mockito.when(proceedingJoinPoint.getArgs()).thenReturn(PARAMETERS);

        AspectjProceedingJoinPointContextProvider givenAspectjProceedingJoinPointContextProvider = new AspectjProceedingJoinPointContextProvider();
        givenAspectjProceedingJoinPointContextProvider.setContextData(proceedingJoinPoint);

        List<String> result = givenAspectjProceedingJoinPointContextProvider.getParameters();

        MatcherAssert.assertThat(result, Matchers.notNullValue());
        MatcherAssert.assertThat(result.size(), Matchers.equalTo(2));
        MatcherAssert.assertThat(result, Matchers.contains("a", "2"));
    }


    @Test
    public void should_return_instance_for_valid_proceedingoinpoint() {

        ProceedingJoinPoint proceedingJoinPoint = Mockito.mock(ProceedingJoinPoint.class);
        Mockito.when(proceedingJoinPoint.getTarget()).thenReturn(this);

        AspectjProceedingJoinPointContextProvider givenAspectjProceedingJoinPointContextProvider = new AspectjProceedingJoinPointContextProvider();
        givenAspectjProceedingJoinPointContextProvider.setContextData(proceedingJoinPoint);

        String result = givenAspectjProceedingJoinPointContextProvider.getDeserializedInstance();

        MatcherAssert.assertThat(result, Matchers.notNullValue());
        MatcherAssert.assertThat(result, Matchers.startsWith("AspectjProceedingJoinPointContextProviderTest@"));
    }


}
