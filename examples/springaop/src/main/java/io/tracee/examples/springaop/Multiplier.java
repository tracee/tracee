package io.tracee.examples.springaop;

import io.tracee.contextlogger.watchdog.Watchdog;

/**
 * Multiplier with method level watchdog annotation.
 */
public class Multiplier {

    @Watchdog(id = "MULTIPLIER_BEAN",isActive = true)
    public int multiply (int a, int b) {
        if (a < 5) {
            throw new IllegalArgumentException(" argument a=" + a + " triggered exception");
        }

        return a*b;
    }

}
