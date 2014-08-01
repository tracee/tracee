package io.tracee.examples.springaop;

import io.tracee.contextlogger.watchdog.Watchdog;

/**
 * Created by TGI on 01.08.14.
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
