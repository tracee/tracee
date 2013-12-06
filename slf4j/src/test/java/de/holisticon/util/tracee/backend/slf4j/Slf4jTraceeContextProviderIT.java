package de.holisticon.util.tracee.backend.slf4j;

import de.holisticon.util.tracee.Tracee;
import de.holisticon.util.tracee.TraceeBackend;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Daniel
 */
public class Slf4jTraceeContextProviderIT {


    private static final Logger log = LoggerFactory.getLogger(Slf4jTraceeContextProviderIT.class);

    @Test
    public void testLoadProviderAndStoreToSlf4jMdc() {
        final TraceeBackend context = Tracee.getBackend();
        context.put("FOO", "BAR");
        assertThat(MDC.get("FOO"), equalTo("BAR"));
        log.debug("Hi");
    }

}
