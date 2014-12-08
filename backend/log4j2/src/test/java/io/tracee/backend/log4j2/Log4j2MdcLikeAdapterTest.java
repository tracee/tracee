package io.tracee.backend.log4j2;


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
public class Log4j2MdcLikeAdapterTest {

	private final Log4j2MdcLikeAdapter unit = new Log4j2MdcLikeAdapter();

	@Before
	public void setupMocks() {
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

	@Test
	public void shouldGetCopyOfThreadContext() {
		final Map<String, String> contextMap = new HashMap<String, String>();
		contextMap.put("A", "vA");
		contextMap.put("B", "vB");
		when(ThreadContext.getContext()).thenReturn(contextMap);

		final Map<String, String> copyOfContext = unit.getCopyOfContext();
		assertThat(contextMap, equalTo(copyOfContext));
		assertThat(contextMap, is(not(sameInstance(copyOfContext))));
	}

}