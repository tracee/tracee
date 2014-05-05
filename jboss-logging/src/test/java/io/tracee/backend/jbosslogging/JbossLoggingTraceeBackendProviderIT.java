package io.tracee.backend.jbosslogging;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import org.jboss.logging.MDC;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;

public class JbossLoggingTraceeBackendProviderIT {

	@Test
	public void testLoadProviderAndStoreToJbossLoggingMdc() {
		final TraceeBackend context = Tracee.getBackend();
		context.put("FOO", "BAR");
		assertThat(MDC.get("FOO"), equalTo((Object) "BAR"));
		context.remove("FOO");
		assertThat(MDC.get("FOO"), nullValue());
	}

}
