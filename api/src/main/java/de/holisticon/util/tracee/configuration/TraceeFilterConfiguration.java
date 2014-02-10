package de.holisticon.util.tracee.configuration;

/**
 * @author Daniel Wegener (Holisticon AG)
 */
public interface TraceeFilterConfiguration {

	enum MessageType {
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
	boolean shouldPropagate(String paramName, MessageType channel);

	boolean shouldProcessContext(MessageType channel);

	/**
	 * @return a desired non-negative length of the generated request identifiers. If it returns <code>0</code>, no request identifiers should be generated.
	 */
	int generatedRequestIdLength();

	/**
	 * @return a desired non-negative length of the generated session identifiers. If it returns <code>0</code>, no session identifiers should be generated.
	 */
	int generatedSessionIdLength();


}
