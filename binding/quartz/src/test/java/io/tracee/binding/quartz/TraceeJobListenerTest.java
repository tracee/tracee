package io.tracee.binding.quartz;

import io.tracee.SimpleTraceeBackend;
import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import io.tracee.configuration.TraceeFilterConfiguration.Profile;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TraceeJobListenerTest {

	private TraceeBackend backend = spy(SimpleTraceeBackend.createNonLoggingAllPermittingBackend());

	private final TraceeJobListener unit = new TraceeJobListener(backend, Profile.DEFAULT);

	@Test
	public void generateRequestIdWhenJobStarts() {
		final JobExecutionContext executionContext = mock(JobExecutionContext.class);
		when(executionContext.getMergedJobDataMap()).thenReturn(new JobDataMap());
		unit.jobToBeExecuted(executionContext);
		assertThat(backend.copyToMap(), Matchers.hasKey(TraceeConstants.INVOCATION_ID_KEY));
		verify(backend).put(eq(TraceeConstants.INVOCATION_ID_KEY), anyString());
	}

	@Test
	public void resetBackendWhenJobHasFinished() {
		unit.jobWasExecuted(mock(JobExecutionContext.class), mock(JobExecutionException.class));
		verify(backend).clear();
	}

	@Test
	public void resetBackendWhenJobHasBeenVetoed() {
		unit.jobExecutionVetoed(mock(JobExecutionContext.class));
		verify(backend).clear();
	}
}
