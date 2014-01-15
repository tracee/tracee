package de.holisticon.util.tracee;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

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
        Random r = ThreadLocalRandom.current();
        return new UUID(r.nextLong(), r.nextLong()).toString().replace("-", "");
    }

}
