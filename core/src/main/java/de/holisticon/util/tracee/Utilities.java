package de.holisticon.util.tracee;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.UUID;

/**
 * Created by Tobias Gindler, holisticon AG on 11.12.13.
 */
public final class Utilities {

    private Utilities() {
        // hide constructor
    }

    public static String convertStacktraceToString(Throwable e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

    public static String createUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
