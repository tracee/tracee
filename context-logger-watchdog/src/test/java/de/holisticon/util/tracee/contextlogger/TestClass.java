package de.holisticon.util.tracee.contextlogger;

/**
 * Created by TGI on 17.02.14.
 */
public class TestClass {

    @Watchdog
    public void throwException(final String abc, String def) {

        abc.equals(def);
    }
    public static void main(final String[] args) {
        new TestClass().throwException(null,"abc");
    }

}
