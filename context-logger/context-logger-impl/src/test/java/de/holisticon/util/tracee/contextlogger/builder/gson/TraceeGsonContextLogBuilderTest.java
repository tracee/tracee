package de.holisticon.util.tracee.contextlogger.builder.gson;

import de.holisticon.util.tracee.contextlogger.RegexMatcher;
import de.holisticon.util.tracee.contextlogger.data.subdata.java.JavaThrowable;
import de.holisticon.util.tracee.contextlogger.data.subdata.tracee.PassedContextDataProvider;
import org.hamcrest.MatcherAssert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

/**
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
            classes.add(JavaThrowable.class);

            logBuilder.setWrapperClasses(classes);

            String json = logBuilder.log(new JavaThrowable(e));

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
            classes.add(PassedContextDataProvider.class);
            classes.add(JavaThrowable.class);

            logBuilder.setWrapperClasses(classes);

            Object[] oarray = new Object[2];
            oarray[0] = new JavaThrowable(e);
            oarray[1] = "TATA";
            PassedContextDataProvider cp = new PassedContextDataProvider(oarray);


            String json = logBuilder.log(cp);

            MatcherAssert.assertThat(json, RegexMatcher.matches("\\[\\{\\\"throwable\\\":\\{\\\"stacktrace\\\":\\\"java.lang.NullPointerException.*java.lang.String\\\":\\\"TATA\\\"\\}\\]"));


        }
    }
}
