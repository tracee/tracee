package de.holisticon.util.tracee.contextlogger.data.subdata.java;

import de.holisticon.util.tracee.contextlogger.api.TraceeContextLogProvider;
import de.holisticon.util.tracee.contextlogger.api.TraceeContextLogProviderMethod;
import de.holisticon.util.tracee.contextlogger.data.Order;
import de.holisticon.util.tracee.contextlogger.profile.ProfilePropertyNames;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Provides context information about thrown exceptions.
 * Created by Tobias Gindler, holisticon AG on 17.03.14.
 */
@SuppressWarnings("unused")
@TraceeContextLogProvider(displayName = "throwable", order = Order.EXCEPTION)
public final class JavaThrowable {


    private final Throwable throwable;

    @SuppressWarnings("unused")
    public JavaThrowable(final Throwable throwable) {
        this.throwable = throwable;
    }

    @SuppressWarnings("unused")
    @TraceeContextLogProviderMethod(
            displayName = "message",
            propertyName = ProfilePropertyNames.EXCEPTION_MESSAGE,
            order = 10)
    public String getMessage() {
        return throwable.getMessage();
    }

    @SuppressWarnings("unused")
    @TraceeContextLogProviderMethod(
            displayName = "stacktrace",
            propertyName = ProfilePropertyNames.EXCEPTION_STACKTRACE,
            order = 20)
    public String getStacktrace() {
        StringWriter sw = new StringWriter();
        throwable.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

}
