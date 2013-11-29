package de.holisticon.util.tracee.backend.threadlocalstore;

import de.holisticon.util.tracee.Tracee;
import de.holisticon.util.tracee.TraceeBackend;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;


/**
 * @author Daniel
 */
public class ThreadLocalContextProviderIT {


    @Test
    public void testLoadProvierThenStoreAndRetrieve() {
        final TraceeBackend context = Tracee.getBackend();
        context.put("FOO", "BAR");
        assertThat(context.get("FOO"), equalTo("BAR"));
        context.clear();
    }


}
