package io.tracee.binding.quartz;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import io.tracee.Utilities;
import io.tracee.configuration.PropertiesBasedTraceeFilterConfiguration;
import io.tracee.configuration.TraceeFilterConfiguration;
import io.tracee.configuration.TraceeFilterConfiguration.Profile;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.listeners.JobListenerSupport;

import java.util.Map;

import static io.tracee.configuration.TraceeFilterConfiguration.Channel.AsyncProcess;

/**
 * Generates a invocation Id and clears the backend after the job has finished or aborted in an ungraceful way.
 * <p/>
 * This class is thread-safe.
 */
public class TraceeJobListener extends JobListenerSupport {

	private final TraceeBackend backend;

	private final TraceeFilterConfiguration filterConfiguration;

	/**
	 * @deprecated Use full constructor
	 */
	@Deprecated
	public TraceeJobListener() {
		this(Tracee.getBackend(), PropertiesBasedTraceeFilterConfiguration.instance().DEFAULT);
	}

	public TraceeJobListener(final String profile) {
		this(Tracee.getBackend(), PropertiesBasedTraceeFilterConfiguration.instance().forProfile(profile));
	}

	TraceeJobListener(final TraceeBackend backend, TraceeFilterConfiguration filterConfiguration) {
		this.backend = backend;
		this.filterConfiguration = filterConfiguration;
	}

	@Override
	public String getName() {
		return "TracEE job listener";
	}

	@Override
	public void jobToBeExecuted(JobExecutionContext context) {

		if (filterConfiguration.shouldProcessContext(AsyncProcess)) {
			@SuppressWarnings("unchecked")
			final Map<String, String> traceeContext = (Map<String, String>) context.getMergedJobDataMap().get(TraceeConstants.TPIC_HEADER);

			if (traceeContext != null && !traceeContext.isEmpty()) {
				final Map<String, String> filteredContext = filterConfiguration.filterDeniedParams(traceeContext, AsyncProcess);
				backend.putAll(filteredContext);
			}
		}

		Utilities.generateInvocationIdIfNecessary(backend, filterConfiguration);
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
