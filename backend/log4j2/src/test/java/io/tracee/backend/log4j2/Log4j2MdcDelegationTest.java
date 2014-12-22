package io.tracee.backend.log4j2;


import io.tracee.ThreadLocalHashSet;
import org.apache.logging.log4j.ThreadContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ThreadContext.class)
public class Log4j2MdcDelegationTest {

	private final ThreadLocalHashSet<String> traceeKeys = new ThreadLocalHashSet<String>();

	private final Log4j2TraceeBackend unit = new Log4j2TraceeBackend(traceeKeys);

	@Before
	public void setup() {
		traceeKeys.get().add("BB");
		PowerMockito.mockStatic(ThreadContext.class);
	}

	@Test
	public void shouldPutToMDC() {
		unit.put("A", "vA");
		PowerMockito.verifyStatic();
		ThreadContext.put("A", "vA");
	}

	@Test
	public void shouldReturnTrueIfKeyIsInMDC() {
		when(ThreadContext.get("BB")).thenReturn("vBB");
		assertThat(unit.containsKey("BB"), is(true));
	}

	@Test
	public void shouldReturnValueFromMDC() {
		when(ThreadContext.get("BB")).thenReturn("vBB");
		assertThat(unit.get("BB"), equalTo("vBB"));
	}

	@Test
	public void shouldCallRemoveOnMDC() {
		unit.remove("BB");
		PowerMockito.verifyStatic();
		ThreadContext.remove("BB");
	}
}
