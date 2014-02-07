package de.holisticon.util.tracee;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.Iterator;
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


    /**
     * Wraps an Iterable[T] as Enumeration[T]
     */
    public static <T> Enumeration<T> toEnumeration(Iterable<T> iterable) {
        final Iterator<T> iterator = iterable.iterator();
        return new Enumeration<T>() {
            @Override
            public boolean hasMoreElements() {
                return iterator.hasNext();
            }

            @Override
            public T nextElement() {
                return iterator.next();
            }
        };
    }



    private static final char[] ALPHANUMERICS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();


    /**
     * Creates a random Strings consisting of alphanumeric charaters with a length of 32.
     */
    public static String createRandomAlphanumeric(int length) {
        final Random r = ThreadLocalRandom.current();
        final char[] randomChars = new char[length];
        for (int i=0; i<length; ++i) {
            randomChars[i] = ALPHANUMERICS[r.nextInt(ALPHANUMERICS.length)];
        }
        return new String(randomChars);
    }

    /**
     * Creates a alphanumeric projection with a given length of the given object using its {@link Object#hashCode()}
     */
    public static String createAlphanumericHash(Object o, int length) {
        final int hashCode = o.hashCode();
        // TODO using seeds is not supported - a UnsupportedOperationException will be thrown
        // ThreadLocalRandom.current().setSeed(hashCode);
        return createRandomAlphanumeric(length);
    }



}
