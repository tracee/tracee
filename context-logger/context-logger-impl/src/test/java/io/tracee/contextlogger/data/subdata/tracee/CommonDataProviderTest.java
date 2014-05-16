package io.tracee.contextlogger.data.subdata.tracee;

import io.tracee.contextlogger.ImplicitContext;
import io.tracee.contextlogger.TraceeContextLoggerConstants;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Date;

/**
 * Test class for {@link CommonDataContextProvider}.
 * Created by Tobias Gindler, holisticon AG on 31.03.14.
 */
public class CommonDataProviderTest {

    public static final String STAGE = "STAGE";
    public static final String SYSTEM = "SYSTEM";

    CommonDataContextProvider commonDataContextProvider;

    @Before
    public void init() {

        commonDataContextProvider = Mockito.spy(new CommonDataContextProvider());

    }

    @Test
    public void should_return_implicit_context() {

        setSystemProperties(true, true);

        ImplicitContext implicitContext = commonDataContextProvider.getImplicitContext();

        MatcherAssert.assertThat(implicitContext, Matchers.notNullValue());
        MatcherAssert.assertThat(implicitContext, Matchers.equalTo(ImplicitContext.COMMON));
    }


    @Test
    public void should_get_time() {

        setSystemProperties(true, true);
        Date result = commonDataContextProvider.getTimestamp();
        MatcherAssert.assertThat(result, Matchers.notNullValue());
    }

    @Test
    public void should_get_stage() {

        setSystemProperties(true, true);

        String result = commonDataContextProvider.getStage();
        MatcherAssert.assertThat(result, Matchers.notNullValue());
        MatcherAssert.assertThat(result, Matchers.equalTo(STAGE));
    }

    @Test
    public void should_get_null_for_not_set_stage() {

        setSystemProperties(false, true);

        String result = commonDataContextProvider.getStage();
        MatcherAssert.assertThat(result, Matchers.nullValue());
    }

    @Test
    public void should_get_system() {

        setSystemProperties(true, true);

        String result = commonDataContextProvider.getSystemName();
        MatcherAssert.assertThat(result, Matchers.notNullValue());
        MatcherAssert.assertThat(result, Matchers.equalTo(SYSTEM));
    }

    @Test
    public void should_get_null_for_not_set_system() {
        setSystemProperties(true,false);

        String result = commonDataContextProvider.getSystemName();
        MatcherAssert.assertThat(result, Matchers.nullValue());
    }

    @Test
    public void should_get_thread_name() {
        setSystemProperties(true,true);
        String expected = Thread.currentThread().getName();
        String result = commonDataContextProvider.getThreadName();
        MatcherAssert.assertThat(result, Matchers.notNullValue());
        MatcherAssert.assertThat(result, Matchers.equalTo(expected));
    }

    @Test
    public void should_get_thread_id() {
        setSystemProperties(true,true);
        Long expected = Thread.currentThread().getId();
        Long result = commonDataContextProvider.getThreadId();
        MatcherAssert.assertThat(result, Matchers.notNullValue());
        MatcherAssert.assertThat(result, Matchers.equalTo(expected));
    }

    private void setSystemProperties (boolean stage, boolean system) {
        Mockito.doReturn(stage ? STAGE : null).when(commonDataContextProvider).getSystemProperty(TraceeContextLoggerConstants.SYSTEM_PROPERTY_NAME_STAGE);
        Mockito.doReturn(system ? SYSTEM : null).when(commonDataContextProvider).getSystemProperty(TraceeContextLoggerConstants.SYSTEM_PROPERTY_NAME_SYSTEM);
    }

}
