package io.tracee.binding.quartz;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import io.tracee.configuration.PropertiesBasedTraceeFilterConfiguration;
import io.tracee.configuration.TraceeFilterConfiguration;
import io.tracee.configuration.TraceeFilterConfiguration.Profile;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Trigger;

import static io.tracee.configuration.TraceeFilterConfiguration.Channel.AsyncDispatch;

/**
 * This class injects the current TPIC into the `JobDataMap` at the time when a dynamic generated job is scheduled.
 */
public class TraceeContextInjector {

	private final TraceeBackend backend;

	private final TraceeFilterConfiguration filterConfiguration;

	/**
	 * @deprecated Use full constructor
	 */
	@Deprecated
	public TraceeContextInjector() {
		this(Tracee.getBackend(), PropertiesBasedTraceeFilterConfiguration.instance().DEFAULT);
	}

	/**
	 * @deprecated Use full constructor
	 */
	@Deprecated
	public TraceeContextInjector(final String profile) {
		this(Tracee.getBackend(), PropertiesBasedTraceeFilterConfiguration.instance().forProfile(profile));
	}

	public TraceeContextInjector(final TraceeBackend backend, TraceeFilterConfiguration filterConfiguration) {
		this.backend = backend;
		this.filterConfiguration = filterConfiguration;
	}

	public void injectContext(Trigger trigger) {
		injectContext(trigger.getJobDataMap());
	}

	public void injectContext(JobDetail jobDetail) {
		injectContext(jobDetail.getJobDataMap());
	}

	public void injectContext(JobDataMap jobDataMap) {

		if (!backend.isEmpty() && filterConfiguration.shouldProcessContext(AsyncDispatch)) {
			jobDataMap.put(TraceeConstants.TPIC_HEADER, filterConfiguration.filterDeniedParams(backend.copyToMap(), AsyncDispatch));
		}
	}
}
