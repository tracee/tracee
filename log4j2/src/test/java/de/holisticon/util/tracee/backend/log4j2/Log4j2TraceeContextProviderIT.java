package de.holisticon.util.tracee.backend.log4j2;

import de.holisticon.util.tracee.Tracee;
import de.holisticon.util.tracee.TraceeBackend;
import org.apache.logging.log4j.ThreadContext;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
public class Log4j2TraceeContextProviderIT {

    @Test
    public void testLoadProviderAndStoreToLog4j2ThreadContext() {
        final TraceeBackend backend = Tracee.getBackend();
		backend.put("FOO", "BAR");
        assertThat(ThreadContext.get("FOO"), equalTo("BAR"));
		backend.remove("FOO");
		assertThat(ThreadContext.get("FOO"), nullValue());
    }

}
