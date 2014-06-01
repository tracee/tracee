package io.tracee.contextlogger.jaxws.container;

/**
 * This class is not directly used but exists for the users convenience
 * (Like adding @HandlerChain(xmlFile=TraceeContextLoggerJaxwsConstants.TRACEE_WITH_ERROR_LOGGING_HANDLER_CHAIN_URL)
 */
@SuppressWarnings("UnusedDeclaration")
public final class TraceeContextLoggerJaxwsConstants {

	private TraceeContextLoggerJaxwsConstants() { }

	public static final String TRACEE_WITH_ERROR_LOGGING_HANDLER_CHAIN_URL =
			"/io/tracee/contextlogger/jaxws/TraceeWithErrorLoggingHandlerChain.xml";

}
