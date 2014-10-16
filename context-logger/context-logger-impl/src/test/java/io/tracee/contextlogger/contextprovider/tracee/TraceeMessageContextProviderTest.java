package io.tracee.contextlogger.contextprovider.tracee;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;

import org.hamcrest.Matchers;
import org.junit.Test;

import io.tracee.contextlogger.RegexMatcher;
import io.tracee.contextlogger.TraceeContextLogger;

/**
 * Unit test for {@link TraceeMessageContextProvider}.
 */
public class TraceeMessageContextProviderTest {

    @Test
    public void should_handle_null_valued_message_correctly() {
        Object message = null;
        TraceeMessageContextProvider unit = new TraceeMessageContextProvider(TraceeMessage.wrap(message));

        assertThat(unit.getValue(), nullValue());
    }

    @Test
    public void should_handle_message_correctly() {
        Object message = "ABC";
        TraceeMessageContextProvider unit = new TraceeMessageContextProvider(TraceeMessage.wrap(message));

        assertThat(unit.getValue(), Matchers.is(message.toString()));
    }

    @Test
    public void should_generate_json() {
        Object message = "ABC";

        String result = TraceeContextLogger.createDefault().createJson(TraceeMessage.wrap(message));

        assertThat(result, RegexMatcher.matches("\\{\\\"message\\\":\\{\\\"value\\\":\\\"ABC\\\"\\}\\}"));
    }

}
