package de.holisticon.util.tracee.contextlogger.data;

/**
 * Order of known providers is defined here
 * Created by Tobias Gindler, holisticon AG on 14.03.14.
 */
public final class Order {
    @SuppressWarnings("unused")
    private Order () {
        // hide constructor
    }
    public static final int COMMON = 0;
    public static final int TRACEE = 10;
    public static final int WATCHDOG = 15;
    public static final int SERVLET = 20;
    public static final int JAXWS = 30;
    public static final int EXCEPTION = 80;

}
