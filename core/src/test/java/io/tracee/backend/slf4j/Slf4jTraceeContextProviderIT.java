package io.tracee.backend.slf4j;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class Slf4jTraceeContextProviderIT {

    private static final Logger LOG = LoggerFactory.getLogger(Slf4jTraceeContextProviderIT.class);

	@Deprecated
    @Test
    public void testLoadProviderAndStoreToSlf4jMdc() {
        final TraceeBackend context = Tracee.getBackend();
        context.put("FOO", "BAR");
        assertThat(MDC.get("FOO"), equalTo("BAR"));
		LOG.debug("Hi");
    }

}
