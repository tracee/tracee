package io.tracee.binding.quartz;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.configuration.PropertiesBasedTraceeFilterConfiguration;
import io.tracee.configuration.TraceeFilterConfiguration;
import io.tracee.testhelper.FieldAccessUtil;
import io.tracee.testhelper.PermitAllTraceeFilterConfiguration;
import io.tracee.testhelper.SimpleTraceeBackend;
import org.junit.Test;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

import java.util.Map;

import static io.tracee.TraceeConstants.TPIC_HEADER;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SuppressWarnings("unchecked")
public class TraceeContextInjectorTest {

	private final TraceeBackend backend = new SimpleTraceeBackend();
	private final TraceeFilterConfiguration filterConfiguration = PermitAllTraceeFilterConfiguration.INSTANCE;

	private TraceeContextInjector unit = new TraceeContextInjector(backend, filterConfiguration);

	@Test
	public void skipTpicProcessingIfBackendIsEmpty() {
		final JobDataMap dataMap = new JobDataMap();
		unit.injectContext(dataMap);
		assertThat(dataMap.size(), is(0));
	}

	@Test
	public void addTpicHeaderToJobDataMapIfBackendContainsValues() {
		backend.put("testKey", "testValue");
		final JobDataMap dataMap = new JobDataMap();
		unit.injectContext(dataMap);
		assertThat(dataMap, hasKey(TPIC_HEADER));
		assertThat((Map<String, String>) dataMap.get(TPIC_HEADER), hasEntry("testKey", "testValue"));
	}

	@Test
	public void addTpicHeaderToJobDataMapOfTriggerIfBackendContainsValues() {
		backend.put("testKey", "testValue");
		final Trigger trigger = TriggerBuilder.newTrigger().build();
		unit.injectContext(trigger);
		assertThat(trigger.getJobDataMap(), hasKey(TPIC_HEADER));
		assertThat((Map<String, String>) trigger.getJobDataMap().get(TPIC_HEADER), hasEntry("testKey", "testValue"));
	}

	@Test
	public void addTpicHeaderToJobDataMapOfJobDetailsIfBackendContainsValues() {
		backend.put("testKey", "testValue");
		final JobDetail jobDetail = JobBuilder.newJob(NoopJob.class).build();
		unit.injectContext(jobDetail);
		assertThat(jobDetail.getJobDataMap(), hasKey(TPIC_HEADER));
		assertThat((Map<String, String>) jobDetail.getJobDataMap().get(TPIC_HEADER), hasEntry("testKey", "testValue"));
	}

	@Test
	public void defaultConstructorUsesDefaultProfile() {
		final TraceeContextInjector injector = new TraceeContextInjector();
		assertThat((TraceeFilterConfiguration) FieldAccessUtil.getFieldVal(injector, "filterConfiguration"), sameInstance(PropertiesBasedTraceeFilterConfiguration.instance().DEFAULT));
	}

	@Test
	public void defaultConstructorUsesTraceeBackend() {
		final TraceeContextInjector injector = new TraceeContextInjector();
		assertThat((TraceeBackend) FieldAccessUtil.getFieldVal(injector, "backend"), sameInstance(Tracee.getBackend()));
	}

	@Test
	public void constructorStoresProfileNameInternal() {
		final TraceeContextInjector injector = new TraceeContextInjector("testProf");
		assertThat((TraceeFilterConfiguration) FieldAccessUtil.getFieldVal(injector, "filterConfiguration"), sameInstance(PropertiesBasedTraceeFilterConfiguration.instance().forProfile("testProf")));
	}

	static class NoopJob implements Job {
		@Override
		public void execute(JobExecutionContext context) throws JobExecutionException {
			// noop
		}
	}
}
