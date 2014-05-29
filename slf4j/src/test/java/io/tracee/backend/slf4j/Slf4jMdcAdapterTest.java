package io.tracee.backend.slf4j;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.MDC;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(MDC.class)
public class Slf4jMdcAdapterTest {

	private final Slf4jMdcAdapter unit = new Slf4jMdcAdapter();

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
	public void shouldCallCopyMethodOnSlf4JMDC() {
		unit.getCopyOfContext();
		PowerMockito.verifyStatic();
		MDC.getCopyOfContextMap();
	}
}