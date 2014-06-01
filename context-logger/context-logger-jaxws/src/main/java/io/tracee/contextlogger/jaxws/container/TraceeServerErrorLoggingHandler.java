package io.tracee.contextlogger.jaxws.container;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.TraceeLogger;
import io.tracee.contextlogger.ImplicitContext;
import io.tracee.contextlogger.builder.TraceeContextLogger;
import io.tracee.contextlogger.data.wrapper.JaxWsWrapper;
import io.tracee.jaxws.AbstractTraceeHandler;
import io.tracee.jaxws.container.TraceeServerHandler;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.io.ByteArrayOutputStream;
import java.util.Set;

/**
 * JaxWs container side handler that detects uncaught exceptions and outputs contextual information.
 *
 */
public class TraceeServerErrorLoggingHandler extends AbstractTraceeHandler {

    private final TraceeLogger logger = this.getTraceeBackend().getLoggerFactory().getLogger(
			TraceeServerHandler.class);

    private static final ThreadLocal<String> THREAD_LOCAL_SOAP_MESSAGE_STR = new ThreadLocal<String>();

	TraceeServerErrorLoggingHandler(TraceeBackend traceeBackend) {
		super(traceeBackend);
	}

	public TraceeServerErrorLoggingHandler() {
		this(Tracee.getBackend());
	}

	@Override
    public final boolean handleFault(SOAPMessageContext context) {

        // Must pipe out Soap envelope
        SOAPMessage soapMessage = context.getMessage();

        TraceeContextLogger.createDefault().logJsonWithPrefixedMessage(
                "TRACEE JMS ERROR CONTEXT LISTENER",
                ImplicitContext.COMMON,
                ImplicitContext.TRACEE,
                JaxWsWrapper.wrap(THREAD_LOCAL_SOAP_MESSAGE_STR.get(),
                        getSoapMessageAsString(soapMessage)));

        return true;
    }

    /**
     * Converts a SOAPMessage instance to string representation.
     */
    String getSoapMessageAsString(SOAPMessage soapMessage) {
		if (soapMessage == null) {
			return "null";
		}
		try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            soapMessage.writeTo(os);
            return new String(os.toByteArray(), "UTF-8");
        } catch (Exception e) {
			logger.error("Couldn't create string representation of soapMessage: " + soapMessage.toString());
            return "ERROR";
        }
    }


    @Override
    protected final void handleIncoming(SOAPMessageContext context) {
        // Save soap request message in thread local storage for error logging
        SOAPMessage soapMessage = context.getMessage();
        if (soapMessage != null) {
            String soapMessageAsString = getSoapMessageAsString(soapMessage);
            THREAD_LOCAL_SOAP_MESSAGE_STR.set(soapMessageAsString);
        }
    }

	@Override
	public void close(MessageContext context) {
		super.close(context);
		// cleanup thread local request soap message
		THREAD_LOCAL_SOAP_MESSAGE_STR.remove();
	}

	@Override
    protected final void handleOutgoing(SOAPMessageContext context) {
        // Do nothing
    }


    @Override
    public final Set<QName> getHeaders() {
        return null;
    }
}
