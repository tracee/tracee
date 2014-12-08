package io.tracee.contextlogger.contextprovider.javaee;

import io.tracee.contextlogger.contextprovider.utility.NameStringValuePair;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;

import javax.interceptor.InvocationContext;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Test class for {@link InvocationContextContextProvider}.
 * Created by Tobias Gindler, holiticon AG on 31.03.14.
 */
public class InvocationContextContextProviderTest {


    private static Method METHOD;

    public void test() {

    }

    {
        try {
            METHOD = InvocationContextContextProviderTest.class.getMethod("test", new Class[0]);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

    }


    @Test
    public void should_return_wrapped_type() {

        InvocationContextContextProvider givenInvocationContextContextProvider = new InvocationContextContextProvider();

        Class result = givenInvocationContextContextProvider.getWrappedType();

        MatcherAssert.assertThat(result.equals(InvocationContext.class), Matchers.equalTo(true));

    }


    @Test
    public void should_return_null_value_for_methodname_for_null_valued_invocation_context() {

        InvocationContextContextProvider givenInvocationContextContextProvider = new InvocationContextContextProvider();
        givenInvocationContextContextProvider.setContextData(null);

        String methodName = givenInvocationContextContextProvider.getMethodName();

        MatcherAssert.assertThat(methodName, Matchers.nullValue());

    }


    @Test
    public void should_return_null_value_for_parameters_for_null_valued_invocation_context() {

        InvocationContextContextProvider givenInvocationContextContextProvider = new InvocationContextContextProvider();
        givenInvocationContextContextProvider.setContextData(null);

        List<String> parameters = givenInvocationContextContextProvider.getParameters();

        MatcherAssert.assertThat(parameters, Matchers.nullValue());

    }

    @Test
    public void should_return_null_value_for_target_instance_for_null_valued_invocation_context() {

        InvocationContextContextProvider givenInvocationContextContextProvider = new InvocationContextContextProvider();
        givenInvocationContextContextProvider.setContextData(null);

        String targetInstance = givenInvocationContextContextProvider.getSerializedTargetInstance();

        MatcherAssert.assertThat(targetInstance, Matchers.nullValue());

    }

    @Test
    public void should_return_null_value_for_contextdata_for_null_valued_invocation_context() {

        InvocationContextContextProvider givenInvocationContextContextProvider = new InvocationContextContextProvider();
        givenInvocationContextContextProvider.setContextData(null);

        List<NameStringValuePair> contextData = givenInvocationContextContextProvider.getContextData();

        MatcherAssert.assertThat(contextData, Matchers.nullValue());

    }


    @Test
    public void should_return_methodname_for_valid_invocation_context() {

        InvocationContext invocationContext = Mockito.mock(InvocationContext.class);
        Mockito.when(invocationContext.getMethod()).thenReturn(METHOD);

        InvocationContextContextProvider givenInvocationContextContextProvider = new InvocationContextContextProvider();
        givenInvocationContextContextProvider.setContextData(invocationContext);

        String methodName = givenInvocationContextContextProvider.getMethodName();

        MatcherAssert.assertThat(methodName, Matchers.equalTo("test"));

    }


    @Test
    public void should_return_parameters_for_valid_invocation_context() {

        Object[] PARAMETERS = {"a", 2};

        InvocationContext invocationContext = Mockito.mock(InvocationContext.class);
        Mockito.when(invocationContext.getParameters()).thenReturn(PARAMETERS);

        InvocationContextContextProvider givenInvocationContextContextProvider = new InvocationContextContextProvider();
        givenInvocationContextContextProvider.setContextData(invocationContext);

        List<String> parameters = givenInvocationContextContextProvider.getParameters();

        MatcherAssert.assertThat(parameters, Matchers.contains("a", "2"));

    }

    @Test
    public void should_return_target_instance_for_null_valued_invocation_context() {

        InvocationContext invocationContext = Mockito.mock(InvocationContext.class);
        Mockito.when(invocationContext.getTarget()).thenReturn(this);

        InvocationContextContextProvider givenInvocationContextContextProvider = new InvocationContextContextProvider();
        givenInvocationContextContextProvider.setContextData(invocationContext);

        String targetInstance = givenInvocationContextContextProvider.getSerializedTargetInstance();

        MatcherAssert.assertThat(targetInstance, Matchers.notNullValue());
        MatcherAssert.assertThat(targetInstance, Matchers.startsWith("InvocationContextContextProviderTest@"));

    }

    @Test
    public void should_return_contextdata_for_null_valued_invocation_context() {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("a", "value");

        InvocationContext invocationContext = Mockito.mock(InvocationContext.class);
        Mockito.when(invocationContext.getContextData()).thenReturn(map);

        InvocationContextContextProvider givenInvocationContextContextProvider = new InvocationContextContextProvider();
        givenInvocationContextContextProvider.setContextData(invocationContext);

        List<NameStringValuePair> contextData = givenInvocationContextContextProvider.getContextData();

        MatcherAssert.assertThat(contextData, Matchers.notNullValue());
        MatcherAssert.assertThat(contextData.size(), Matchers.equalTo(1));
        MatcherAssert.assertThat(contextData.get(0).getName(), Matchers.equalTo("a"));
        MatcherAssert.assertThat(contextData.get(0).getValue(), Matchers.startsWith("String@"));

    }

}
