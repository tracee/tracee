package io.tracee.binding.quartz;

import io.tracee.Tracee;
import io.tracee.testhelper.FieldAccessUtil;
import io.tracee.testhelper.SimpleTraceeBackend;
import io.tracee.TraceeBackend;
import io.tracee.configuration.TraceeFilterConfiguration.Profile;
import org.junit.Test;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

import java.util.Map;

import static io.tracee.TraceeConstants.TPIC_HEADER;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;

@SuppressWarnings("unchecked")
public class TraceeContextInjectorTest {

	private final TraceeBackend backend = SimpleTraceeBackend.createNonLoggingAllPermittingBackend();

	private TraceeContextInjector unit = new TraceeContextInjector(backend, Profile.DEFAULT);

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
		final JobDetail jobDetail = JobBuilder.newJob().build();
		unit.injectContext(jobDetail);
		assertThat(jobDetail.getJobDataMap(), hasKey(TPIC_HEADER));
		assertThat((Map<String, String>) jobDetail.getJobDataMap().get(TPIC_HEADER), hasEntry("testKey", "testValue"));
	}

	@Test
	public void defaultConstructorUsesDefaultProfile() {
		final TraceeContextInjector injector = new TraceeContextInjector();
		assertThat((String) FieldAccessUtil.getFieldVal(injector, "profile"), is(Profile.DEFAULT));
	}

	@Test
	public void defaultConstructorUsesTraceeBackend() {
		final TraceeContextInjector injector = new TraceeContextInjector();
		assertThat((TraceeBackend) FieldAccessUtil.getFieldVal(injector, "backend"), is(Tracee.getBackend()));
	}

	@Test
	public void constructorStoresProfileNameInternal() {
		final TraceeContextInjector injector = new TraceeContextInjector("testProf");
		assertThat((String) FieldAccessUtil.getFieldVal(injector, "profile"), is("testProf"));
	}
}
