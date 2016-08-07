package io.tracee.configuration;

import java.util.Map;

public interface TraceeFilterConfiguration {

	enum Channel {
		/**
		 * Accepting context from incoming requests.
		 */
		IncomingRequest,
		/**
		 * Propagate context on outgoing responses.
		 */
		OutgoingResponse,
		/**
		 * Propagate context on outgoing requests.
		 */
		OutgoingRequest,
		/**
		 * Accept context from incoming responses.
		 */
		IncomingResponse,
		/**
		 * Propagate context over asynchronous boundaries.
		 */
		AsyncDispatch,
		/**
		 * Accept context when starting over after an asynchronous boundary.
		 */
		AsyncProcess
	}

	/**
	 * Tracee ships with a set of builtin profiles.
	 */
	final class Profile {

		/**
		 * Default Profile.
		 * Permits Tracee headers to flow into all directions.
		 */
		public static final String DEFAULT = "default";

		/**
		 * HideInbound Profile.
		 * Prevents responding with a Tracee-Header in OutgoingResponses.
		 */
		public static final String HIDE_INBOUND = "HideInbound";
		/**
		 * HideOutbound Profile.
		 * Prevents passing of any Tracee header in OutgoingRequests.
		 */
		public static final String HIDE_OUTBOUND = "HideOutbound";
		/**
		 * DisableInbound Profile.
		 * Prevents parsing of any Tracee header from IncomingRequests and prevents responding with a Tracee-Header in OutgoingResponses.
		 */
		public static final String DISABLE_INBOUND = "DisableInbound";
		/**
		 * DisableOutbound Profile.
		 * Prevents passing of any Tracee header in OutgoingRequests and prevents parsing of any Tracee-Header in IncomingResponses.
		 */
		public static final String DISABLE_OUTBOUND = "DisableOutbound";
		/**
		 * Disabled Profile.
		 * Completely disables tracee context processing on all channels.
		 */
		public static final String DISABLED = "Disabled";

		private Profile() {
		}
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


	boolean shouldGenerateInvocationId();

	/**
	 * @return a desired non-negative length of the generated request identifiers. If it returns <code>0</code>, no request identifiers should be generated.
	 */
	int generatedInvocationIdLength();


	boolean shouldGenerateSessionId();

	/**
	 * @return a desired non-negative length of the generated session identifiers. If it returns <code>0</code>, no session identifiers should be generated.
	 */
	int generatedSessionIdLength();


}
