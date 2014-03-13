package de.holisticon.util.tracee.contextlogger.jms;

import de.holisticon.util.tracee.Tracee;
import de.holisticon.util.tracee.TraceeBackend;
import de.holisticon.util.tracee.contextlogger.PrintableByConnector;
import de.holisticon.util.tracee.contextlogger.connector.ConnectorFactory;
import de.holisticon.util.tracee.contextlogger.json.generator.TraceeContextLoggerJsonBuilder;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.jms.Message;
import java.lang.reflect.Method;

/**
 * Message listener to detect exceptions that happened during jms message processing.
 * In case of an exception contextual information will be written to the log.
 * Created by Tobias Gindler, holisticon AG on 13.03.14.
 */
public class TraceeJmsErrorMessageListener {

    private final TraceeBackend backend;

    TraceeJmsErrorMessageListener(TraceeBackend backend) {
        this.backend = backend;
    }

    @SuppressWarnings("unused")
    public TraceeJmsErrorMessageListener() {
        this(Tracee.getBackend());
    }

    @AroundInvoke
    public Object intercept(InvocationContext ctx) throws Exception {
        final boolean isMdbInvocation = isMessageListenerOnMessageMethod(ctx.getMethod());
        try {
            return ctx.proceed();
        } catch (Exception e) {

            if (isMdbInvocation) {
                
                Message message = extractMessageParameter(ctx.getParameters());

                // TODO should write contextual log information (for example message)
                PrintableByConnector printable = TraceeContextLoggerJsonBuilder.createJsonCreator().addTraceeCategory(backend).addCommonCategory().addExceptionCategory(e);
                ConnectorFactory.sendErrorReportToConnectors(printable);
            }

            throw e;
        }
    }

    Message extractMessageParameter(Object[] parameters) {
        return (Message) parameters[0];
    }

    boolean isMessageListenerOnMessageMethod(Method method) {
        return "onMessage".equals(method.getName())
                && method.getParameterTypes().length == 1
                && method.getParameterTypes()[0] == Message.class;
    }

}
