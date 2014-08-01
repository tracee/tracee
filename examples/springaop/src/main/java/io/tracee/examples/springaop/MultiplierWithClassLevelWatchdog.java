package io.tracee.examples.springaop;

import io.tracee.contextlogger.watchdog.Watchdog;

/**
 * Multiplier with type level watchdog annotation (affects all public methods).
 */
@Watchdog(id = "MULTIPLIER_BEAN_WITH_CLASS_LEVEL_WATCHDOG",isActive = true)
public class MultiplierWithClassLevelWatchdog {

    public int multiply (int a, int b) {
        if (a < 5) {
            throw new IllegalArgumentException(" argument a=" + a + " triggered exception");
        }

        return a*b;
    }

}
