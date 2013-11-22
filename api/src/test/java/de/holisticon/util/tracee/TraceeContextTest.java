package de.holisticon.util.tracee;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Daniel
 */
public class TraceeContextTest {

    private final TraceeContext unit = new TraceeContext();

    @Test
    public void testEscape() throws Exception {
        assertThat(unit.escape("hello, world"), equalTo("hello\\, world"));
    }

    @Test
    public void testSymmetrical() throws Exception {
        final String value = "hello, : world";
        assertThat(unit.unescape(unit.escape(value)), equalTo(value));
    }

    @Test
    public void testSymmetrical2() throws Exception {
        final String value = "hello, \\: world";
        assertThat(unit.unescape(unit.escape(value)), equalTo(value));
    }

    @Test
    public void testSymmetricalSerialization() {
        final String value = "key:value,key2=value2";
        final TraceeContext unit = new TraceeContext(value);
        unit.put("ke:y","va,lue");
        assertThat(unit.toString(), equalTo(value));
        assertThat(unit.get("key"), equalTo("value"));
        assertThat(unit.get("key2"), equalTo("value2"));
    }
}
