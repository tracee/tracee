package de.holisticon.util.tracee.configuration;

import java.util.Map;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
public interface TraceeFilterConfiguration {

	enum Channel {
		IncomingRequest,
		OutgoingResponse,
		OutgoingRequest,
		IncomingResponse,
		AsyncDispatch,
		AsyncProcess
	}

	/**
	 * Returns true if a given TracEE context parameter should be parsed or sent on a given channel.
	 */
	boolean shouldProcessParam(String paramName, Channel channel);

	/**
	 * Returns a map that is a filtered copy of the given {@code unfiltered} map. It contains only keys, that
	 * are allowed to be processed on this channel.
	 */
	Map<String, String> filterDeniedParams(Map<String, String> unfiltered, Channel channel);

	/**
	 * @return {@code true} if the current configuration allows context processing on the given {@code channel} at all, {@code} false otherwise.
	 */
	boolean shouldProcessContext(Channel channel);


	boolean shouldGenerateRequestId();
	/**
	 * @return a desired non-negative length of the generated request identifiers. If it returns <code>0</code>, no request identifiers should be generated.
	 */
	int generatedRequestIdLength();


	boolean shouldGenerateSessionId();

	/**
	 * @return a desired non-negative length of the generated session identifiers. If it returns <code>0</code>, no session identifiers should be generated.
	 */
	int generatedSessionIdLength();


}
