package de.holisticon.util.tracee;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Random;

/**
 * Created by Tobias Gindler, holisticon AG
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



    private static final char[] ALPHANUMERICS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();

    /**
     * Creates a random Strings consisting of alphanumeric charaters with a length of 32.
     */
    public static String createRandomAlphanumeric() {
        final int length = 32;
        final Random r = ThreadLocalRandom.current();
        final char[] randomChars = new char[length];
        for (int i=0; i<length; ++i) {
            randomChars[i] = ALPHANUMERICS[r.nextInt(ALPHANUMERICS.length)];
        }
        return new String(randomChars);
    }

}
