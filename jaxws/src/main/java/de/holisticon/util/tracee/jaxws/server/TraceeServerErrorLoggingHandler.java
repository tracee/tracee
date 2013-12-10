package de.holisticon.util.tracee.jaxws.server;

import de.holisticon.util.tracee.TraceeErrorConstants;
import de.holisticon.util.tracee.TraceeLogger;
import de.holisticon.util.tracee.jaxws.AbstractTraceeHandler;
import org.json.JSONObject;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Tobias Gindler, holisticon AG on 06.12.13.
 */
public class TraceeServerErrorLoggingHandler extends AbstractTraceeHandler {

    private final TraceeLogger traceeLogger = this.getTraceeBackend().getLogger(
            TraceeServerHandler.class);

    private static final ThreadLocal<String> threadLocalSoapMessageStr = new ThreadLocal<String>();

    @Override
    public final boolean handleFault(SOAPMessageContext context) {

        // Must pipe out Soap envelope
        SOAPMessage soapMessage = context.getMessage();

        Map<String, Object> map = new HashMap<String, Object>();

        // add soap request and response to map
        map.put(TraceeErrorConstants.JAX_WS_REQUEST_SOAP_MESSAGE,
                threadLocalSoapMessageStr.get());

        map.put(TraceeErrorConstants.JAX_WS_RESPONSE_SOAP_MESSAGE,
                getSoapMessageAsString(soapMessage));

        // write log message
        traceeLogger.error("TraceeServerErrorLoggingHandler - FAULT :\n "
                + new JSONObject(map).toString());

        // cleanup thread local request soap message
        threadLocalSoapMessageStr.remove();

        return true;

    }

    /**
     * Converts a SOAPMessage instance to string representation.
     *
     * @param soapMessage
     * @return
     */
    private String getSoapMessageAsString(SOAPMessage soapMessage) {

        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            soapMessage.writeTo(os);
            return new String(os.toByteArray(), "UTF-8");
        } catch (Exception e) {
            return "ERROR";
        }
    }


    @Override
    protected final void handleInbound(SOAPMessageContext context) {
        // Save soap request message in thread local storage for error logging
        SOAPMessage soapMessage = context.getMessage();
        if (soapMessage != null) {
            String soapMessageAsString = getSoapMessageAsString(soapMessage);
            threadLocalSoapMessageStr.set(soapMessageAsString);
        }

    }

    @Override
    protected final void handleOutbound(SOAPMessageContext context) {
        // Do nothing
    }


    @Override
    public Set<QName> getHeaders() {
        return null;
    }
}
