package de.holisticon.util.tracee.contextlogger.watchdog;

@Watchdog
public class TestClass2 {

    public void throwException(final String abc, String def) {

        abc.equals(def);
    }

    public static void main(final String[] args) {
        new TestClass2().throwException(null, "abc");
    }

}
