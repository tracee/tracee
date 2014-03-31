package de.holisticon.util.tracee.contextlogger.data.subdata.tracee;

import de.holisticon.util.tracee.contextlogger.ImplicitContext;
import de.holisticon.util.tracee.contextlogger.TraceeContextLoggerConstants;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

/**
 * Test class for {@link CommonDataContextProvider}.
 * Created by Tobias Gindler, holisticon AG on 31.03.14.
 */
public class CommonDataProviderTest {

    public static final String STAGE = "STAGE";
    public static final String SYSTEM = "SYSTEM";

    @Before
    public void init() {
        System.setProperty(TraceeContextLoggerConstants.SYSTEM_PROPERTY_NAME_STAGE, STAGE);
        System.setProperty(TraceeContextLoggerConstants.SYSTEM_PROPERTY_NAME_SYSTEM, SYSTEM);
    }

    @Test
    public void should_return_implicit_context() {
        ImplicitContext implicitContext = new CommonDataContextProvider().getImplicitContext();

        MatcherAssert.assertThat(implicitContext, Matchers.notNullValue());
        MatcherAssert.assertThat(implicitContext, Matchers.equalTo(ImplicitContext.COMMON));
    }


    @Test
    public void should_get_time() {
        Date result = new CommonDataContextProvider().getTimestamp();
        MatcherAssert.assertThat(result, Matchers.notNullValue());
    }

    @Test
    public void should_get_stage() {
        String result = new CommonDataContextProvider().getStage();
        MatcherAssert.assertThat(result, Matchers.notNullValue());
        MatcherAssert.assertThat(result, Matchers.equalTo(STAGE));
    }

    @Test
    public void should_get_null_for_not_set_stage() {
        System.getProperties().remove(TraceeContextLoggerConstants.SYSTEM_PROPERTY_NAME_STAGE);
        String result = new CommonDataContextProvider().getStage();
        MatcherAssert.assertThat(result, Matchers.nullValue());
    }

    @Test
    public void should_get_system() {
        String result = new CommonDataContextProvider().getSystemName();
        MatcherAssert.assertThat(result, Matchers.notNullValue());
        MatcherAssert.assertThat(result, Matchers.equalTo(SYSTEM));
    }

    @Test
    public void should_get_null_for_not_set_system() {
        System.getProperties().remove(TraceeContextLoggerConstants.SYSTEM_PROPERTY_NAME_SYSTEM);
        String result = new CommonDataContextProvider().getSystemName();
        MatcherAssert.assertThat(result, Matchers.nullValue());
    }

    @Test
    public void should_get_thread_name() {
        String expected = Thread.currentThread().getName();
        String result = new CommonDataContextProvider().getThreadName();
        MatcherAssert.assertThat(result, Matchers.notNullValue());
        MatcherAssert.assertThat(result, Matchers.equalTo(expected));
    }

    @Test
    public void should_get_thread_id() {
        Long expected = Thread.currentThread().getId();
        Long result = new CommonDataContextProvider().getThreadId();
        MatcherAssert.assertThat(result, Matchers.notNullValue());
        MatcherAssert.assertThat(result, Matchers.equalTo(expected));
    }


}
