package io.tracee.contextlogger.contextprovider.tracee;

import io.tracee.contextlogger.RegexMatcher;
import io.tracee.contextlogger.builder.TraceeContextLogger;
import io.tracee.contextlogger.contextprovider.aspectj.WatchdogDataWrapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link WatchdogContextProvider}.
 * Created by Tobias Gindler, holisticon AG on 28.03.14.
 */
public class WatchdogContextProviderTest {

	private static final Object[] ARGS = {"ads", 3, 3.5};

	private ProceedingJoinPoint proceedingJoinPoint;

	@Before
	public void init() {
		proceedingJoinPoint = Mockito.mock(ProceedingJoinPoint.class);
		Signature signature = Mockito.mock(Signature.class);
		when(proceedingJoinPoint.getArgs()).thenReturn(ARGS);
		when(proceedingJoinPoint.getSignature()).thenReturn(signature);
		when(signature.getName()).thenReturn("NAME");
	}

	@Test
	public void should_generate_json() {
		String result = TraceeContextLogger.createDefault().createJson(WatchdogDataWrapper.wrap("ID", proceedingJoinPoint));

		assertThat(result, RegexMatcher.matches("\\{\\\"watchdog\\\":\\{\\\"id\\\":\\\"ID\\\",\\\"aspectj.proceedingJoinPoint\\\":\\{\\\"method\\\":\\\"NAME\\\",\\\"parameters\\\":\\[\\\"ads\\\",\\\"3\\\",\\\"3.5\\\"\\]\\}\\}\\}"));
	}

	@Test
	public void should_return_null_for_id_if_wrapped_instance_is_null() {
		final WatchdogContextProvider unit = new WatchdogContextProvider(WatchdogDataWrapper.wrap(null, proceedingJoinPoint));

		assertThat(unit.getId(), nullValue());
	}

	@Test
	public void should_return_id() {
		final String RESULT = "ID";

		final WatchdogContextProvider unit = new WatchdogContextProvider(WatchdogDataWrapper.wrap(RESULT, proceedingJoinPoint));

		assertThat(unit.getId(), equalTo(RESULT));
	}

	@Test
	public void should_return_null_for_proceedingjoinpoint_if_wrapped_instance_is_null() {
		final WatchdogContextProvider unit = new WatchdogContextProvider(WatchdogDataWrapper.wrap("ID", null));

		assertThat(unit.getProceedingJoinPoint(), nullValue());
	}

	@Test
	public void should_return_proceedingjoinpoint_provider() {
		final WatchdogContextProvider unit = new WatchdogContextProvider(WatchdogDataWrapper.wrap("ID", proceedingJoinPoint));

		assertThat(unit.getProceedingJoinPoint(), notNullValue());
	}

	@Test
	public void should_return_wrapped_type() {
		final WatchdogContextProvider unit = new WatchdogContextProvider(WatchdogDataWrapper.wrap("ID", null));

		assertThat(unit.getWrappedType(), equalTo(WatchdogDataWrapper.class));
	}
}
