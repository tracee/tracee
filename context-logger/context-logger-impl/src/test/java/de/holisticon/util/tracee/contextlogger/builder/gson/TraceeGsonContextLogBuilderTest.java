package de.holisticon.util.tracee.contextlogger.builder.gson;

import de.holisticon.util.tracee.contextlogger.RegexMatcher;
import de.holisticon.util.tracee.contextlogger.data.subdata.java.JavaThrowableContextProvider;
import de.holisticon.util.tracee.contextlogger.data.subdata.tracee.PassedDataContextProvider;
import org.hamcrest.MatcherAssert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * Test class for {@link de.holisticon.util.tracee.contextlogger.builder.gson.TraceeGsonContextLogBuilder}.
 * Created by Tobias Gindler on 20.03.14.
 */
public class TraceeGsonContextLogBuilderTest {


    @Test
    public void should_create_context_log () {
        try {
            throw new NullPointerException();
        } catch (NullPointerException e) {

            TraceeGsonContextLogBuilder logBuilder = new TraceeGsonContextLogBuilder();
            Set<Class> classes = new HashSet<Class>();
            classes.add(JavaThrowableContextProvider.class);

            logBuilder.setWrapperClasses(classes);

            String json = logBuilder.log(new JavaThrowableContextProvider(e));

            MatcherAssert.assertThat(json, RegexMatcher.matches("\\[\\{\\\"stacktrace\\\":\\\"java.lang.Null.*\\}\\]"));


        }
    }

    @Test
    public void should_create_mixed_context_log () {
        try {
            throw new NullPointerException();
        } catch (NullPointerException e) {

            TraceeGsonContextLogBuilder logBuilder = new TraceeGsonContextLogBuilder();
            Set<Class> classes = new HashSet<Class>();
            classes.add(PassedDataContextProvider.class);
            classes.add(JavaThrowableContextProvider.class);

            logBuilder.setWrapperClasses(classes);

            Object[] oarray = new Object[2];
            oarray[0] = new JavaThrowableContextProvider(e);
            oarray[1] = "TATA";
            PassedDataContextProvider cp = new PassedDataContextProvider(oarray);


            String json = logBuilder.log(cp);

            MatcherAssert.assertThat(json, RegexMatcher.matches("\\[\\{\\\"throwable\\\":\\{\\\"stacktrace\\\":\\\"java.lang.NullPointerException.*java.lang.String\\\":\\\"TATA\\\"\\}\\]"));


        }
    }
}
