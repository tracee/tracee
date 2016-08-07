package io.tracee.binding.quartz;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import io.tracee.configuration.PropertiesBasedTraceeFilterConfiguration;
import io.tracee.configuration.TraceeFilterConfiguration;
import io.tracee.configuration.TraceeFilterConfiguration.Profile;
import io.tracee.testhelper.FieldAccessUtil;
import io.tracee.testhelper.PermitAllTraceeFilterConfiguration;
import io.tracee.testhelper.SimpleTraceeBackend;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TraceeJobListenerTest {

	private final TraceeBackend backend = spy(new SimpleTraceeBackend());
	private final TraceeFilterConfiguration filterConfiguration = new PermitAllTraceeFilterConfiguration();

	private final TraceeJobListener unit = new TraceeJobListener(backend, filterConfiguration);

	@Test
	public void generateRequestIdWhenJobStarts() {
		final JobExecutionContext executionContext = mock(JobExecutionContext.class);
		when(executionContext.getMergedJobDataMap()).thenReturn(new JobDataMap());
		unit.jobToBeExecuted(executionContext);
		assertThat(backend.copyToMap(), Matchers.hasKey(TraceeConstants.INVOCATION_ID_KEY));
		verify(backend).put(eq(TraceeConstants.INVOCATION_ID_KEY), anyString());
	}

	@Test
	public void addContextToTraceeBackend() {
		final Map<String, String> traceeContext = new HashMap<>();
		final JobExecutionContext executionContext = mock(JobExecutionContext.class);
		final JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put(TraceeConstants.TPIC_HEADER, traceeContext);
		when(executionContext.getMergedJobDataMap()).thenReturn(jobDataMap);
		traceeContext.put("test", "testVal");
		unit.jobToBeExecuted(executionContext);
		assertThat(backend.get("test"), is("testVal"));
	}

	@Test
	public void defaultConstructorUsesDefaultProfile() {
		final TraceeJobListener listener = new TraceeJobListener();
		assertThat((TraceeFilterConfiguration) FieldAccessUtil.getFieldVal(listener, "filterConfiguration"), sameInstance(PropertiesBasedTraceeFilterConfiguration.instance().DEFAULT));
	}

	@Test
	public void defaultConstructorUsesTraceeBackend() {
		final TraceeJobListener listener = new TraceeJobListener();
		assertThat((TraceeBackend) FieldAccessUtil.getFieldVal(listener, "backend"), sameInstance(Tracee.getBackend()));
	}

	@Test
	public void constructorStoresProfileNameInternal() {
		final TraceeJobListener listener = new TraceeJobListener("testProf");
		assertThat((TraceeFilterConfiguration) FieldAccessUtil.getFieldVal(listener, "filterConfiguration"), sameInstance(PropertiesBasedTraceeFilterConfiguration.instance().forProfile("testProf")));
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

	@Test
	public void returnTheNameOfTheListener() {
		assertThat(unit.getName(), is("TracEE job listener"));
	}
}
