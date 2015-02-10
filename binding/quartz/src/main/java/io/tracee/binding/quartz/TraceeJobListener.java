package io.tracee.binding.quartz;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import io.tracee.Utilities;
import io.tracee.configuration.TraceeFilterConfiguration;
import io.tracee.configuration.TraceeFilterConfiguration.Profile;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.listeners.JobListenerSupport;

import java.util.Map;

import static io.tracee.configuration.TraceeFilterConfiguration.Channel.AsyncDispatch;
import static io.tracee.configuration.TraceeFilterConfiguration.Channel.AsyncProcess;

public class TraceeJobListener extends JobListenerSupport {

	private final TraceeBackend backend;

	private final String profile;

	public TraceeJobListener() {
		this(Tracee.getBackend(), Profile.DEFAULT);
	}

	public TraceeJobListener(final String profile) {
		this(Tracee.getBackend(), profile);
	}

	TraceeJobListener(final TraceeBackend backend, final String profile) {
		this.backend = backend;
		this.profile = profile;
	}

	@Override
	public String getName() {
		return "TracEE job listener";
	}

	@Override
	public void jobToBeExecuted(JobExecutionContext context) {
		final TraceeFilterConfiguration configuration = backend.getConfiguration(profile);

		if (configuration.shouldProcessContext(AsyncProcess)) {
			final Map<String, String> traceeContext = (Map<String, String>) context.getMergedJobDataMap().get(TraceeConstants.TPIC_HEADER);

			if (traceeContext != null && !traceeContext.isEmpty()) {
				final Map<String, String> filteredContext = configuration.filterDeniedParams(traceeContext, AsyncProcess);
				backend.putAll(filteredContext);
			}
		}

		Utilities.generateInvocationIdIfNecessary(backend);
	}

	@Override
	public void jobExecutionVetoed(JobExecutionContext context) {
		backend.clear();
	}

	@Override
	public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
		backend.clear();
	}
}
