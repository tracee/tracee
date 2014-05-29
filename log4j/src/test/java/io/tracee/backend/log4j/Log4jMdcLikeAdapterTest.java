package io.tracee.backend.log4j;

import org.apache.log4j.MDC;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Hashtable;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(MDC.class)
public class Log4jMdcLikeAdapterTest {


	private final Log4jMdcLikeAdapter unit = new Log4jMdcLikeAdapter();

	@Before
	public void setupMocks() {
		PowerMockito.mockStatic(MDC.class);
	}

	@Test
	public void shouldPutToMDC() {
		unit.put("A", "vA");
		PowerMockito.verifyStatic();
		MDC.put("A", "vA");
	}

	@Test
	public void shouldReturnTrueIfValueIsInMDC() {
		when(MDC.get("BB")).thenReturn("vBB");
		assertThat(unit.containsKey("BB"), is(true));
	}

	@Test
	public void shouldReturnValueFromMDC() {
		when(MDC.get("BB")).thenReturn("vBB");
		assertThat(unit.get("BB"), equalTo("vBB"));
	}

	@Test
	public void shouldCallRemoveOnMDC() {
		unit.remove("BB");
		PowerMockito.verifyStatic();
		MDC.remove("BB");
	}

	@Test
	public void shouldGetCopyOfMdc() {
		final Hashtable<String, String> contextMap = new Hashtable<String, String>();
		contextMap.put("A", "vA");
		contextMap.put("B", "vB");
		when(MDC.getContext()).thenReturn(contextMap);

		final Map<String, String> copyOfContext = unit.getCopyOfContext();
		assertThat(contextMap, equalTo(copyOfContext));
		assertThat(contextMap, is(not(sameInstance(copyOfContext))));
	}

}