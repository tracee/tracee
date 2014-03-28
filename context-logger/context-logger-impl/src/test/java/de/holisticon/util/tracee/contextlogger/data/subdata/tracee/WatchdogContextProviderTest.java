package de.holisticon.util.tracee.contextlogger.data.subdata.tracee;

import de.holisticon.util.tracee.contextlogger.RegexMatcher;
import de.holisticon.util.tracee.contextlogger.builder.TraceeContextLogger;
import de.holisticon.util.tracee.contextlogger.data.wrapper.WatchdogDataWrapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.mockito.Mockito;

import org.aspectj.lang.Signature;

/**
 * Created by TGI on 28.03.14.
 */
public class WatchdogContextProviderTest {

    @Test
    public void should_generate_json() {

        final Object[] ARGS = {"ads",3,3.5};

        ProceedingJoinPoint proceedingJoinPoint = Mockito.mock(ProceedingJoinPoint.class);
        Signature signature = Mockito.mock(Signature.class);
        Mockito.when(proceedingJoinPoint.getArgs()).thenReturn(ARGS);
        Mockito.when(proceedingJoinPoint.getSignature()).thenReturn(signature);
        Mockito.when(signature.getName()).thenReturn("NAME");

        String result = TraceeContextLogger.createDefault().createJson(WatchdogDataWrapper.wrap("ID", proceedingJoinPoint));

        MatcherAssert.assertThat(result, RegexMatcher.matches("\\{\\\"watchdog\\\":\\{\\\"id\\\":\\\"ID\\\",\\\"aspectj.proceedingJoinPoint\\\":\\{\\\"method\\\":\\\"NAME\\\",\\\"parameters\\\":\\[\\\"ads\\\",\\\"3\\\",\\\"3.5\\\"\\]\\}\\}\\}"));


    }


}
