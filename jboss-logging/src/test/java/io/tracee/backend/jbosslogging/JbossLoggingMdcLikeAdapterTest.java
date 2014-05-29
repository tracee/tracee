package io.tracee.backend.jbosslogging;

import org.jboss.logging.MDC;
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
@PrepareForTest(MDC.class)
public class JbossLoggingMdcLikeAdapterTest {

	private final JbossLoggingMdcLikeAdapter unit = new JbossLoggingMdcLikeAdapter();

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
	public void shouldReturnTrueIfKeyIsInMDC() {
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
		final Map<String, Object> contextMap = new HashMap<String, Object>();
		contextMap.put("A", "vA");
		contextMap.put("B", "vB");
		when(MDC.getMap()).thenReturn(contextMap);

		final Map<String, String> copyOfContext = unit.getCopyOfContext();
		assertThat(copyOfContext.size(), is(2));
		assertThat(copyOfContext.keySet(), containsInAnyOrder("A", "B"));
		assertThat(copyOfContext.values(), containsInAnyOrder((Object) "vA", "vB"));
	}
}