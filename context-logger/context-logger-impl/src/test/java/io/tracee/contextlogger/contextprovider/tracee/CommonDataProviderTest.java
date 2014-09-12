package io.tracee.contextlogger.contextprovider.tracee;

import io.tracee.contextlogger.api.ImplicitContext;
import io.tracee.contextlogger.TraceeContextLoggerConstants;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

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

        assertThat(implicitContext, notNullValue());
        assertThat(implicitContext, equalTo(ImplicitContext.COMMON));
    }


    @Test
    public void should_get_time() {

        setSystemProperties(true, true);
        Date result = commonDataContextProvider.getTimestamp();
        assertThat(result, notNullValue());
    }

    @Test
    public void should_get_stage() {

        setSystemProperties(true, true);

        String result = commonDataContextProvider.getStage();
        assertThat(result, notNullValue());
        assertThat(result, equalTo(STAGE));
    }

    @Test
    public void should_get_null_for_not_set_stage() {

        setSystemProperties(false, true);

        String result = commonDataContextProvider.getStage();
        assertThat(result, nullValue());
    }

    @Test
    public void should_get_system() {

        setSystemProperties(true, true);

        String result = commonDataContextProvider.getSystemName();
        assertThat(result, notNullValue());
        assertThat(result, equalTo(SYSTEM));
    }

    @Test
    public void should_get_null_for_not_set_system() throws UnknownHostException {
        setSystemProperties(true,false);

        String result = commonDataContextProvider.getSystemName();
        assertThat(result, equalTo(InetAddress.getLocalHost().getHostName()));
    }

    @Test
    public void should_get_thread_name() {
        setSystemProperties(true,true);
        String expected = Thread.currentThread().getName();
        String result = commonDataContextProvider.getThreadName();
        assertThat(result, notNullValue());
        assertThat(result, equalTo(expected));
    }

    @Test
    public void should_get_thread_id() {
        setSystemProperties(true,true);
        Long expected = Thread.currentThread().getId();
        Long result = commonDataContextProvider.getThreadId();
        assertThat(result, notNullValue());
        assertThat(result, equalTo(expected));
    }

    private void setSystemProperties (boolean stage, boolean system) {
        Mockito.doReturn(stage ? STAGE : null).when(commonDataContextProvider).getSystemProperty(TraceeContextLoggerConstants.SYSTEM_PROPERTY_NAME_STAGE);
        Mockito.doReturn(system ? SYSTEM : null).when(commonDataContextProvider).getSystemProperty(TraceeContextLoggerConstants.SYSTEM_PROPERTY_NAME_SYSTEM);
    }

}
