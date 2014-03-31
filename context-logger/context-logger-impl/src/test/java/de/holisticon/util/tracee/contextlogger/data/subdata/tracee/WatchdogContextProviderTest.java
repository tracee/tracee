package de.holisticon.util.tracee.contextlogger.data.subdata.tracee;

import de.holisticon.util.tracee.contextlogger.RegexMatcher;
import de.holisticon.util.tracee.contextlogger.builder.TraceeContextLogger;
import de.holisticon.util.tracee.contextlogger.data.subdata.aspectj.AspectjProceedingJoinPointContextProvider;
import de.holisticon.util.tracee.contextlogger.data.wrapper.WatchdogDataWrapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Test class for {@link de.holisticon.util.tracee.contextlogger.data.subdata.tracee.WatchdogContextProvider}.
 * Created by Tobias Gindler, holisticon AG on 28.03.14.
 */
public class WatchdogContextProviderTest {

    private static final Object[] ARGS = {"ads", 3, 3.5};

    private ProceedingJoinPoint proceedingJoinPoint;
    private Signature signature;

    @Before
    public void init() {

        proceedingJoinPoint = Mockito.mock(ProceedingJoinPoint.class);
        signature = Mockito.mock(Signature.class);
        Mockito.when(proceedingJoinPoint.getArgs()).thenReturn(ARGS);
        Mockito.when(proceedingJoinPoint.getSignature()).thenReturn(signature);
        Mockito.when(signature.getName()).thenReturn("NAME");
    }

    @Test
    public void should_generate_json() {

        String result = TraceeContextLogger.createDefault().createJson(WatchdogDataWrapper.wrap("ID", proceedingJoinPoint));

        MatcherAssert.assertThat(result, RegexMatcher.matches("\\{\\\"watchdog\\\":\\{\\\"id\\\":\\\"ID\\\",\\\"aspectj.proceedingJoinPoint\\\":\\{\\\"method\\\":\\\"NAME\\\",\\\"parameters\\\":\\[\\\"ads\\\",\\\"3\\\",\\\"3.5\\\"\\]\\}\\}\\}"));


    }

    @Test
    public void should_return_null_for_id_if_wrapped_instance_is_null() {

        WatchdogContextProvider givenWatchdogContextProvider = new WatchdogContextProvider(WatchdogDataWrapper.wrap(null, proceedingJoinPoint));

        String result = givenWatchdogContextProvider.getId();

        MatcherAssert.assertThat(result, Matchers.nullValue());

    }

    @Test
    public void should_return_id() {

        final String RESULT = "ID";

        WatchdogContextProvider givenWatchdogContextProvider = new WatchdogContextProvider(WatchdogDataWrapper.wrap(RESULT, proceedingJoinPoint));

        String result = givenWatchdogContextProvider.getId();

        MatcherAssert.assertThat(result, Matchers.equalTo(RESULT));

    }

    @Test
    public void should_return_null_for_proceedingjoinpoint_if_wrapped_instance_is_null() {

        WatchdogContextProvider givenWatchdogContextProvider = new WatchdogContextProvider(WatchdogDataWrapper.wrap("ID", null));

        AspectjProceedingJoinPointContextProvider result = givenWatchdogContextProvider.getProceedingJoinPoint();

        MatcherAssert.assertThat(result, Matchers.nullValue());

    }

    @Test
    public void should_return_proceedingjoinpoint_provider() {

        WatchdogContextProvider givenWatchdogContextProvider = new WatchdogContextProvider(WatchdogDataWrapper.wrap("ID", proceedingJoinPoint));

        AspectjProceedingJoinPointContextProvider result = givenWatchdogContextProvider.getProceedingJoinPoint();

        MatcherAssert.assertThat(result, Matchers.notNullValue());

    }

    @Test
    public void should_return_wrapped_type() {

        WatchdogContextProvider givenWatchdogContextProvider = new WatchdogContextProvider(WatchdogDataWrapper.wrap("ID", null));

        Class result = givenWatchdogContextProvider.getWrappedType();

        MatcherAssert.assertThat(result.equals(WatchdogDataWrapper.class), Matchers.equalTo(true));

    }


}
