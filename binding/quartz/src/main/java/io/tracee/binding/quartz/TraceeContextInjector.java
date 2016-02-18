package io.tracee.binding.quartz;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import io.tracee.configuration.TraceeFilterConfiguration;
import io.tracee.configuration.TraceeFilterConfiguration.Profile;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Trigger;

import static io.tracee.configuration.TraceeFilterConfiguration.Channel.AsyncDispatch;

public class TraceeContextInjector {

	private final TraceeBackend backend;

	private final String profile;

	public TraceeContextInjector() {
		this(Tracee.getBackend(), Profile.DEFAULT);
	}

	public TraceeContextInjector(final String profile) {
		this(Tracee.getBackend(), profile);
	}

	TraceeContextInjector(final TraceeBackend backend, final String profile) {
		this.backend = backend;
		this.profile = profile;
	}

	public void injectContext(Trigger trigger) {
		injectContext(trigger.getJobDataMap());
	}

	public void injectContext(JobDetail jobDetail) {
		injectContext(jobDetail.getJobDataMap());
	}

	public void injectContext(JobDataMap jobDataMap) {
		final TraceeFilterConfiguration configuration = backend.getConfiguration(profile);

		if (!backend.isEmpty() && configuration.shouldProcessContext(AsyncDispatch)) {
			jobDataMap.put(TraceeConstants.TPIC_HEADER, backend.getConfiguration(profile).filterDeniedParams(backend.copyToMap(), AsyncDispatch));
		}
	}
}
