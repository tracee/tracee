package io.tracee.backend.log4j;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import org.apache.log4j.MDC;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;

public class Log4jTraceeContextProviderIT {


    @Test
    public void testLoadProviderAndStoreToLog4jMdc() {
        final TraceeBackend context = Tracee.getBackend();
        context.put("FOO", "BAR");
        assertThat(MDC.get("FOO"), equalTo((Object) "BAR"));
		context.remove("FOO");
		assertThat(MDC.get("FOO"), nullValue());
    }

}
